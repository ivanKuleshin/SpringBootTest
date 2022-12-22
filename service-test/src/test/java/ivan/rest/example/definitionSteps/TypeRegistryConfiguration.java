package test.java.ivan.rest.example.definitionSteps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.http.Method;
import main.java.ivan.rest.example.model.Address;
import main.java.ivan.rest.example.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import test.java.ivan.rest.example.clients.RestClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TypeRegistryConfiguration {

    @Autowired
    private ObjectMapper objectMapper;

    @ParameterType(value = ".*")
    public RestClient.RequestTypes requestType(String requestType) {
        return RestClient.RequestTypes.valueOf(requestType);
    }

    @ParameterType(value = ".*")
    public Employee singleEmployee(String employeeParams) {
        List<String> params = Stream.of(employeeParams.split(",")).map(String::trim).collect(Collectors.toList());

        return new Employee(Integer.valueOf(params.get(0)), params.get(1), params.get(2), params.get(3), null, null);
    }

    @ParameterType("[A-Z]+")
    public Method httpMethod(String requestType) {
        return Method.valueOf(requestType);
    }

    @DataTableType
    public Employee employeeEntry(Map<String, String> row) {
        return Employee.builder()
                .id(Integer.valueOf(row.get("id")))
                .name(row.get("name"))
                .passportNumber(row.get("passportNumber"))
                .education(row.get("education"))
                .address(Address.builder()
                        .city(row.get("address.city"))
                        .country(row.get("address.country"))
                        .zip(row.get("address.zip"))
                        .build())
                .employeeHash(row.get("employeeHash"))
                .build();
    }
}
