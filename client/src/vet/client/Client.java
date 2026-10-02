package vet.client;

import java.lang.reflect.Proxy;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import vet.common.Patient;
import vet.common.Cabinet;
import vet.common.Observation;
import vet.common.exceptions.PatientAlreadyExistsException;
import vet.common.exceptions.PatientNotFoundException;
import vet.common.PatientRecord;
import vet.common.Species;

/**
 * Le CLIENT. Il ne connait que l'interface Patient, jamais PatientImpl.
 *
 * Usage : java vet.client.Client [hote]
 */
public class Client {

    private static void afficherTousLesPatients(Cabinet cabinet) throws RemoteException {
        System.out.println("Liste des patients :");
        List<Patient> patients = cabinet.getPatients();
        int indexPatient = 1;
        for (Patient patient : patients) {
            System.out.println("    Patient " + indexPatient++ + " :" + patient.getName());
        }
    }

    private static Patient rechercherPatient(Cabinet cabinet, String name)
            throws PatientNotFoundException, RemoteException {
        Patient patient = cabinet.getPatient(name);
        return patient;
    }

    private static void rechercherPatientInexistant(Cabinet cabinet, String name) throws RemoteException {
        try {
            cabinet.getPatient(name);
        } catch (PatientNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void afficherInfosPatient(Patient patient) throws RemoteException {
        System.out.println("Infos sur le patient " + patient.getName() + ":");
        System.out.println("nom     : " + patient.getName());
        System.out.println("maitre  : " + patient.getOwnerName());
        System.out.println("race    : " + patient.getRace());
        System.out.println("espece  : " + patient.getSpecies().getName());
        System.out.println("classe du stub : " + patient.getClass().getName());
        System.out.println("proxy dynamique ? " + Proxy.isProxyClass(patient.getClass()));
    }

    private static void testerPassageParValeur(Patient patient) throws RemoteException {
        Species patientSpecies = patient.getSpecies();
        System.out.println("# identite des objets");
        System.out.println("[getSpecies] identityHashCode : " + System.identityHashCode(patient.getSpecies()));
        System.out.println("[patientSpecies] identityHashCode : " + System.identityHashCode(patientSpecies) + "\n");
        patientSpecies.setAverageLife(99);
        System.out.println("# apres la mutation locale du client [patientSpecies]");
        System.out.println("[getSpecies] " + patient.getSpecies());
        System.out.println("[patientSpecies] " + patientSpecies);
    }

    private static void testerDossierPatient(Patient patient) throws RemoteException {
        System.out.println("Recup du dossier patient de " + patient.getName());
        PatientRecord patientRecord = patient.getRecord();
        Observation observation = new Observation("Il boit dans sa fontaine à eau !");
        patientRecord.addObservation(observation);
        System.out.println("Observation ajoutee");
        patientRecord.setHealthStatus("bonne santé");
        System.out.println("Statut de sante modifie : " + patientRecord.getHealthStatus());

        System.out.println("Liste des observations : ");
        for (Observation focus : patientRecord.getObservations()) {
            System.out.println("  " + focus.getDate() + ": " + focus.getObservation());
        }
    }

    private static void createNewPatient(Cabinet cabinet) throws PatientNotFoundException, RemoteException {
        try {
            Species chat = new Species("Chat", 15);
            cabinet.createPatient("Morty", "Mathieu", "Colourpoint qui louche", chat);
            System.out.println("Patient 'Morty' créé");
        } catch (PatientAlreadyExistsException e) {
            System.out.println(e.getMessage());
            Patient morty = cabinet.getPatient("Morty");
            afficherInfosPatient(morty);
        }
    }

    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host, 1099);
            Cabinet cabinet = (Cabinet) registry.lookup("cabinet");
            // A4
            afficherTousLesPatients(cabinet);
            // rechercherPatientInexistant(cabinet, "Nom qui n'existe pas");

            // try {
            // Patient link = rechercherPatient(cabinet, "Link");
            // System.out.println("Patient 'Link' trouvé");
            // // A1
            // System.out.println("------------");
            // afficherInfosPatient(link);

            // // A2
            // System.out.println("------------");
            // testerPassageParValeur(link);

            // // A3
            // System.out.println("------------");
            // testerDossierPatient(link);
            // } catch (PatientNotFoundException e) {
            // System.out.println(e.getMessage());
            // }
            // A5
            System.out.println("------------");
            createNewPatient(cabinet);

        } catch (Exception e) {
            System.err.println("Client exception: " + e);
            e.printStackTrace();
        }
    }
}