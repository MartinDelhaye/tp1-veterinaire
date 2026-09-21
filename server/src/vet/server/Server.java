package vet.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import vet.common.Animal;

/**
 * Le LANCEUR du serveur : il cree le servant et le publie.
 *
 * java vet.server.Server -> registre externe (rmiregistry)
 * java vet.server.Server --embedded -> registre cree dans cette JVM
 */
public class Server {

    public static final int PORT = 1099;

    public static void main(String[] args) {
        boolean embedded = args.length > 0 && "--embedded".equals(args[0]);
        try {
            Animal link = new AnimalImpl("Link", "Martin", "Chat noir et blanc trop mignon", "Chat");

            Registry registry = embedded
                    ? LocateRegistry.createRegistry(PORT)
                    : LocateRegistry.getRegistry(PORT);

            // rebind plutot que bind : on peut relancer le serveur sans
            // redemarrer le registre (voir TD 1, question 7).
            registry.rebind("Link", link);

            System.out.println("Server ready (registre "
                    + (embedded ? "interne" : "externe") + ", port " + PORT + ")");
        } catch (Exception e) {
            System.err.println("Server exception: " + e);
            e.printStackTrace();
        }
    }
}