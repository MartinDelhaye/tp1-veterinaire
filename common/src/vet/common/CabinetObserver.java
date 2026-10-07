package vet.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CabinetObserver extends Remote {
    void notifierSeuil(AlerteSeuil alerte) throws RemoteException;
}