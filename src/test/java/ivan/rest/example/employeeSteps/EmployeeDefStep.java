package ivan.rest.example.employeeSteps;

import io.cucumber.spring.CucumberContextConfiguration;
import ivan.rest.example.exception.CustomRuntimeException;
import ivan.rest.example.model.Employee;
import ivan.rest.example.configuration.SpringIntegrationTestConfiguration;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ivan.rest.example.util.session.SessionKey.*;
import static ivan.rest.example.util.testUtils.TestUtil.castCollectionTo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@CucumberContextConfiguration
public class EmployeeDefStep extends SpringIntegrationTestConfiguration {

    @Before
    public void doSetup() {
        // Was added to test the parallel execution:
        long threadId = Thread.currentThread().getId();
        String processName = ManagementFactory.getRuntimeMXBean().getName();
        log.info(String.format("Started in thread: %s, in JVM: %s", threadId, processName));
    }

    @Before(value = "@testData")
    public void setUp() {
        System.out.println("This methods performs only for the Scenarios with @testData");
    }

    @After
    public void cleanUp() {
        restTemplate.delete("/employee");
        session.clear();
    }

    @DataTableType
    public Employee employeeEntry(Map<String, String> row) {
        return Employee.builder()
                .id(Integer.valueOf(row.get("id")))
                .name(row.get("name"))
                .passportNumber(row.get("passportNumber"))
                .education(row.get("education"))
                .build();
    }

    @ParameterType(value = ".*")
    public Employee singleEmployee(String employeeParams) {
        List<String> params = Stream.of(employeeParams.split(",")).map(String::trim).collect(Collectors.toList());

        return new Employee(Integer.valueOf(params.get(0)), params.get(1), params.get(2), params.get(3));
    }

    @Given("Employee '{singleEmployee}' added to Employee rest service repository")
    public void addEmployee(Employee employee) {
        restTemplate.put("/employee", employee);

        ResponseEntity<Employee> forEntity = restTemplate.getForEntity("/employee/" + employee.getId(), Employee.class);

        Assert.assertTrue(forEntity.getStatusCode().is2xxSuccessful());
        log.info("Test result {}", forEntity.toString());
    }

    @Given("employees added to Employee rest service repository:")
    public void addListOfEmployees(List<Employee> employees) {
        restTemplate.put("/employee/list", employees);
        List<Employee> expectedList = employees.stream().sorted().collect(Collectors.toList());
        session.put(EXPECTED_LIST, expectedList);
    }

    @When("we send {string} request to the {string} endpoint")
    public void weSendGETRequestToTheEmployeeEndpoint(String methodName, String endpoint) {
        ResponseEntity<List<Employee>> responseEntity = restTemplate.exchange(
                String.format("%s/%s", baseUrl, endpoint),
                HttpMethod.resolve(methodName),
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        List<Employee> actualList = Objects.requireNonNull(responseEntity.getBody()).stream().sorted().collect(Collectors.toList());
        session.put(ACTUAL_LIST, actualList);
    }

    @Then("retrieved data is equal to added data")
    public void retrievedDataIsEqualToAddedData() {
        assertThat(session.get(ACTUAL_LIST, Object.class))
                .isEqualTo(session.get(EXPECTED_LIST, Object.class));
    }

    @When("we send {string} request to the {string} endpoint with {int} id")
    public void weSendGETRequestToTheEmployeeEndpointWithId(String methodName, String endpoint, int id) {
        ResponseEntity<Employee> responseEntity = restTemplate.exchange(
                String.format("%s/%s/%s", baseUrl, endpoint, id),
                HttpMethod.resolve(methodName),
                null,
                Employee.class
        );

        Employee actual = Objects.requireNonNull(responseEntity.getBody());
        session.put(ACTUAL_RESULT, actual);

        Employee expected = castCollectionTo(Employee.class, session.get(EXPECTED_LIST, List.class))
                .stream().filter(employee -> employee.getId() == id).findFirst()
                .orElseThrow(() -> new CustomRuntimeException(String.format("Expected employee not found with id = %s", id)));

        session.put(EXPECTED_RESULT, expected);
    }

    @Then("retrieved data is equal to added data for specified id")
    public void retrievedDataIsEqualToAddedDataForSpecifiedId() {
        assertThat(session.get(ACTUAL_RESULT, Employee.class))
                .isEqualTo(session.get(EXPECTED_RESULT, Employee.class));
    }

    @Given("User sees that our controller is not NULL")
    public void controllerIsNotNull() {
        assertThat(controller).isNotNull();
        log.info("Controller checking...");
    }
}
