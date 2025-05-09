package com.example.employeeapi;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeDAO {

    private List<Employee> employeeList = new ArrayList<>();

    public List<Employee> getAll() {
        return employeeList;
    }

    public void add(Employee employee) {
        employeeList.add(employee);
    }

    public Employee getById(int id) {
        return employeeList.stream()
                .filter(emp -> emp.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void update(int id, Employee updated) {
        for (int i = 0; i < employeeList.size(); i++) {
            if (employeeList.get(i).getId() == id) {
                employeeList.set(i, updated);
                return;
            }
        }
    }

    public void delete(int id) {
        employeeList.removeIf(emp -> emp.getId() == id);
    }
}