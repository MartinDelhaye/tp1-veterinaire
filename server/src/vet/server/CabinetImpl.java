package vet.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vet.common.Animal;
import vet.common.Cabinet;
import vet.common.PatientNotFoundException;
import vet.common.Species;

public class CabinetImpl extends UnicastRemoteObject implements Cabinet {

    private final Map<String, Animal> patients = new HashMap<String, Animal>();

    public CabinetImpl() throws RemoteException {
        super();
        Species chat = new Species("Chat",15);
        Animal link = new AnimalImpl("Link", "Martin", "Chat noir et blanc trop mignon", chat);
        patients.put(link.getName(), link);
    }
    
    public List<Animal> getPatients() throws RemoteException {
        return new ArrayList<Animal>(patients.values());
    }
    
    public Animal getPatient(String name) throws PatientNotFoundException, RemoteException {
        Animal patient = patients.get(name);
        if (patient == null) {
            throw new PatientNotFoundException(name);
        }
        return patient;
    }
    
}
