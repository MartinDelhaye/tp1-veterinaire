package vet.common.exceptions;

public class PatientAlreadyExistsException extends Exception {

    public PatientAlreadyExistsException(String name) {
        super("Un patient nommé '" + name + "' existe déjà");
    }
}