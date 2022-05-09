package ivan.rest.example.controller;

import ivan.rest.example.client.ExternalClient;
import ivan.rest.example.exception.CustomRuntimeException;
import ivan.rest.example.model.Address;
import ivan.rest.example.model.Employee;
import ivan.rest.example.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    ExternalClient externalClient;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAll();
    }

    @GetMapping("/{employeeId}")
    public Employee getById(@PathVariable Integer employeeId) {
        return employeeService.getById(employeeId);
    }

    @PutMapping
    public String add(@RequestBody Employee employee) {
        employeeService.add(employee);
        return String.format("Employee with id = %s has been added!", employee.getId());
    }

    @PostMapping("/address/{employeeId}")
    public @ResponseBody Employee addAddress(@PathVariable Integer employeeId, @RequestBody Address address) {
        Employee currentEmployee = employeeService.getById(employeeId);
        if (currentEmployee.getAddress().isEmpty()) {
            currentEmployee.setAddress(address);
            return currentEmployee;
        } else {
            throw new CustomRuntimeException("Employee already has an address. Please use update instead of create.");
        }
    }

    @PutMapping("/list")
    public List<Employee> addList(@RequestBody List<Employee> employees) {
        employeeService.addList(employees);
        return employeeService.getAll();
    }

    @PatchMapping("/update")
    public Employee updateEmployee(@Valid @RequestBody Employee employee) {
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
        externalClient.deleteRequestToExternalService(id);
        return String.format("Employee with id = %s has been successfully deleted!", id);
    }

    @DeleteMapping
    public String deleteAll() {
        employeeService.clear();
        return "All employees have been successfully deleted!";
    }
}
