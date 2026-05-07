package services;

import models.Employee;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmployeeService {

    private static List<Employee> employees = new ArrayList<>(Arrays.asList(
        new Employee(1, "Admin User",   "admin@company.com", "9999999999", "Human Resources", "HR Manager",          "admin",    "admin123"),
        new Employee(2, "John Doe",     "john@company.com",  "9876543210", "Engineering",     "Software Engineer",   "employee", "john123"),
        new Employee(3, "Jane Smith",   "jane@company.com",  "9123456789", "Finance",         "Accountant",          "employee", "jane123"),
        new Employee(4, "Ravi Kumar",   "ravi@company.com",  "9988776655", "Engineering",     "Backend Developer",   "employee", "ravi123"),
        new Employee(5, "Priya Nair",   "priya@company.com", "9876001234", "Marketing",       "Marketing Executive", "employee", "priya123")
    ));

    private static int nextId = 6;

    public Employee login(String email, String password) {
        return employees.stream()
            .filter(e -> e.getEmail().equalsIgnoreCase(email) && e.getPassword().equals(password))
            .findFirst().orElse(null);
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }

    public boolean registerEmployee(Employee emp) {
        emp.setEmpId(nextId++);
        employees.add(emp);
        return true;
    }

    public boolean updateEmployee(Employee emp) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getEmpId() == emp.getEmpId()) {
                employees.set(i, emp);
                return true;
            }
        }
        return false;
    }

    public boolean deleteEmployee(int empId) {
        return employees.removeIf(e -> e.getEmpId() == empId);
    }

    public Employee getEmployee(int empId) {
        return employees.stream().filter(e -> e.getEmpId() == empId).findFirst().orElse(null);
    }
}
