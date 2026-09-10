package com.ambulance.model;

import com.ambulance.enums.Priority;
import java.time.LocalDateTime;

public class EmergencyRequest implements Comparable<EmergencyRequest> {
    private String patientId;
    private String emergencyType;
    private String pickupLocation;
    private String destinationHospital;
    private Priority priority;
    private double estimatedDistance;
    private LocalDateTime timestamp;
    private String assignedAmbulanceId;

    public EmergencyRequest(String patientId, String emergencyType, String pickupLocation, 
                            String destinationHospital, Priority priority, double estimatedDistance) {
        this.patientId = patientId;
        this.emergencyType = emergencyType;
        this.pickupLocation = pickupLocation;
        this.destinationHospital = destinationHospital;
        this.priority = priority;
        this.estimatedDistance = estimatedDistance;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public int compareTo(EmergencyRequest other) {
        int priorityCompare = Integer.compare(this.priority.getLevel(), other.priority.getLevel());
        if (priorityCompare != 0) {
            return priorityCompare;
        }
        return this.timestamp.compareTo(other.timestamp);
    }

    public Priority getPriority() { return priority; }
    public double getEstimatedDistance() { return estimatedDistance; }
    public String getAssignedAmbulanceId() { return assignedAmbulanceId; }
    public void setAssignedAmbulanceId(String assignedAmbulanceId) { this.assignedAmbulanceId = assignedAmbulanceId; }
}
