package vet.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import vet.common.Patient;
import vet.common.AlerteSeuil;
import vet.common.Cabinet;
import vet.common.CabinetObserver;
import vet.common.exceptions.PatientNotFoundException;
import vet.common.Species;
import vet.common.exceptions.PatientAlreadyExistsException;

public class CabinetImpl extends UnicastRemoteObject implements Cabinet {

    private final Map<String, Patient> patients = new HashMap<String, Patient>();
    private final List<CabinetObserver> abonnes = new CopyOnWriteArrayList<CabinetObserver>();

    public CabinetImpl() throws RemoteException {
        super();
        Species chat = new Species("Chat", 15);
        Patient link = new PatientImpl("Link", "Martin", "Chat noir et blanc trop mignon", chat);
        patients.put(link.getName(), link);
    }

    public List<Patient> getPatients() throws RemoteException {
        return new ArrayList<Patient>(patients.values());
    }

    public Patient getPatient(String name) throws PatientNotFoundException, RemoteException {
        Patient patient = patients.get(name);
        if (patient == null) {
            throw new PatientNotFoundException(name);
        }
        return patient;
    }

    public Patient createPatient(String name, String ownerName, String race, Species species)
            throws RemoteException, PatientAlreadyExistsException {
        if (patients.containsKey(name)) {
            throw new PatientAlreadyExistsException(name);
        }
        int avant = patients.size();
        Patient patient = new PatientImpl(name, ownerName, race, species);
        patients.put(name, patient);
        int apres = patients.size();
        verifierSeuils(avant, apres);
        return patient;
    }

    public void subscribe(CabinetObserver observer) throws RemoteException {
        abonnes.add(observer);
    }

    public void unsubscribe(CabinetObserver observer) throws RemoteException {
        abonnes.remove(observer);
    }

    private void verifierSeuils(int avant, int apres) throws RemoteException {
        int[] seuils = { 100, 500, 1000 };
        for (int seuil : seuils) {
            boolean etaitAuDessus = avant >= seuil;
            boolean estAuDessus = apres >= seuil;
            if (etaitAuDessus != estAuDessus) {
                AlerteSeuil.Sens sens = estAuDessus ? AlerteSeuil.Sens.HAUSSE : AlerteSeuil.Sens.BAISSE;
                AlerteSeuil alerte = new AlerteSeuil(seuil, sens);
                for (CabinetObserver abonne : abonnes) {
                    try {
                        abonne.notifierSeuil(alerte);
                    } catch (RemoteException e) {
                        unsubscribe(abonne);
                        System.out.println("[cabinet] observateur defaillant retire : " + e.getMessage());
                    }
                }
            }
        }
    }

}
