package vet.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import vet.common.Observation;
import vet.common.PatientRecord;

public class PatientRecordImpl extends UnicastRemoteObject implements PatientRecord {

    private String healthStatus;
    private ArrayList<Observation> observations;

    public PatientRecordImpl(
    ) throws RemoteException {
        super();
        observations = new ArrayList<Observation>();
    }

    @Override
    public void setHealthStatus(String healthStatus) throws RemoteException {
        this.healthStatus = healthStatus;
    }

    @Override
    public String getHealthStatus() throws RemoteException {
        return healthStatus;
    }

    @Override
    public void addObservation(Observation observation) throws RemoteException {
        observations.add(observation);
    }

    @Override
    public ArrayList<Observation> getObservations() throws RemoteException {
        return observations;
    }
}
