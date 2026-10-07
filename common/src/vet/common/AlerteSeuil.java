package vet.common;

import java.io.Serializable;

public class AlerteSeuil implements Serializable {

    public enum Sens {
        HAUSSE,
        BAISSE
    }

    private final int seuil;
    private final Sens sens;

    public AlerteSeuil(int seuil, Sens sens) {
        this.seuil = seuil;
        this.sens = sens;
    }

    public int getSeuil() {
        return seuil;
    }

    public Sens getSens() {
        return sens;
    }

    @Override
    public String toString() {
        return "Seuil " + seuil + " franchi (" + sens + ")";
    }
}