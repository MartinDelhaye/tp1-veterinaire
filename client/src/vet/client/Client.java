package vet.client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import vet.common.Cabinet;
import vet.common.CabinetObserver;
import vet.common.Chien;
import vet.common.Patient;
import vet.common.Species;
import vet.common.exceptions.PatientAlreadyExistsException;

public class Client {
    private static void createPatientsInBulk(
            Cabinet cabinet,
            int count) throws RemoteException {

        System.out.println("------------ Création de " + count + " patients ------------");

        for (int i = 1; i <= count; i++) {
            System.out.println("Création du patient " + i + "...");
            try {
                cabinet.createPatient(
                        String.valueOf(i),
                        "Test",
                        "Test race",
                        new Species("Test", 10));
            } catch (PatientAlreadyExistsException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        String mode = (args.length > 0) ? args[0] : "";
        String host = (args.length > 1) ? args[1] : null;
        try {
            Registry registry = LocateRegistry.getRegistry(host, 1099);
            Cabinet cabinet = (Cabinet) registry.lookup("cabinet");

            CabinetObserver observer = new CabinetObserverImpl();
            cabinet.subscribe(observer);
            System.out.println("Abonné aux alertes de seuils, en attente d'alertes...");
            System.out.println("--------- Création d'un patient avec la class Chien ---------");
            try {
                Chien dogSpecies = new Chien();
                cabinet.createPatient("Zébulon", "Mamie d'amour", "jsp", dogSpecies);

                Patient zebulon = cabinet.getPatient("Zébulon");
                System.out.println("nom : " + zebulon.getName());
                System.out.println("espèce : " + zebulon.getSpecies().getName());

            } catch (Exception e) {
                System.err.println("Client exception: " + e);
            }
            switch (mode) {
                case "--createur":
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("Combien de patients voulez-vous créer ? ");
                    int chiffre = scanner.nextInt();
                    scanner.close();
                    createPatientsInBulk(cabinet, chiffre);
                    break;

                default:
                    break;
            }

        } catch (Exception e) {
            System.err.println("Client exception: " + e);
            e.printStackTrace();
        }
    }
}