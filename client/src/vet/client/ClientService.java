package vet.client;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import vet.common.Cabinet;
import vet.common.CabinetObserver;
import vet.common.Observation;
import vet.common.Patient;
import vet.common.PatientRecord;
import vet.common.Species;
import vet.common.exceptions.PatientAlreadyExistsException;
import vet.common.exceptions.PatientNotFoundException;

public class ClientService {

    /** Copie locale d'un patient : aucune méthode distante, donc aucun RemoteException. */
    public record PatientInfo(String nom, String proprietaire, String race, String espece) {
    }

    private final Cabinet cabinet;
    private CabinetObserver observer;

    public ClientService(Cabinet cabinet) {
        this.cabinet = cabinet;
    }

    // ---------- Patients ----------

    public List<PatientInfo> listerPatients() throws ClientException {
        try {
            List<PatientInfo> infos = new ArrayList<>();
            for (Patient p : cabinet.getPatients()) {
                infos.add(versInfo(p));
            }
            return infos;
        } catch (RemoteException e) {
            throw traduire(e);
        }
    }

    public PatientInfo rechercherPatient(String nom) throws ClientException {
        try {
            return versInfo(cabinet.getPatient(nom));
        } catch (PatientNotFoundException e) {
            throw new ClientException("Aucun patient nommé '" + nom + "'.", e);
        } catch (RemoteException e) {
            throw traduire(e);
        }
    }

    public PatientInfo creerPatient(String nom, String proprietaire, String race,
                                    String nomEspece, int esperanceVie) throws ClientException {
        try {
            Species espece = new Species(nomEspece, esperanceVie);
            return versInfo(cabinet.createPatient(nom, proprietaire, race, espece));
        } catch (PatientAlreadyExistsException e) {
            throw new ClientException("Un patient nommé '" + nom + "' existe déjà.", e);
        } catch (RemoteException e) {
            throw traduire(e);
        }
    }

    // ---------- Dossier de suivi (le patient est désigné par son nom) ----------

    public String consulterEtatSante(String nom) throws ClientException {
        try {
            return dossier(nom).getHealthStatus();
        } catch (RemoteException e) {
            throw traduire(e);
        }
    }

    public void modifierEtatSante(String nom, String nouvelEtat) throws ClientException {
        try {
            dossier(nom).setHealthStatus(nouvelEtat);
        } catch (RemoteException e) {
            throw traduire(e);
        }
    }

    /** Renvoie des lignes déjà formatées "date : texte". */
    public List<String> consulterObservations(String nom) throws ClientException {
        try {
            List<String> lignes = new ArrayList<>();
            for (Observation o : dossier(nom).getObservations()) {
                lignes.add(o.getDate() + " : " + o.getObservation());
            }
            return lignes;
        } catch (RemoteException e) {
            throw traduire(e);
        }
    }

    public void ajouterObservation(String nom, String texte) throws ClientException {
        try {
            dossier(nom).addObservation(new Observation(texte));
        } catch (RemoteException e) {
            throw traduire(e);
        }
    }

    // ---------- Alertes ----------

    public boolean estAbonne() {
        return observer != null;
    }

    public void sAbonner() throws ClientException {
        CabinetObserver nouveau = null;
        try {
            nouveau = new CabinetObserverImpl();
            cabinet.subscribe(nouveau);
            observer = nouveau;
        } catch (RemoteException e) {
            retirerExport(nouveau);
            throw traduire(e);
        }
    }

    public void seDesabonner() throws ClientException {
        CabinetObserver obs = observer;
        observer = null;
        try {
            cabinet.unsubscribe(obs);
        } catch (RemoteException e) {
            throw traduire(e);
        } finally {
            retirerExport(obs);
        }
    }

    // ---------- Démo ----------

    public void creerPatientsEnMasse(int nombre) throws ClientException {
        try {
            for (int i = 1; i <= nombre; i++) {
                try {
                    cabinet.createPatient("Demo_" + i + "_" + System.nanoTime(),
                            "Test", "Test race", new Species("Test", 10));
                } catch (PatientAlreadyExistsException ignoree) {}
            }
        } catch (RemoteException e) {
            throw traduire(e);
        }
    }

    // ---------- Outils internes ----------

    private PatientInfo versInfo(Patient p) throws RemoteException {
        return new PatientInfo(p.getName(), p.getOwnerName(), p.getRace(), p.getSpecies().getName());
    }

    private PatientRecord dossier(String nom) throws ClientException, RemoteException {
        try {
            return cabinet.getPatient(nom).getRecord();
        } catch (PatientNotFoundException e) {
            throw new ClientException("Aucun patient nommé '" + nom + "'.", e);
        }
    }

    private static void retirerExport(CabinetObserver obs) {
        if (obs == null) {
            return;
        }
        try {
            UnicastRemoteObject.unexportObject(obs, true);
        } catch (NoSuchObjectException dejaRetire) {}
    }

    /** Parcourt la chaîne des causes pour produire un message compréhensible. */
    private static ClientException traduire(RemoteException e) {
        for (Throwable t = e; t != null; t = t.getCause()) {
            if (t instanceof java.net.ConnectException
                    || t instanceof java.rmi.ConnectException
                    || t instanceof java.rmi.ConnectIOException
                    || t instanceof java.net.UnknownHostException) {
                return new ClientException("Serveur injoignable. Vérifiez qu'il est démarré.", e);
            }
            if (t instanceof NoSuchObjectException) {
                return new ClientException(
                        "Objet distant introuvable : le serveur a sans doute redémarré. Relancez le client.", e);
            }
        }
        return new ClientException("Erreur de communication avec le serveur : " + e.getMessage(), e);
    }
}
