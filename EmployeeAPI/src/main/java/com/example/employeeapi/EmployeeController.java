package com.example.employeeapi;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeDAO employeeDAO;

    @GetMapping("/")
    public List<Employee> getAllEmployees() {
        return employeeDAO.getAll();
    }

    @PostMapping("/")
    public void addEmployee(@RequestBody Employee employee) {
        employeeDAO.add(employee);
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable int id) {
        return employeeDAO.getById(id);
    }

    @PutMapping("/{id}")
    public void updateEmployee(@PathVariable int id, @RequestBody Employee updatedEmployee) {
        employeeDAO.update(id, updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable int id) {
        employeeDAO.delete(id);
    }
}
