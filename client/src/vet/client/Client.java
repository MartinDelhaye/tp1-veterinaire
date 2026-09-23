package vet.client;

import java.lang.reflect.Proxy;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import vet.common.Animal;
import vet.common.Observation;
import vet.common.PatientRecord;
import vet.common.Species;

/**
 * Le CLIENT. Il ne connait que l'interface Animal, jamais AnimalImpl.
 *
 * Usage : java vet.client.Client [hote]
 */
public class Client {

    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host, 1099);
            Animal link = (Animal) registry.lookup("Link");

            System.out.println("classe du stub : " + link.getClass().getName());
            System.out.println("proxy dynamique ? " + Proxy.isProxyClass(link.getClass()));

            System.out.println("nom     : " + link.getName());
            System.out.println("maitre  : " + link.getOwnerName());
            System.out.println("race    : " + link.getRace());
            System.out.println("espece  : " + link.getSpecies());
            System.out.println("\n");

            Species linkSpecies = link.getSpecies();
            System.out.println("# identite des objets");
            System.out.println("[getSpecies] identityHashCode : " +  System.identityHashCode(link.getSpecies()));
            System.out.println("[linkSpecies] identityHashCode : "+  System.identityHashCode(linkSpecies));
            System.out.println("\n");

            linkSpecies.setAverageLife(99);  
            System.out.println("# apres la mutation locale du client [linkSpecies]");
            System.out.println("[getSpecies] "+link.getSpecies());
            System.out.println("[linkSpecies] "+ linkSpecies);

            System.out.println("Recup du dossier patient de Link");
            PatientRecord linkRecord = link.getRecord();
            Observation observation = new Observation("Il boit dans sa fontaine à eau !");
            linkRecord.addObservation(observation);
            System.out.println("Observation ajoutee");
            linkRecord.setHealthStatus("bonne santé");
            System.out.println("Statut de sante modifie : "+ linkRecord.getHealthStatus());

            System.out.println("Liste des observations : ");
            for (Observation focus : linkRecord.getObservations()) {
                System.out.println("  " + focus.getDate() + ": " + focus.getObservation());
            }


        } catch (Exception e) {
            System.err.println("Client exception: " + e);
            e.printStackTrace();
        }
    }
}