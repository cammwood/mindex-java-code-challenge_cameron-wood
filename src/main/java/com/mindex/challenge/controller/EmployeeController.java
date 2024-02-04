package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.model.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    /**
     * Create a new employee record
     * @param employee
     * @return Employee
     */
    @Operation(summary = "Creates a new employee.")
    @PostMapping("/employee")
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody @Valid Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);

        return employeeService.createEmployee(employee);
    }

    /**
     * Get an employee by employee id
     * @param id
     * @return Employee
     */
    @Operation(summary = "Gets employee information by their id.")
    @GetMapping("/employee/{id}")
    public Employee readEmployee(@PathVariable String id) {
        LOG.debug("Received employee read request for id [{}]", id);

        return employeeService.readEmployee(id);
    }

    /**
     * Update an employee's record
     * @param id
     * @param employee
     * @return Employee
     */
    @Operation(summary = "Update an employee's information.")
    @PutMapping("/employee/{id}")
    public Employee updateEmployee(@PathVariable String id, @RequestBody @Valid Employee employee) {
        LOG.debug("Received employee update request for id [{}] and employee [{}]", id, employee);

        employee.setEmployeeId(id);
        return employeeService.updateEmployee(employee);
    }

    /**
     * Get an employees number of direct reports.
     * @param id
     * @return ReportingStructure
     */
    @Operation(summary = "Gets an employee's direct reports information.")
    @GetMapping("/employee/{id}/direct-reports")
    public ReportingStructure getEmployeeReportingStructure(@PathVariable String id) {
        LOG.debug("Received reporting structure read for employee request for id [{}]", id);

        return employeeService.readEmployeeReportingStructure(id);
    }

    /**
     * Get an employee's compensation information
     * @param id
     * @return Compensation
     */
    @Operation(summary = "Gets an employee's compensation information.")
    @GetMapping("/employee/{id}/compensation")
    public Compensation getEmployeeCompensation(@PathVariable String id) {
        LOG.debug("Received employee compensation read request for id [{}]", id);

        return employeeService.readEmployeeCompensation(id);
    }

    /**
     * Create a compensation record for an employee
     * @param compensation
     * @return Compensation
     */
    @Operation(summary = "Creates a compensation record for an employee.")
    @PostMapping("/employee/{id}/compensation")
    @ResponseStatus(HttpStatus.CREATED)
    public Compensation createEmployeeCompensation(@PathVariable String id, @RequestBody @Valid Compensation compensation) {
        LOG.debug("Received employee compensation create request for [{}]", compensation);

        compensation.getEmployee().setEmployeeId(id);
        return employeeService.createEmployeeCompensation(compensation);
    }
}