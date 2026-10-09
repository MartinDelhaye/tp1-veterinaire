package vet.client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import vet.common.Cabinet;

public class Client {

    public static void main(String[] args) {
        String host = (args.length > 0) ? args[0] : null;
        try {
            Registry registry = LocateRegistry.getRegistry(host, 1099);
            Cabinet cabinet = (Cabinet) registry.lookup("cabinet");

            ClientService service = new ClientService(cabinet);
            ClientUI ui = new ClientUI(service);
            ui.lancer();

        } catch (Exception e) {
            System.err.println("Impossible de se connecter au serveur : " + e.getMessage());
        }
    }
}