package com.mindex.challenge.data;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

public class Employee {

    @Id
    private String employeeId;

    @NotEmpty(message = "First name is required!")
    //match only characters and at least one
    @Pattern(regexp = "^[A-Za-z]+$",  message = "Last name must only contain characters!")
    private String firstName;

    @NotEmpty(message = "Last name is required!")
    //match only characters and at least one
    @Pattern(regexp = "^[A-Za-z]+$", message = "Last name must only contain characters!")
    private String lastName;

    @NotEmpty(message = "Position name is required!")
    private String position;

    @NotEmpty(message = "Department name is required!")
    private String department;
    private List<Employee> directReports;

    public Employee() {
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<Employee> getDirectReports() {
        return directReports;
    }

    public void setDirectReports(List<Employee> directReports) {
        this.directReports = directReports;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId='" + employeeId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", position='" + position + '\'' +
                ", department='" + department + '\'' +
                ", directReports=" + directReports +
                '}';
    }
}
