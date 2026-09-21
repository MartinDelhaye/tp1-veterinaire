package vet.common;

import java.io.Serializable;

public class Species implements Serializable {

    private String name;
    private int averageLife;

    public Species(String name, int averageLife) {
        this.name = name;
        this.averageLife = averageLife;
    }

    public String getName() {
        return name;
    }

    public int getAverageLife() {
        return averageLife;
    }

    public void setAverageLife(int newAverageLife) {
        this.averageLife = newAverageLife;
    }

    public String toString() {
        return  "Species("+name+", " + averageLife + " ans)";
    }

}
