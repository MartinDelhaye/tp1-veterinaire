package vet.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface PatientRecord extends Remote {
    void setHealthStatus(String healthStatus) throws RemoteException;
    String getHealthStatus() throws RemoteException;
    void addObservation(Observation observation) throws RemoteException;
    List<Observation> getObservations() throws RemoteException;
}
