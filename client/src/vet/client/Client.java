package vet.client;

import java.lang.reflect.Proxy;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import vet.common.Animal;
import vet.common.Cabinet;
import vet.common.Observation;
import vet.common.PatientNotFoundException;
import vet.common.PatientRecord;
import vet.common.Species;

/**
 * Le CLIENT. Il ne connait que l'interface Animal, jamais AnimalImpl.
 *
 * Usage : java vet.client.Client [hote]
 */
public class Client {

    private static void afficherTousLesPatients(Cabinet cabinet) throws RemoteException {
        System.out.println("Liste des patients :");
        List<Animal> patients = cabinet.getPatients();
        int indexPatient = 1;
        for (Animal patient : patients) {
            System.out.println("    Patient " + indexPatient++ + " :" + patient.getName());
        }
    }

    private static Animal rechercherPatient(Cabinet cabinet, String name)
            throws PatientNotFoundException, RemoteException {
        Animal patient = cabinet.getPatient(name);
        return patient;
    }

    private static void rechercherPatientInexistant(Cabinet cabinet, String name) throws RemoteException {
        try {
            cabinet.getPatient(name);
        } catch (PatientNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void afficherInfosAnimal(Animal animal) throws RemoteException {
        System.out.println("Infos sur le patient " + animal.getName() + ":");
        System.out.println("nom     : " + animal.getName());
        System.out.println("maitre  : " + animal.getOwnerName());
        System.out.println("race    : " + animal.getRace());
        System.out.println("espece  : " + animal.getSpecies().getName());
        System.out.println("classe du stub : " + animal.getClass().getName());
        System.out.println("proxy dynamique ? " + Proxy.isProxyClass(animal.getClass()));
    }

    private static void testerPassageParValeur(Animal animal) throws RemoteException {
        Species animalSpecies = animal.getSpecies();
        System.out.println("# identite des objets");
        System.out.println("[getSpecies] identityHashCode : " + System.identityHashCode(animal.getSpecies()));
        System.out.println("[animalSpecies] identityHashCode : " + System.identityHashCode(animalSpecies) + "\n");
        animalSpecies.setAverageLife(99);
        System.out.println("# apres la mutation locale du client [animalSpecies]");
        System.out.println("[getSpecies] " + animal.getSpecies());
        System.out.println("[animalSpecies] " + animalSpecies);
    }

    private static void testerDossierPatient(Animal animal) throws RemoteException {
        System.out.println("Recup du dossier patient de " + animal.getName());
        PatientRecord animalRecord = animal.getRecord();
        Observation observation = new Observation("Il boit dans sa fontaine à eau !");
        animalRecord.addObservation(observation);
        System.out.println("Observation ajoutee");
        animalRecord.setHealthStatus("bonne santé");
        System.out.println("Statut de sante modifie : " + animalRecord.getHealthStatus());

        System.out.println("Liste des observations : ");
        for (Observation focus : animalRecord.getObservations()) {
            System.out.println("  " + focus.getDate() + ": " + focus.getObservation());
        }
    }

    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host, 1099);
            Cabinet cabinet = (Cabinet) registry.lookup("cabinet");
            // A4
            afficherTousLesPatients(cabinet);
            rechercherPatientInexistant(cabinet, "Nom qui n'existe pas");

            try {
                Animal link = rechercherPatient(cabinet, "Link");
                System.out.println("Patient 'Link' trouvé");
                // A1
                System.out.println("------------");
                afficherInfosAnimal(link);

                // A2
                System.out.println("------------");
                testerPassageParValeur(link);

                // // A3
                System.out.println("------------");
                testerDossierPatient(link);
            } catch (PatientNotFoundException e) {
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("Client exception: " + e);
            e.printStackTrace();
        }
    }
}