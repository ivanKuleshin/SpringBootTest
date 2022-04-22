package ivan.rest.example.controller;

import ivan.rest.example.model.Employee;
import ivan.rest.example.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAll();
    }

    @GetMapping("/{id}")
    public Employee getById(@PathVariable Integer id) {
        return employeeService.getById(id);
    }

    @PutMapping
    public String add(@RequestBody Employee employee) {
        employeeService.add(employee);
        return String.format("Employee with id = %s has been added!", employee.getId());
    }

    @PutMapping("/list")
    public List<Employee> addList(@RequestBody List<Employee> employees) {
        employeeService.addList(employees);
        return employeeService.getAll();
    }

    @PatchMapping("/update")
    public Employee updateEmployee(@RequestBody Employee employee) {
        Employee employeeFromDB = employeeService.getById(employee.getId());

        Employee employeeToUpdate = Employee.builder()
                .id(employee.getId())
                .name(isNull(employee.getName()) ? employeeFromDB.getName() : employee.getName())
                .passportNumber(isNull(employee.getPassportNumber()) ? employeeFromDB.getPassportNumber() : employee.getPassportNumber())
                .education(isNull(employee.getEducation()) ? employeeFromDB.getEducation() : employee.getEducation())
                .address(employeeFromDB.getAddress())
                .build();

        return employeeService.update(employeeToUpdate);
    }

    @DeleteMapping("/{id}")
    public String removeEmployeeById(@PathVariable Integer id) {
        employeeService.deleteById(id);
        return String.format("Employee with id = %s has been successfully deleted!", id);
    }

    @DeleteMapping
    public String deleteAll() {
        employeeService.clear();
        return "All employees have been successfully deleted!";
    }
}
