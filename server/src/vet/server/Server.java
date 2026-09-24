package vet.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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
            CabinetImpl cabinet = new CabinetImpl();

            Registry registry = embedded
                    ? LocateRegistry.createRegistry(PORT)
                    : LocateRegistry.getRegistry(PORT);

            // rebind plutot que bind : on peut relancer le serveur sans
            // redemarrer le registre (voir TD 1, question 7).
            registry.rebind("cabinet", cabinet);

            System.out.println("Server ready (registre "
                    + (embedded ? "interne" : "externe") + ", port " + PORT + ")");
        } catch (Exception e) {
            System.err.println("Server exception: " + e);
            e.printStackTrace();
        }
    }
}