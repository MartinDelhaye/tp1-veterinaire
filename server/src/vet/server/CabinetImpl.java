package vet.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vet.common.Patient;
import vet.common.Cabinet;
import vet.common.exceptions.PatientNotFoundException;
import vet.common.Species;
import vet.common.exceptions.PatientAlreadyExistsException;

public class CabinetImpl extends UnicastRemoteObject implements Cabinet {

    private final Map<String, Patient> patients = new HashMap<String, Patient>();

    public CabinetImpl() throws RemoteException {
        super();
        Species chat = new Species("Chat", 15);
        Patient link = new PatientImpl("Link", "Martin", "Chat noir et blanc trop mignon", chat);
        patients.put(link.getName(), link);
    }

    public List<Patient> getPatients() throws RemoteException {
        return new ArrayList<Patient>(patients.values());
    }

    public Patient getPatient(String name) throws PatientNotFoundException, RemoteException {
        Patient patient = patients.get(name);
        if (patient == null) {
            throw new PatientNotFoundException(name);
        }
        return patient;
    }

    public Patient createPatient(String name, String ownerName, String race, Species species)
            throws RemoteException, PatientAlreadyExistsException {
        if (patients.containsKey(name)) {
            throw new PatientAlreadyExistsException(name);
        }
        Patient patient = new PatientImpl(name, ownerName, race, species);
        patients.put(name, patient);
        return patient;
    }

}
