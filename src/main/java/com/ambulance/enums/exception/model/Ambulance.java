package com.ambulance.model;

import com.ambulance.enums.AmbulanceState;
import com.ambulance.enums.AmbulanceType;

public class Ambulance {
    private String ambulanceId;
    private AmbulanceType type;
    private AmbulanceState state;
    private Driver driver;

    public Ambulance(String ambulanceId, AmbulanceType type, Driver driver) {
        this.ambulanceId = ambulanceId;
        this.type = type;
        this.state = AmbulanceState.AVAILABLE;
        this.driver = driver;
    }

    public String getAmbulanceId() { return ambulanceId; }
    public AmbulanceType getType() { return type; }
    public AmbulanceState getState() { return state; }
    public void setState(AmbulanceState state) { this.state = state; }
    public Driver getDriver() { return driver; }
}
