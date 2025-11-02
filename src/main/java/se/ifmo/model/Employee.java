package se.ifmo.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Employee {
    private String name;
    private double salary;
    private String department;
    private LocalDate hireDate;

    public Employee(String name, double salary, String department, LocalDate hireDate) {
        this.name = name;
        this.salary = salary;
        this.department = department;
        this.hireDate = hireDate;
    }
}
