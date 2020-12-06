package com.antoine.assignment.haud.controllers;

import com.antoine.assignment.haud.HaudApplication;
import com.antoine.assignment.haud.builders.AssignmentBuilder;
import com.antoine.assignment.haud.dtos.CustomerDTO;
import com.antoine.assignment.haud.dtos.SimDTO;
import com.antoine.assignment.haud.models.CustomerModel;
import com.antoine.assignment.haud.models.SimModel;
import com.antoine.assignment.haud.services.AssignmentService;
import com.antoine.assignment.haud.services.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = HaudApplication.class)
@TestPropertySource("/test.properties")
@EnableAutoConfiguration(exclude = {
        JmxAutoConfiguration.class
})
public class AssignmentControllerTest {

    @MockBean
    private AssignmentService assignmentService;

    @MockBean
    private EmailService emailService;

    private AssignmentController controller;

    private MockMvc mockMvc;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AssignmentBuilder builder;

    @Before
    public void setUp(){
        controller = new AssignmentController(assignmentService, modelMapper, builder);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new AssignmentControllerAdvice())
                .build();
    }

    @Test
    public void createCustomerTest() throws Exception {

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setAddress("testemail@email.com");
        customerDTO.setBirthDate("1990-10-20");
        customerDTO.setName("Antoine");
        customerDTO.setSurname("Breuil");
        customerDTO.setUsername("antbre");

        Mockito.doNothing().when(assignmentService).registerCustomer(any(CustomerModel.class));

        this.mockMvc.perform(post("/createCustomer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(customerDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void createCustomerTestBR() throws Exception {

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setAddress("testemail@email.com");
        customerDTO.setBirthDate("1990-10-20");
        customerDTO.setName("Antoine");
        customerDTO.setSurname("Breuil");

        Mockito.doNothing().when(assignmentService).registerCustomer(any(CustomerModel.class));

        this.mockMvc.perform(post("/createCustomer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(customerDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void createSimTest() throws Exception {
        SimDTO simDTO = new SimDTO();
        simDTO.setICCID("iccid");
        simDTO.setIMSI("imsi");
        simDTO.setKI("ki");
        simDTO.setPIN("pin");
        simDTO.setPUK("puk");

        Mockito.doNothing().when(assignmentService).registerSim(any(SimModel.class));

        this.mockMvc.perform(post("/createSim")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(simDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void createSimTestBR() throws Exception {
        SimDTO simDTO = new SimDTO();
        simDTO.setIMSI("imsi");
        simDTO.setKI("ki");
        simDTO.setPIN("pin");
        simDTO.setPUK("puk");

        Mockito.doNothing().when(assignmentService).registerSim(any(SimModel.class));

        this.mockMvc.perform(post("/createSim")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(simDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void linkSimTest() throws Exception {
        String iccId = "iccid";
        String username ="antbre";

        Mockito.doNothing().when(assignmentService).linkSim(anyString(), anyString());

        this.mockMvc.perform(post("/linkSim")
                .contentType(MediaType.APPLICATION_JSON)
                .param("username", username)
                .param("iccId", iccId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getCustomerSimsTest() throws Exception {
        String username ="antbre";
        SimDTO simDTO = new SimDTO();
        simDTO.setICCID("iccid");
        simDTO.setIMSI("imsi");
        simDTO.setKI("ki");
        simDTO.setPIN("pin");
        simDTO.setPUK("puk");

        SimDTO simDTO2 = new SimDTO();
        simDTO2.setICCID("iccid2");
        simDTO2.setIMSI("imsi2");
        simDTO2.setKI("ki2");
        simDTO2.setPIN("pin2");
        simDTO2.setPUK("puk2");

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
        List<SimModel> modelList = List.of(simModel, simModel2);
        List<SimDTO> list = List.of(simDTO, simDTO2);
        Mockito.when(assignmentService.getSims(anyString())).thenReturn(modelList);

        MvcResult response = this.mockMvc.perform(get("/getCustomerSims")
                .contentType(MediaType.APPLICATION_JSON)
                .param("username", username)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertThat(response.getResponse().getContentAsString()).isEqualTo(toJson(list));
    }

    @Test
    public void getAllSimsTest() throws Exception {
        String username ="antbre";
        SimDTO simDTO = new SimDTO();
        simDTO.setICCID("iccid");
        simDTO.setIMSI("imsi");
        simDTO.setKI("ki");
        simDTO.setPIN("pin");
        simDTO.setPUK("puk");

        SimDTO simDTO2 = new SimDTO();
        simDTO2.setICCID("iccid2");
        simDTO2.setIMSI("imsi2");
        simDTO2.setKI("ki2");
        simDTO2.setPIN("pin2");
        simDTO2.setPUK("puk2");

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
        List<SimModel> modelList = List.of(simModel, simModel2);
        List<SimDTO> list = List.of(simDTO, simDTO2);
        Mockito.when(assignmentService.getAllSims()).thenReturn(modelList);

        MvcResult response = this.mockMvc.perform(get("/getAllSims")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertThat(response.getResponse().getContentAsString()).isEqualTo(toJson(list));
    }


    public String toJson(Object obj) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer();
        return ow.writeValueAsString(obj);
    }
}
