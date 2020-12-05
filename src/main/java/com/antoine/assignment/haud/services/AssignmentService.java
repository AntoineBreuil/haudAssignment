package com.antoine.assignment.haud.services;

import com.antoine.assignment.haud.models.CustomerModel;
import com.antoine.assignment.haud.models.SimModel;

import java.util.List;

public interface AssignmentService {
    void registerCustomer(CustomerModel model);
    void registerSim(SimModel model);
    void linkSim(String iccId, String username);
    List<SimModel> getSims(String username);
    List<SimModel> getAllSims();
}
