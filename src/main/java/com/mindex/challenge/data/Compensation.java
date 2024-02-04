package com.mindex.challenge.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindex.challenge.config.DateDeserializer;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Compensation {

    @DBRef
    @NotNull(message = "Employee is required!")
    private Employee employee;

    @Min(value = 0, message = "Salary amount must be greater than 0!")
    private BigDecimal salary;

    @NotNull(message = "Effective date is required!")
    //create customer deserializer so we can control the error message returned to the client when date format is incorrect
    //using @DateTimeFormatter or @JsonFormat will return an error message without telling the client what is wrong with the request
    //i.e "Text '1993-10-2' could not be parsed at index 8" instead of "Invalid date format. Expected format is yyyy-MM-dd."
    @JsonDeserialize(using = DateDeserializer.class)
    private LocalDate effectiveDate;

    public Compensation() {
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @Override
    public String toString() {
        return "Compensation{" +
                "employee=" + employee +
                ", salary=" + salary +
                ", effectiveDate=" + effectiveDate +
                '}';
    }
}