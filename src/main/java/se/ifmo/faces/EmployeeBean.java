package se.ifmo.faces;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import lombok.Getter;
import se.ifmo.model.Employee;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Named("employeeBean")
@SessionScoped
public class EmployeeBean implements Serializable {

    private List<Employee> employees;

    public EmployeeBean() {
        employees = new ArrayList<>();
        // Добавим тестовые данные
        employees.add(new Employee("Иванов Иван", 80000, "IT", LocalDate.of(2023, 1, 15)));
        employees.add(new Employee("Петрова Анна", 75000, "HR", LocalDate.of(2023, 3, 1)));
        employees.add(new Employee("Сидоров Петр", 90000, "Продажи", LocalDate.of(2023, 2, 10)));
        employees.add(new Employee("Козлова Мария", 85000, "Маркетинг", LocalDate.of(2023, 4, 5)));
    }
}
