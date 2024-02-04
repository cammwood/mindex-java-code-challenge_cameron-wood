package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.model.ReportingStructure;

public interface EmployeeService {
    Employee createEmployee(Employee employee);
    Employee readEmployee(String id);
    Employee updateEmployee(Employee employee);
    ReportingStructure readEmployeeReportingStructure(String id);
    Compensation createEmployeeCompensation(Compensation compensation);
    Compensation readEmployeeCompensation(String id);
}
