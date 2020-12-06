package com.antoine.assignment.haud.services;

import com.antoine.assignment.haud.builders.CsvBuilder;
import com.antoine.assignment.haud.entities.Customer;
import com.antoine.assignment.haud.entities.Sim;
import com.antoine.assignment.haud.exceptions.AssignmentException;
import com.antoine.assignment.haud.exceptions.NoDataFoundException;
import com.antoine.assignment.haud.models.CustomerModel;
import com.antoine.assignment.haud.models.SimModel;
import com.antoine.assignment.haud.repositories.CustomerRepository;
import com.antoine.assignment.haud.repositories.SimRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("/test.properties")
public class AssignmentServiceTest {

    @Autowired
    private AssignmentServiceImpl assignmentService;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private SimRepository simRepository;

    @MockBean
    private EmailServiceImpl emailService;

    @MockBean
    private CsvBuilder csvBuilder;

    @Test
    public void registerCustomerTest() {

        CustomerModel model = new CustomerModel();
        model.setAddress("testemail@email.com");
        model.setBirthDate(LocalDate.of(1990, 10, 20));
        model.setName("Antoine");
        model.setSurname("Breuil");
        model.setUsername("antbre");

        assignmentService.registerCustomer(model);
        Mockito.verify(customerRepository, Mockito.times(1)).save(any(Customer.class));
    }

    @Test(expected = AssignmentException.class)
    public void registerCustomerTestException() {

        CustomerModel model = new CustomerModel();
        model.setAddress("testemail@email.com");
        model.setBirthDate(LocalDate.of(1990, 10, 20));
        model.setName("Antoine");
        model.setSurname("Breuil");
        model.setUsername("antbre");

        Mockito.when(customerRepository.save(any())).thenThrow(new AssignmentException("test", "test"));
        assignmentService.registerCustomer(model);
    }

    @Test
    public void registerSimTest() {

        SimModel simModel = new SimModel();
        simModel.setICCID("iccid");
        simModel.setIMSI("imsi");
        simModel.setKI("ki");
        simModel.setPIN("pin");
        simModel.setPUK("puk");
        assignmentService.registerSim(simModel);
        Mockito.verify(simRepository, Mockito.times(1)).save(any(Sim.class));
    }

    @Test(expected = AssignmentException.class)
    public void registerSimTestException() {

        SimModel simModel = new SimModel();
        simModel.setICCID("iccid");
        simModel.setIMSI("imsi");
        simModel.setKI("ki");
        simModel.setPIN("pin");
        simModel.setPUK("puk");
        Mockito.when(simRepository.save(any())).thenThrow(new AssignmentException("test", "test"));

        assignmentService.registerSim(simModel);
    }

    @Test
    public void linkSimTest() {
        String iccId = "iccId";
        String username = "antbre";

        Sim foundSim = new Sim();
        foundSim.setICCID("iccid");
        foundSim.setIMSI("imsi");
        foundSim.setKI("ki");
        foundSim.setPIN("pin");
        foundSim.setPUK("puk");

        Customer foundCustomer = new Customer();
        foundCustomer.setAddress("testemail@email.com");
        foundCustomer.setBirthDate(LocalDate.of(1990, 10, 20));
        foundCustomer.setName("Antoine");
        foundCustomer.setSurname("Breuil");
        foundCustomer.setUsername("antbre");

        Mockito.when(customerRepository.findByUsername(eq(username))).thenReturn(foundCustomer);
        Mockito.when(simRepository.findByICCID(eq(iccId))).thenReturn(foundSim);

        assignmentService.linkSim(iccId, username);
        foundCustomer.getSims().add(foundSim); //So we make sure the sim card got linked to the customer thanks to the mockito.verify used with eq(foundCustomer)

        Mockito.verify(customerRepository, Mockito.times(1)).save(eq(foundCustomer));

    }

    @Test(expected = NoDataFoundException.class)
    public void linkSimTestExceptionSim() {
        String iccId = "iccId";
        String username = "antbre";

        Customer foundCustomer = new Customer();
        foundCustomer.setAddress("testemail@email.com");
        foundCustomer.setBirthDate(LocalDate.of(1990, 10, 20));
        foundCustomer.setName("Antoine");
        foundCustomer.setSurname("Breuil");
        foundCustomer.setUsername("antbre");

        Mockito.when(customerRepository.findByUsername(eq(username))).thenReturn(foundCustomer);
        Mockito.when(simRepository.findByICCID(eq(iccId))).thenThrow(new NoDataFoundException("test"));

        assignmentService.linkSim(iccId, username);

    }

    @Test(expected = NoDataFoundException.class)
    public void linkSimTestExceptionCustomer() {
        String iccId = "iccId";
        String username = "antbre";

        Sim foundSim = new Sim();
        foundSim.setICCID("iccid");
        foundSim.setIMSI("imsi");
        foundSim.setKI("ki");
        foundSim.setPIN("pin");
        foundSim.setPUK("puk");

        Mockito.when(customerRepository.findByUsername(eq(username))).thenThrow(new NoDataFoundException("test"));
        Mockito.when(simRepository.findByICCID(eq(iccId))).thenReturn(foundSim);

        assignmentService.linkSim(iccId, username);
    }

