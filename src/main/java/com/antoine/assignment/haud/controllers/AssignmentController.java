package com.antoine.assignment.haud.controllers;

import com.antoine.assignment.haud.builders.AssignmentBuilder;
import com.antoine.assignment.haud.dtos.CustomerDTO;
import com.antoine.assignment.haud.dtos.SimDTO;
import com.antoine.assignment.haud.models.CustomerModel;
import com.antoine.assignment.haud.models.SimModel;
import com.antoine.assignment.haud.services.AssignmentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class AssignmentController {

    private final AssignmentService service;
    private final ModelMapper mapper;
    private final AssignmentBuilder builder;

    @Autowired
    public AssignmentController(AssignmentService service, ModelMapper modelMapper, AssignmentBuilder builder) {
        this.service = service;
        this.mapper = modelMapper;
        this.builder = builder;
    }
//TODO Add springfox

    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/createCustomer", produces = MediaType.APPLICATION_JSON_VALUE)
    public void createCustomer(@RequestBody @Valid CustomerDTO customerDTO){
        log.info("New incoming request on POST /createCustomer with parameters : [{}]", customerDTO.toString());
        CustomerModel customer = mapper.map(customerDTO, CustomerModel.class);
        service.registerCustomer(customer);
        log.info("Customer [{}] created successfully ", customer.getUsername());
    }

    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/createSim", produces = MediaType.APPLICATION_JSON_VALUE)
    public void createSim(@RequestBody SimDTO simDTO){
        log.info("New incoming request on POST /createSim with parameters : [{}]", simDTO.toString());
        SimModel sim = mapper.map(simDTO, SimModel.class);
        service.registerSim(sim);
        log.info("Sim [{}] created successfully ", sim.getICCID());
    }

    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/linkSim", produces = MediaType.APPLICATION_JSON_VALUE)
    public void linkSimToCustomer(@RequestParam String username,
                                  @RequestParam String iccId){
        log.info("New incoming request on POST /linkSim with parameters : [{}], [{}]", username, iccId);
        service.linkSim(username, iccId);
        log.info("Sim [{}] linked to customer [{}] successfully ", iccId, username);
    }

    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/getCustomerSims", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SimDTO> getOwnSims(@RequestParam String username){
        log.info("New incoming request on GET /getSims with parameters : [{}]", username);
        return builder.buildSimList(service.getSims(username));
    }

    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/getAllSims", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SimDTO> getAllSims(){
        log.info("New incoming request on GET /getAllSims");
        return builder.buildSimList(service.getAllSims());
    }

}
