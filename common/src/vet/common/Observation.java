package vet.common;

import java.io.Serializable;
import java.util.Date;

public class Observation implements Serializable {
    private Date date;
    private String observation;
    
    public Observation(String observation, Date date) {
        this.date = date;
        this.observation = observation;
    }
    public Observation(String observation) {
        this.date = new Date(System.currentTimeMillis());
        this.observation = observation;
    }

    public Date getDate() {
        return date;
    }

    public String getObservation() {
        return observation;
    }
    
}
