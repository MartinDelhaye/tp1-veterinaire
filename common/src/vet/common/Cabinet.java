package vet.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Cabinet extends Remote {
    List<Animal> getPatients() throws RemoteException;
    Animal getPatient(String name) throws RemoteException;
}
