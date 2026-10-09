package vet.client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import vet.common.AlerteSeuil;
import vet.common.CabinetObserver;

public class CabinetObserverImpl extends UnicastRemoteObject implements CabinetObserver {

    public CabinetObserverImpl() throws RemoteException {
        super();
    }

    @Override
    public void notifierSeuil(AlerteSeuil alerte) throws RemoteException {
        System.out.println(Color.alert("[ALERTE] " + alerte));
    }
}