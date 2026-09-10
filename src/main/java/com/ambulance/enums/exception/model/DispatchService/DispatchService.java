package com.ambulance.service;

import com.ambulance.enums.AmbulanceState;
import com.ambulance.exception.InvalidRequestException;
import com.ambulance.exception.ResourceUnavailableException;
import com.ambulance.model.Ambulance;
import com.ambulance.model.EmergencyRequest;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

public class DispatchService {
    private final List<Ambulance> ambulances = new ArrayList<>();
    private final PriorityBlockingQueue<EmergencyRequest> waitingQueue = new PriorityBlockingQueue<>();
    private final List<EmergencyRequest> emergencyHistory = new ArrayList<>();

    public void registerAmbulance(Ambulance ambulance) {
        ambulances.add(ambulance);
    }

    public synchronized void processEmergency(EmergencyRequest request) {
        if (request == null || request.getEstimatedDistance() < 0) {
            throw new InvalidRequestException("Invalid emergency request parameters.");
        }

        Optional<Ambulance> availableAmbulance = ambulances.stream()
                .filter(a -> a.getState() == AmbulanceState.AVAILABLE && a.getType() == a.getType())
                .min(Comparator.comparingDouble(a -> request.getEstimatedDistance()));

        if (availableAmbulance.isPresent()) {
            Ambulance ambulance = availableAmbulance.get();
            ambulance.setState(AmbulanceState.DISPATCHED);
            request.setAssignedAmbulanceId(ambulance.getAmbulanceId());
            emergencyHistory.add(request);
        } else {
            waitingQueue.add(request);
            throw new ResourceUnavailableException("No ambulances available. Added to waiting queue.");
        }
    }

    public synchronized void updateAmbulanceState(String ambulanceId, AmbulanceState newState) {
        Ambulance ambulance = ambulances.stream()
                .filter(a -> a.getAmbulanceId().equals(ambulanceId))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException("Ambulance not found: " + ambulanceId));

        ambulance.setState(newState);

        if (newState == AmbulanceState.AVAILABLE && !waitingQueue.isEmpty()) {
            EmergencyRequest nextRequest = waitingQueue.poll();
            processEmergency(nextRequest);
        }
    }

    public double calculateETA(double distanceKm, double averageSpeedKmh) {
        if (averageSpeedKmh <= 0) throw new InvalidRequestException("Speed must be greater than zero.");
        return (distanceKm / averageSpeedKmh) * 60; // Returns ETA in minutes
    }

    public List<EmergencyRequest> getEmergencyHistory() { return emergencyHistory; }
    public int getWaitingQueueSize() { return waitingQueue.size(); }
}