    @Test
    public void getSimsTest() {
        String username = "antbre";

        Sim sim1 = new Sim();
        sim1.setICCID("iccid");
        sim1.setIMSI("imsi");
        sim1.setKI("ki");
        sim1.setPIN("pin");
        sim1.setPUK("puk");

        Sim sim2 = new Sim();
        sim2.setICCID("iccid2");
        sim2.setIMSI("imsi2");
        sim2.setKI("ki2");
        sim2.setPIN("pin2");
        sim2.setPUK("puk2");

        SimModel simModel = new SimModel();
        simModel.setICCID("iccid");
        simModel.setIMSI("imsi");
        simModel.setKI("ki");
        simModel.setPIN("pin");
        simModel.setPUK("puk");

        SimModel simModel2 = new SimModel();
        simModel2.setICCID("iccid2");
        simModel2.setIMSI("imsi2");
        simModel2.setKI("ki2");
        simModel2.setPIN("pin2");
        simModel2.setPUK("puk2");

        List<SimModel> expected = List.of(simModel, simModel2);

        Customer customer = new Customer();
        customer.setUsername("antbre");
        customer.getSims().add(sim1);
        customer.getSims().add(sim2);

        Mockito.when(customerRepository.findByUsername(eq(username))).thenReturn(customer);

        List<SimModel> response = assignmentService.getSims(username);

        Assertions.assertThat(response).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }

    @Test(expected = NoDataFoundException.class)
    public void getSimsTestException() {
        String username = "antbre";

        Mockito.when(customerRepository.findByUsername(eq(username))).thenThrow(new NoDataFoundException("test"));
        assignmentService.getSims(username);

    }

    @Test
    public void getAllSimsTest() {

        Sim sim1 = new Sim();
        sim1.setICCID("iccid");
        sim1.setIMSI("imsi");
        sim1.setKI("ki");
        sim1.setPIN("pin");
        sim1.setPUK("puk");

        Sim sim2 = new Sim();
        sim2.setICCID("iccid2");
        sim2.setIMSI("imsi2");
        sim2.setKI("ki2");
        sim2.setPIN("pin2");
        sim2.setPUK("puk2");

        SimModel simModel = new SimModel();
        simModel.setICCID("iccid");
        simModel.setIMSI("imsi");
        simModel.setKI("ki");
        simModel.setPIN("pin");
        simModel.setPUK("puk");

        SimModel simModel2 = new SimModel();
        simModel2.setICCID("iccid2");
        simModel2.setIMSI("imsi2");
        simModel2.setKI("ki2");
        simModel2.setPIN("pin2");
        simModel2.setPUK("puk2");

        List<SimModel> expected = List.of(simModel, simModel2);

        Mockito.when(simRepository.findAll()).thenReturn(List.of(sim1, sim2));
        List<SimModel> response = assignmentService.getAllSims();

        Assertions.assertThat(response).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);

    }

    @Test(expected = NoDataFoundException.class)
    public void getAllSimsTestNoSimException() {

        Mockito.when(simRepository.findAll()).thenThrow(new NoDataFoundException("test"));
        assignmentService.getAllSims();
    }

    @Test
    public void dailyExportTest() throws IOException {

        Customer customer1 = new Customer();
        customer1.setAddress("testemail@email.com");
        customer1.setBirthDate(LocalDate.of(1990, 10, 20));
        customer1.setName("Antoine");
        customer1.setSurname("Breuil");
        customer1.setUsername("antbre");

        Customer customer2 = new Customer();
        customer2.setAddress("testemail2@email.com");
        customer2.setBirthDate(LocalDate.of(1990, 10, 20));
        customer2.setName("Antoine2");
        customer2.setSurname("Breuil2");
        customer2.setUsername("antbre2");

        Sim sim1 = new Sim();
        sim1.setICCID("iccid");
        sim1.setIMSI("imsi");
        sim1.setKI("ki");
        sim1.setPIN("pin");
        sim1.setPUK("puk");

        Sim sim2 = new Sim();
        sim2.setICCID("iccid2");
        sim2.setIMSI("imsi2");
        sim2.setKI("ki2");
        sim2.setPIN("pin2");
        sim2.setPUK("puk2");

        customer1.getSims().add(sim1);
        customer1.getSims().add(sim2);

        List<Customer> customerList = List.of(customer1, customer2);

        Mockito.when(customerRepository.findcustomerHavingBirthday()).thenReturn(customerList);

        assignmentService.dailyExport();

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = dateFormatter.format(new Date());
        File file = new File("src/main/java/com/antoine/assignment/haud/exports/" + "users_" + currentDateTime + ".csv");

        assertTrue(file.exists());

    }

    @Test
    public void dailyBirthdayCheckTest(){
        List<String> emails = List.of("test1@email.com", "test2@email.com");

        Mockito.when(customerRepository.findCustomersEmailHavingBirthdayNextWeek()).thenReturn(emails);

        assignmentService.dailyBirthdayCheck();
        Mockito.verify(emailService, Mockito.times(2)).sendSimpleMessage(anyString());

    }
}
