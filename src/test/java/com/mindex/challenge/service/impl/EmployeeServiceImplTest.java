package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.exceptions.EmptyObjectException;
import com.mindex.challenge.model.ReportingStructure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class EmployeeServiceImplTest {

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private CompensationRepository compensationRepository;

    private Employee testEmployee;

    private Employee testEmployee2;

    private Employee testEmployee3;

    private Employee testEmployee4;

    private Compensation testCompensation;

    @Before
    public void setup() {
        testEmployee = new Employee();
        testEmployee.setEmployeeId("123");
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");


        testEmployee2 = new Employee();
        testEmployee2.setEmployeeId("456");
        testEmployee2.setFirstName("Chuck");
        testEmployee2.setLastName("Doe");
        testEmployee2.setDepartment("Engineering");
        testEmployee2.setPosition("Developer");

        testEmployee3 = new Employee();
        testEmployee3.setEmployeeId("789");
        testEmployee3.setFirstName("Moe");
        testEmployee3.setLastName("Doe");
        testEmployee3.setDepartment("Engineering");
        testEmployee3.setPosition("Developer");

        testEmployee4 = new Employee();
        testEmployee4.setEmployeeId("012");
        testEmployee4.setFirstName("Poe");
        testEmployee4.setLastName("Doe");
        testEmployee4.setDepartment("Engineering");
        testEmployee4.setPosition("Developer");

        testCompensation = new Compensation();
        testCompensation.setEmployee(testEmployee);
        testCompensation.setSalary(new BigDecimal(12345));
        testCompensation.setEffectiveDate(LocalDate.now());
    }

    @Test
    public void testCreateEmployee() {
      when(employeeRepository.insert(Mockito.any(Employee.class))).thenReturn(testEmployee);
      Employee employeeReturn = employeeService.createEmployee(testEmployee);
      assertEquals(testEmployee, employeeReturn);
    }

    @Test
    public void testUpdateEmployee() {
        when(employeeRepository.findByEmployeeId(Mockito.anyString())).thenReturn(testEmployee);
        when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(testEmployee);
        Employee employeeReturn = employeeService.updateEmployee(testEmployee);
        assertEquals(testEmployee, employeeReturn);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNonExistentEmployee() {
        when(employeeRepository.findByEmployeeId(Mockito.anyString())).thenReturn(null);
        employeeService.updateEmployee(testEmployee);
    }

    @Test
    public void testReadEmployee() {
        when(employeeRepository.findByEmployeeId(Mockito.anyString())).thenReturn(testEmployee);
        Employee employeeReturn = employeeService.readEmployee(testEmployee.getEmployeeId());
        assertEquals(testEmployee, employeeReturn);
    }

    @Test(expected = EmptyObjectException.class)
    public void testReadNonExistentEmployee() {
        when(employeeRepository.findByEmployeeId(Mockito.anyString())).thenReturn(null);
        employeeService.readEmployee(testEmployee.getEmployeeId());
    }

    @Test
    public void readReportingStructure() {
        when(employeeRepository.findByEmployeeId(testEmployee.getEmployeeId())).thenReturn(testEmployee);
        when(employeeRepository.findByEmployeeId(testEmployee2.getEmployeeId())).thenReturn(testEmployee2);
        when(employeeRepository.findByEmployeeId(testEmployee3.getEmployeeId())).thenReturn(testEmployee3);
        when(employeeRepository.findByEmployeeId(testEmployee4.getEmployeeId())).thenReturn(testEmployee4);
        ReportingStructure reportingStructureReturn;

        testEmployee.setDirectReports(Arrays.asList(testEmployee2, testEmployee3));
        testEmployee2.setDirectReports(Arrays.asList(testEmployee4));
        reportingStructureReturn = employeeService.readEmployeeReportingStructure(testEmployee.getEmployeeId());
        assertEquals(testEmployee, reportingStructureReturn.getEmployee());
        assertEquals(3, reportingStructureReturn.getNumberOfReports());
        cleanReportingStructure();

        testEmployee.setDirectReports(Arrays.asList(testEmployee2));
        testEmployee2.setDirectReports(Arrays.asList(testEmployee3, testEmployee4));
        reportingStructureReturn = employeeService.readEmployeeReportingStructure(testEmployee.getEmployeeId());
        assertEquals(testEmployee, reportingStructureReturn.getEmployee());
        assertEquals(3, reportingStructureReturn.getNumberOfReports());
        cleanReportingStructure();

        testEmployee.setDirectReports(Arrays.asList(testEmployee2, testEmployee3));
        reportingStructureReturn = employeeService.readEmployeeReportingStructure(testEmployee.getEmployeeId());
        assertEquals(testEmployee, reportingStructureReturn.getEmployee());
        assertEquals(2, reportingStructureReturn.getNumberOfReports());
        cleanReportingStructure();

        testEmployee.setDirectReports(Arrays.asList(testEmployee2));
        testEmployee2.setDirectReports(Arrays.asList(testEmployee3));
        reportingStructureReturn = employeeService.readEmployeeReportingStructure(testEmployee.getEmployeeId());
        assertEquals(testEmployee, reportingStructureReturn.getEmployee());
        assertEquals(2, reportingStructureReturn.getNumberOfReports());
        cleanReportingStructure();

        testEmployee.setDirectReports(Arrays.asList(testEmployee2));
        reportingStructureReturn = employeeService.readEmployeeReportingStructure(testEmployee.getEmployeeId());
        assertEquals(testEmployee, reportingStructureReturn.getEmployee());
        assertEquals(1, reportingStructureReturn.getNumberOfReports());
        cleanReportingStructure();

        reportingStructureReturn = employeeService.readEmployeeReportingStructure(testEmployee.getEmployeeId());
        assertEquals(testEmployee, reportingStructureReturn.getEmployee());
        assertEquals(0, reportingStructureReturn.getNumberOfReports());
        cleanReportingStructure();
    }

    @Test(expected = EmptyObjectException.class)
    public void testReadNonExistentEmployeeReportingStructure() {
        when(employeeRepository.findByEmployeeId(Mockito.anyString())).thenReturn(null);
        employeeService.readEmployeeReportingStructure(testEmployee.getEmployeeId());
    }

    @Test
    public void createEmployeeCompensation() {
        when(employeeRepository.findByEmployeeId(Mockito.anyString())).thenReturn(testEmployee);
        when(compensationRepository.findByEmployee(Mockito.any(Employee.class))).thenReturn(null);
        when(compensationRepository.insert(Mockito.any(Compensation.class))).thenReturn(testCompensation);
        Compensation compensationReturn = employeeService.createEmployeeCompensation(testCompensation);
        assertEquals(testCompensation, compensationReturn);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createEmployeeCompensationNonExistentEmployee() {
        when(employeeRepository.findByEmployeeId(Mockito.anyString())).thenReturn(null);
        employeeService.createEmployeeCompensation(testCompensation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createEmployeeCompensationAlreadyExists() {
        when(employeeRepository.findByEmployeeId(Mockito.anyString())).thenReturn(testEmployee);
        when(compensationRepository.findByEmployee(Mockito.any(Employee.class))).thenReturn(testCompensation);
        employeeService.createEmployeeCompensation(testCompensation);
    }

    @Test
    public void readEmployeeCompensation() {
        when(employeeRepository.findByEmployeeId(Mockito.anyString())).thenReturn(testEmployee);
        when(compensationRepository.findByEmployee(Mockito.any(Employee.class))).thenReturn(testCompensation);
        Compensation compensationReturn = employeeService.readEmployeeCompensation(testEmployee.getEmployeeId());
        assertEquals(testCompensation, compensationReturn);
    }

    @Test(expected = EmptyObjectException.class)
    public void readNonExistentEmployeeCompensationNoEmployee() {
        when(employeeRepository.findByEmployeeId(Mockito.anyString())).thenReturn(null);
        when(compensationRepository.findByEmployee(null)).thenReturn(null);
        employeeService.readEmployeeCompensation(testEmployee.getEmployeeId());
    }

    @Test(expected = EmptyObjectException.class)
    public void readNonExistentEmployeeCompensationNoCompensation() {
        when(employeeRepository.findByEmployeeId(Mockito.anyString())).thenReturn(testEmployee);
        when(compensationRepository.findByEmployee(Mockito.any(Employee.class))).thenReturn(null);
        employeeService.readEmployeeCompensation(testEmployee.getEmployeeId());
    }

    /**
     * Clean reporting structure so new reporting structures can be set up for different tests
     */
    private void cleanReportingStructure() {
        testEmployee.setDirectReports(null);
        testEmployee2.setDirectReports(null);
        testEmployee3.setDirectReports(null);
        testEmployee4.setDirectReports(null);
    }
}
