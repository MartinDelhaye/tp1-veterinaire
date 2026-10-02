package vet.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import vet.common.exceptions.PatientAlreadyExistsException;
import vet.common.exceptions.PatientNotFoundException;

public interface Cabinet extends Remote {
    List<Patient> getPatients() throws RemoteException;
    Patient getPatient(String name) throws RemoteException, PatientNotFoundException;
    Patient createPatient(String name, String ownerName, String race, Species species) throws RemoteException, PatientAlreadyExistsException;
}
