package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.exceptions.EmptyObjectException;
import com.mindex.challenge.model.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.mindex.challenge.service.ApplicationConstants.EMPLOYEE_NOT_FOUND;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompensationRepository compensationRepository;

    /**
     * Create a new employee record
     * @param employee
     * @return Employee
     */
    @Override
    public Employee createEmployee(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        LOG.info("Employee with id {} created successfully!", employee.getEmployeeId());
        return employee;
    }

    /**
     * Retrieve an employee by their id
     * @param id
     * @return Employee
     */
    @Override
    public Employee readEmployee(String id) {
        LOG.debug("Reading employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new EmptyObjectException(EMPLOYEE_NOT_FOUND + id);
        }

        LOG.info("Found employee with id {}!", id);
        return employee;
    }

    /**
     * Update an employee record
     * @param employee
     * @return Employee
     */
    @Override
    public Employee updateEmployee(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        if (employeeRepository.findByEmployeeId(employee.getEmployeeId()) == null) {
            throw new IllegalArgumentException(EMPLOYEE_NOT_FOUND + employee.getEmployeeId());
        }
        employeeRepository.save(employee);

        LOG.info("Employee with id {} updated successfully!", employee.getEmployeeId());
        return employee;
    }

    /**
     * Retrieve an employee's report structure
     * @param id
     * @return ReportingStructure
     */
    @Override
    public ReportingStructure readEmployeeReportingStructure(String id) {
        LOG.debug("Reading employee reporting structure with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);
        if (employee == null) {
            throw new EmptyObjectException(EMPLOYEE_NOT_FOUND + id);
        }

        ReportingStructure structure = new ReportingStructure(employee, getNumberOfReports(employee));
        LOG.info("Found reporting structure for employee with id {}!", id);
        return structure;
    }

    /**
     * Create a compensation record for an employee
     * @param compensation
     * @return Compensation
     */
    @Override
    public Compensation createEmployeeCompensation(Compensation compensation) {
        Employee employee = employeeRepository.findByEmployeeId(compensation.getEmployee().getEmployeeId());
        if (employee == null) {
            throw new IllegalArgumentException(EMPLOYEE_NOT_FOUND + compensation.getEmployee().getEmployeeId());
        }
        //depending on requirements an upsert could be done here instead of throwing an exception
        //otherwise an update should be done through an PUT endpoint
        if (compensationRepository.findByEmployee(employee) != null) {
            throw new IllegalArgumentException("Compensation already exists for employee " + employee.getEmployeeId());
        }
        compensation.setEmployee(employee);
        compensationRepository.insert(compensation);
        LOG.info("Created compensation for employee id {} successfully!", employee.getEmployeeId());
        return compensation;
    }

    /**
     * Retrieve an employee's compensation record
     * @param id
     * @return Compensation
     */
    @Override
    public Compensation readEmployeeCompensation(String id) {
        Compensation compensation = compensationRepository.findByEmployee(employeeRepository.findByEmployeeId(id));

        if (compensation == null) {
            throw new EmptyObjectException("No compensation found for employee with id: " + id);
        }

        LOG.info("Found compensation for employee id {} successfully!", id);
        return compensation;
    }

    /**
     * Get an employee's number of reports.
     * Reports are defined as the employee's direct reports and all of their distinct reports.
     * @param employee
     * @return int
     */
    private int getNumberOfReports(Employee employee) {
        int total = 0;
        if (employee != null) {
            total += CollectionUtils.isNotEmpty(employee.getDirectReports()) ? employee.getDirectReports().size() : 0;
            for (Employee report : CollectionUtils.emptyIfNull(employee.getDirectReports())) {
                //use recursion since reports are a tree structure and we want to traverse the tree and do the same
                //operation at each node
                total += getNumberOfReports(employeeRepository.findByEmployeeId(report.getEmployeeId()));
            }
            LOG.debug("Employee id {} direct reports are {}", employee.getEmployeeId(), total);
        }
        return total;
    }
}
