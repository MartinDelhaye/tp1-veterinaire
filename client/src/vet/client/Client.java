package vet.client;

import java.lang.reflect.Proxy;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import vet.common.Animal;

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
            Animal stub = (Animal) registry.lookup("Link");

            System.out.println("classe du stub : " + stub.getClass().getName());
            System.out.println("proxy dynamique ? " + Proxy.isProxyClass(stub.getClass()));

            System.out.println("nom     : " + stub.getName());
            System.out.println("maitre  : " + stub.getOwnerName());
            System.out.println("race    : " + stub.getRace());
            System.out.println("espece  : " + stub.getSpecies());
        } catch (Exception e) {
            System.err.println("Client exception: " + e);
            e.printStackTrace();
        }
    }
}