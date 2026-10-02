package vet.common.exceptions;


public class PatientNotFoundException extends Exception {

    public PatientNotFoundException(String name) {
        super("Patient '" + name + "' introuvable");
    }
}
