package vet.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import vet.common.Animal;

/**
 * Le SERVANT : l'objet reel, qui ne quitte jamais la JVM du serveur.
 * Il vit dans le projet server ; le client ne connait pas cette classe.
 */
public class AnimalImpl extends UnicastRemoteObject implements Animal {

    private final String name;
    private final String ownerName;
    private final String race;
    private final String species;

    public AnimalImpl(String name, String ownerName, String race, String species)
            throws RemoteException {
        super(); // c'est ici que l'objet est exporte
        this.name = name;
        this.ownerName = ownerName;
        this.race = race;
        this.species = species;
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public String getOwnerName() throws RemoteException {
        return ownerName;
    }

    @Override
    public String getRace() throws RemoteException {
        return race;
    }

    @Override
    public String getSpecies() throws RemoteException {
        return species;
    }
}