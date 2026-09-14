package hello.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * L'interface distante : le CONTRAT partage.
 *
 * C'est le seul type que le client et le serveur doivent connaitre tous
 * les deux. Il vit donc dans le projet common, ajoute au classpath des
 * deux autres projets.
 */
public interface Hello extends Remote {

    String sayHello() throws RemoteException;

    void printHello() throws RemoteException;
}
