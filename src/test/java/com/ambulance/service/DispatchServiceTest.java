package com.ambulance.service;

import com.ambulance.enums.AmbulanceState;
import com.ambulance.enums.AmbulanceType;
import com.ambulance.enums.Priority;
import com.ambulance.exception.ResourceUnavailableException;
import com.ambulance.model.Ambulance;
import com.ambulance.model.Driver;
import com.ambulance.model.EmergencyRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DispatchServiceTest {
    private DispatchService dispatchService;

    @BeforeEach
    void setUp() {
        dispatchService = new DispatchService();
        Driver driver = new Driver("D01", "John Doe", "555-0199");
        dispatchService.registerAmbulance(new Ambulance("AMB-01", AmbulanceType.ICU, driver));
    }

    @Test
    void testSuccessfulDispatch() {
        EmergencyRequest request = new EmergencyRequest("P01", "Cardiac", "123 Main St", "City Hospital", Priority.CRITICAL, 4.5);
        assertDoesNotThrow(() -> dispatchService.processEmergency(request));
        assertEquals(1, dispatchService.getEmergencyHistory().size());
    }

    @Test
    void testResourceUnavailableQueue() {
        EmergencyRequest req1 = new EmergencyRequest("P01", "Trauma", "Street A", "Hospital A", Priority.CRITICAL, 2.0);
        EmergencyRequest req2 = new EmergencyRequest("P02", "Stroke", "Street B", "Hospital B", Priority.HIGH, 3.0);

        dispatchService.processEmergency(req1);
        assertThrows(ResourceUnavailableException.class, () -> dispatchService.processEmergency(req2));
        assertEquals(1, dispatchService.getWaitingQueueSize());
    }

    @Test
    void testETAcalculation() {
        double eta = dispatchService.calculateETA(10.0, 60.0);
        assertEquals(10.0, eta, 0.01);
    }
}
