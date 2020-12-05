package com.antoine.assignment.haud.services;

import com.antoine.assignment.haud.builders.BuildCsvResponse;
import com.antoine.assignment.haud.entities.Customer;
import com.antoine.assignment.haud.entities.Sim;
import com.antoine.assignment.haud.exceptions.AssignmentException;
import com.antoine.assignment.haud.exceptions.NoDataFoundException;
import com.antoine.assignment.haud.models.CustomerModel;
import com.antoine.assignment.haud.models.SimModel;
import com.antoine.assignment.haud.repositories.CustomerRepository;
import com.antoine.assignment.haud.repositories.SimRepository;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final CustomerRepository customerRepository;
    private final SimRepository simRepository;
    private final ModelMapper mapper;
    private final EmailService emailService;

    @Autowired
    public AssignmentServiceImpl(CustomerRepository customerRepository, SimRepository simRepository, ModelMapper mapper, EmailService emailService) {
        this.customerRepository = customerRepository;
        this.simRepository = simRepository;
        this.mapper = mapper;
        this.emailService = emailService;
    }

    @Override
    public void registerCustomer(CustomerModel model) {
        Customer customerEntity = mapper.map(model, Customer.class);
        try {
            customerRepository.save(customerEntity);
        } catch (Exception ex) {
            throw new AssignmentException("Error while registering customer : " + model.toString(), "registerCustomer exception");
        }
    }

    @Override
    public void registerSim(SimModel model) {
        Sim simEntity = mapper.map(model, Sim.class);
        try {
            simRepository.save(simEntity);
        } catch (Exception ex) {
            throw new AssignmentException("Error while registering sim : " + model.toString(), "registerCustomer exception");
        }
    }

    @Override
    @Transactional
    public void linkSim(String iccId, String username) {
        Customer customer;
        Sim sim;
        try {
            sim = simRepository.findByICCID(iccId);
            log.info("retrieved for iccid [{}] the following sim : [{}]", iccId, sim.toString());
        } catch (Exception ex) {
            throw new NoDataFoundException("no sim found for iccId : " + iccId);
        }

        try {
            customer = customerRepository.findByUsername(username);
            log.info("retrieved for username [{}] the following customer : [{}]", username, customer.toString());
        } catch (Exception ex) {
            throw new NoDataFoundException("no customer found for username : " + username);
        }

        customer.getSims().add(sim);
        customerRepository.save(customer);

    }

    @Override
    @Transactional(readOnly = true)
    public List<SimModel> getSims(String username) {
        Customer customer;
        List<SimModel> modelList = new ArrayList<>();
        try {
            customer = customerRepository.findByUsername(username);
            log.info("retrieved for username [{}] the following customer : [{}]", username, customer.toString());
        } catch (Exception ex) {
            throw new NoDataFoundException("no customer found for username : " + username);
        }

        customer.getSims().forEach(sim -> {
            SimModel model = mapper.map(sim, SimModel.class);
            log.info("adding in the response list the following model : [{}] ", model.toString());
            modelList.add(model);
        });
        return modelList;
    }

    @Override
    public List<SimModel> getAllSims() {
        List<SimModel> modelList = new ArrayList<>();
        try {
            simRepository.findAll().forEach(sim -> {
                SimModel model = mapper.map(sim, SimModel.class);
                log.info("retrieved sim : [{}]", model.toString());
                modelList.add(model);
            });
        } catch (Exception ex) {
            throw new NoDataFoundException("no sim found");
        }
        return modelList;
    }

    @Scheduled(cron = "0 0 6 * * ?", zone = "Europe/Paris") //Will execute every day at 6am
    @SchedulerLock(name = "dailyExport", lockAtLeastFor = "1430m")
    public void dailyExport() throws IOException {
        LockAssert.assertLocked();

        List<Customer> customers = customerRepository.findcustomerHavingBirthday();
        log.info("Found following customers having their birthday today : [{}]", customers.toString());
        BuildCsvResponse csvBuilder = new BuildCsvResponse();

        csvBuilder.exportToCSV(customers);
    }


    @Scheduled(cron = "0 0 6 * * ?", zone = "Europe/Paris") //Will execute every day at 6am
    @SchedulerLock(name = "dailyBirthdayCheck", lockAtLeastFor = "1430m")
    public void dailyBirthdayCheck(){
        LockAssert.assertLocked();

        List<String> emails = customerRepository.findCustomersEmailHavingBirthdayNextWeek();
        if (emails.isEmpty()){
            return;
        }
        log.info("Found following customers' email addresses having their birthday next weeks : [{}]", emails.toString());

        emails.forEach(email-> {
            log.info("Sending birthday soon email to customer : [{}]", email);
            emailService.sendSimpleMessage(email);
        });

    }
}
