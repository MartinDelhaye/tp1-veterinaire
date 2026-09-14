package hello.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import hello.common.Hello;

/**
 * Le SERVANT : l'objet reel, qui ne quitte jamais la JVM du serveur.
 * Il vit dans le projet server ; le client ne connait pas cette classe.
 */
public class HelloImpl extends UnicastRemoteObject implements Hello {

    private static final long serialVersionUID = 1L;

    public HelloImpl() throws RemoteException {
        super();   // c'est ici que l'objet est exporte
    }

    @Override
    public String sayHello() throws RemoteException {
        return "Hello, world!";
    }

    @Override
    public void printHello() throws RemoteException {
        System.out.println("The server prints : Hello, world!");
    }
}
