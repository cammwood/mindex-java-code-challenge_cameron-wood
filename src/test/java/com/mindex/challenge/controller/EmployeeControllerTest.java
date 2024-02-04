package com.mindex.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.exceptions.EmptyObjectException;
import com.mindex.challenge.model.ReportingStructure;
import com.mindex.challenge.service.impl.EmployeeServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private EmployeeServiceImpl employeeService;

    private String employeeBasePath = "/employee";

    private String employeeId = "/123";

    private String directReports = "/direct-reports";

    private String compensation = "/compensation";

    private Employee testEmployee;

    private Compensation testCompensation;

    @Before
    public void setup() {
        testEmployee = new Employee();
        testEmployee.setEmployeeId("123");
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        testCompensation = new Compensation();
        testCompensation.setEmployee(testEmployee);
        testCompensation.setSalary(new BigDecimal(12345));
        testCompensation.setEffectiveDate(LocalDate.now());
    }

    @Test
    public void createEmployee() throws Exception {
        when(employeeService.createEmployee(Mockito.any(Employee.class))).thenReturn(new Employee());
        MvcResult result = mockMvc.perform(post(employeeBasePath)
                        .content(mapper.writeValueAsString(testEmployee))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(201))
                .andReturn();
        assertTrue(mapper.readValue(result.getResponse().getContentAsString(), Employee.class) != null);
    }

    @Test
    public void createEmployee400() throws Exception {
        MvcResult result = mockMvc.perform(post(employeeBasePath)
                        .content(mapper.writeValueAsString(new Employee()))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is(400))
                .andReturn();
        assertTrue(result.getResponse().getContentAsString() != null);
    }

    @Test
    public void createEmployee500() throws Exception {
        when(employeeService.createEmployee(Mockito.any(Employee.class))).thenThrow(RuntimeException.class);
        MvcResult result = mockMvc.perform(post(employeeBasePath)
                        .content(mapper.writeValueAsString(testEmployee))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(500))
                .andReturn();
        assertTrue(result.getResponse().getContentAsString() != null);
    }

    @Test
    public void getEmployeeById() throws Exception {
        when(employeeService.readEmployee(Mockito.anyString())).thenReturn(new Employee());
        MvcResult result = mockMvc.perform(get(employeeBasePath + employeeId))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        assertTrue(mapper.readValue(result.getResponse().getContentAsString(), Employee.class) != null);
    }

    @Test
    public void getEmployee204() throws Exception {
        when(employeeService.readEmployee(Mockito.anyString())).thenThrow(EmptyObjectException.class);
        MvcResult result = mockMvc.perform(get(employeeBasePath + employeeId))
                .andDo(print())
                .andExpect(status().is(204))
                .andReturn();
        assertTrue(StringUtils.isBlank(result.getResponse().getContentAsString()));
    }

    @Test
    public void getEmployee500() throws Exception {
        when(employeeService.readEmployee(Mockito.anyString())).thenThrow(RuntimeException.class);
        MvcResult result = mockMvc.perform(get(employeeBasePath + employeeId))
                .andDo(print())
                .andExpect(status().is(500))
                .andReturn();
        assertTrue(result.getResponse().getContentAsString() != null);
    }

    @Test
    public void updateEmployee() throws Exception {
        when(employeeService.updateEmployee(Mockito.any(Employee.class))).thenReturn(new Employee());
        MvcResult result = mockMvc.perform(put(employeeBasePath + employeeId)
                        .content(mapper.writeValueAsString(testEmployee))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        assertTrue(mapper.readValue(result.getResponse().getContentAsString(), Employee.class) != null);
    }

    @Test
    public void updateEmployee400BadRequestBody() throws Exception {
        MvcResult result = mockMvc.perform(put(employeeBasePath + employeeId)
                        .content(mapper.writeValueAsString(new Employee()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(400))
                .andReturn();
        assertTrue(result.getResponse().getContentAsString() != null);
    }

    @Test
    public void updateEmployee400NoEmployee() throws Exception {
        when(employeeService.updateEmployee(Mockito.any(Employee.class))).thenThrow(IllegalArgumentException.class);
        MvcResult result = mockMvc.perform(put(employeeBasePath + employeeId)
                        .content(mapper.writeValueAsString(testEmployee))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(400))
                .andReturn();
        assertTrue(result.getResponse().getContentAsString() != null);
    }

    @Test
    public void updateEmployee500() throws Exception {
        when(employeeService.updateEmployee(Mockito.any(Employee.class))).thenThrow(RuntimeException.class);
        MvcResult result = mockMvc.perform(put(employeeBasePath + employeeId)
                        .content(mapper.writeValueAsString(testEmployee))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(500))
                .andReturn();
        assertTrue(result.getResponse().getContentAsString() != null);
    }

    @Test
    public void getEmployeeDirectReports() throws Exception {
        when(employeeService.readEmployeeReportingStructure(Mockito.anyString())).thenReturn(new ReportingStructure());
        MvcResult result = mockMvc.perform(get(employeeBasePath + employeeId + directReports))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        assertTrue(mapper.readValue(result.getResponse().getContentAsString(), ReportingStructure.class) != null);
    }

    @Test
    public void getEmployeeDirectReports204() throws Exception {
        when(employeeService.readEmployeeReportingStructure(Mockito.anyString())).thenThrow(EmptyObjectException.class);
        MvcResult result = mockMvc.perform(get(employeeBasePath + employeeId + directReports))
                .andDo(print())
                .andExpect(status().is(204))
                .andReturn();
        assertTrue(StringUtils.isBlank(result.getResponse().getContentAsString()));
    }

    @Test
    public void getEmployeeDirectReports500() throws Exception {
        when(employeeService.readEmployeeReportingStructure(Mockito.anyString())).thenThrow(RuntimeException.class);
        MvcResult result = mockMvc.perform(get(employeeBasePath + employeeId + directReports))
                .andDo(print())
                .andExpect(status().is(500))
                .andReturn();
        assertTrue(result.getResponse().getContentAsString() != null);
    }

    @Test
    public void createEmployeeCompensation() throws Exception {
        when(employeeService.createEmployeeCompensation(Mockito.any(Compensation.class))).thenReturn(new Compensation());
        MvcResult result = mockMvc.perform(post(employeeBasePath + employeeId + compensation)
                        .content(mapper.writeValueAsString(testCompensation))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(201))
                .andReturn();
        assertTrue(mapper.readValue(result.getResponse().getContentAsString(), Compensation.class) != null);
    }

    @Test
    public void createEmployeeCompensation400() throws Exception {
        MvcResult result = mockMvc.perform(post(employeeBasePath + employeeId + compensation)
                        .content(mapper.writeValueAsString(new Compensation()))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is(400))
                .andReturn();
        assertTrue(result.getResponse().getContentAsString() != null);
    }

    @Test
    public void createEmployeeCompensation400BadDate() throws Exception {
        MvcResult result = mockMvc.perform(post(employeeBasePath + employeeId + compensation)
                        .content("{\n" +
                                "    \"employee\" :{\n" +
                                "    \"employeeId\": \"16a596ae-edd3-4847-99fe-c4518e82c86f\",\n" +
                                "    \"firstName\": \"John\",\n" +
                                "    \"lastName\": \"Lennon\",\n" +
                                "    \"position\": \"Development Manager\",\n" +
                                "    \"department\": \"Engineering\",\n" +
                                "    \"directReports\": [\n" +
                                "        {\n" +
                                "            \"employeeId\": \"b7839309-3348-463b-a7e3-5de1c168beb3\",\n" +
                                "            \"firstName\": null,\n" +
                                "            \"lastName\": null,\n" +
                                "            \"position\": null,\n" +
                                "            \"department\": null,\n" +
                                "            \"directReports\": null\n" +
                                "        },\n" +
                                "        {\n" +
                                "            \"employeeId\": \"03aa1462-ffa9-4978-901b-7c001562cf6f\",\n" +
                                "            \"firstName\": null,\n" +
                                "            \"lastName\": null,\n" +
                                "            \"position\": null,\n" +
                                "            \"department\": null,\n" +
                                "            \"directReports\": null\n" +
                                "        }\n" +
                                "    ]\n" +
                                "    },\n" +
                                "    \"salary\" : 123456,\n" +
                                "    \"effectiveDate\" : \"1234\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is(400))
                .andReturn();
        assertTrue(result.getResponse().getContentAsString() != null);
    }

    @Test
    public void createEmployeeCompensation400NoEmployeeOrExistingCompensation() throws Exception {
        when(employeeService.createEmployeeCompensation(Mockito.any(Compensation.class))).thenThrow(IllegalArgumentException.class);
        MvcResult result = mockMvc.perform(post(employeeBasePath + employeeId + compensation)
                        .content(mapper.writeValueAsString(testCompensation))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is(400))
                .andReturn();
        assertTrue(result.getResponse().getContentAsString() != null);
    }


    @Test
    public void createEmployeeCompensation500() throws Exception {
        when(employeeService.createEmployeeCompensation(Mockito.any(Compensation.class))).thenThrow(RuntimeException.class);
        MvcResult result = mockMvc.perform(post(employeeBasePath + employeeId + compensation)
                        .content(mapper.writeValueAsString(testCompensation))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().is(500))
                .andReturn();
        assertTrue(result.getResponse().getContentAsString() != null);
    }

    @Test
    public void getEmployeeCompensation() throws Exception {
        when(employeeService.readEmployeeCompensation(Mockito.anyString())).thenReturn(new Compensation());
        MvcResult result = mockMvc.perform(get(employeeBasePath + employeeId + compensation))
                .andDo(print())
                .andExpect(status().is(200))
                .andReturn();
        assertTrue(mapper.readValue(result.getResponse().getContentAsString(), Compensation.class) != null);
    }

    @Test
    public void getEmployeeCompensation204() throws Exception {
        when(employeeService.readEmployeeCompensation(Mockito.anyString())).thenThrow(EmptyObjectException.class);
        MvcResult result = mockMvc.perform(get(employeeBasePath + employeeId + compensation))
                .andDo(print())
                .andExpect(status().is(204))
                .andReturn();
        assertTrue(StringUtils.isBlank(result.getResponse().getContentAsString()));
    }

    @Test
    public void getEmployeeCompensation500() throws Exception {
        when(employeeService.readEmployeeCompensation(Mockito.anyString())).thenThrow(RuntimeException.class);
        MvcResult result = mockMvc.perform(get(employeeBasePath + employeeId + compensation))
                .andDo(print())
                .andExpect(status().is(500))
                .andReturn();
        assertTrue(result.getResponse().getContentAsString() != null);
    }
}