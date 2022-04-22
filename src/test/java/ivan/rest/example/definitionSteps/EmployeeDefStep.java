package ivan.rest.example.definitionSteps;

import com.fasterxml.jackson.core.type.TypeReference;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.response.Response;
import ivan.rest.example.clients.RestClient;
import ivan.rest.example.clients.RestClient.RequestTypes;
import ivan.rest.example.configuration.SpringIntegrationTestConfiguration;
import ivan.rest.example.exception.CustomRuntimeException;
import ivan.rest.example.model.Address;
import ivan.rest.example.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ivan.rest.example.util.session.SessionKey.*;
import static ivan.rest.example.util.testUtils.TestUtil.castCollectionTo;
import static ivan.rest.example.util.testUtils.TestUtil.convertValue;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@CucumberContextConfiguration
public class EmployeeDefStep extends SpringIntegrationTestConfiguration {

    @Autowired
    private RestClient restClient;

    private final TypeReference<List<Employee>> employeeListTypeReference = new TypeReference<>() {
    };

    private static final String ALL_VALUES = "$";

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
                .address(Address.builder()
                        .city(row.get("address.city"))
                        .country(row.get("address.country"))
                        .zip(row.get("address.zip"))
                        .build())
                .build();
    }

    @ParameterType(value = ".*")
    public Employee singleEmployee(String employeeParams) {
        List<String> params = Stream.of(employeeParams.split(",")).map(String::trim).collect(Collectors.toList());

        return new Employee(Integer.valueOf(params.get(0)), params.get(1), params.get(2), params.get(3), null);
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

        employees.sort(Comparator.comparing(Employee::getId));
        session.put(EXPECTED_RESULT, employees);
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

    @When("the {string} request is sent to the {string} endpoint with params:")
    public void theGETRequestIsSentToTheEmployeeEndpointWithParams(String requestTypeStr, String endpoint, Map<String, String> params) {
        RequestTypes requestType = RequestTypes.valueOf(requestTypeStr);
        var response = restClient.sendRequestWithParams(requestType, endpoint, params);
//        .jsonPath().get("$");
        session.put(RESPONSE, response);
    }

    @When("the {string} request is sent to the {string} endpoint without params")
    public void theGETRequestIsSentToTheEmployeeEndpointWithoutParams(String requestTypeStr, String endpoint) {
        RequestTypes requestType = RequestTypes.valueOf(requestTypeStr);
        var response = restClient.sendRequestWithoutParams(requestType, endpoint);
        session.put(RESPONSE, response);
    }

    @Then("the status code is {int}")
    public void verifyStatusCode(int expectedStatusCode) {
        Response response = session.get(RESPONSE, Response.class);
        int actualStatusCode = response.getStatusCode();

        assertThat(actualStatusCode)
                .as("Status code mismatch! \n Response is %s", response.getBody().print())
                .isEqualTo(expectedStatusCode);
    }

    @Then("retrieved data is equal to added data")
    public void retrievedDataIsEqualToAddedData() {
        List<Employee> actualResult = convertValue(session.get(RESPONSE, Response.class).jsonPath().get(ALL_VALUES),
                employeeListTypeReference);
        List<Employee> expectedResult = convertValue(session.get(EXPECTED_RESULT), employeeListTypeReference);

        assertThat(actualResult).isEqualTo(expectedResult);
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
