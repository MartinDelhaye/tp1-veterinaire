package vet.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Animal extends Remote {

    String getName() throws RemoteException;
    String getOwnerName() throws RemoteException;
    String getRace() throws RemoteException;
    Species getSpecies() throws RemoteException;
    PatientRecord getRecord() throws RemoteException;
}
