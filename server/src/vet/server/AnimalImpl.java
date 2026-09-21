package vet.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import vet.common.Animal;

/**
 * Le SERVANT : l'objet reel, qui ne quitte jamais la JVM du serveur.
 * Il vit dans le projet server ; le client ne connait pas cette classe.
 */
public class AnimalImpl extends UnicastRemoteObject implements Animal {

    public AnimalImpl() throws RemoteException {
        super();    // c'est ici que l'objet est exporte
    }

    @Override
    public String getName() throws RemoteException {
        return "Animal";
    }

    @Override
    public String getOwnerName() throws RemoteException {
        return "Owner";
    }

    @Override
    public String getSpecies() throws RemoteException {
        return "Species";
    }

    @Override
    public String getRace() throws RemoteException {
        return "Race";
    }
}
