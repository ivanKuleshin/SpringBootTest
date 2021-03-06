package ivan.rest.example.definitionSteps;

import com.fasterxml.jackson.core.type.TypeReference;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.response.Response;
import ivan.rest.example.clients.RestClient;
import ivan.rest.example.clients.RestClient.RequestTypes;
import ivan.rest.example.configuration.SpringIntegrationTestConfiguration;
import ivan.rest.example.exception.CustomRuntimeException;
import ivan.rest.example.exceptions.TestExecutionException;
import ivan.rest.example.model.Address;
import ivan.rest.example.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ivan.rest.example.clients.RestClient.RequestTypes.*;
import static ivan.rest.example.util.session.SessionKey.*;
import static ivan.rest.example.util.testUtils.TestUtil.castMapToObject;
import static ivan.rest.example.util.testUtils.TestUtil.convertValueToList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@CucumberContextConfiguration
public class EmployeeSteps extends SpringIntegrationTestConfiguration {

    @Autowired
    private RestClient restClient;

    private static final int FIRST = 0;
    private static final int STATUS_OK = 200;

    static final TypeReference<List<Employee>> employeeListTypeReference = new TypeReference<>() {
    };

    private static final String ALL_VALUES = "$";

    @Before
    public void doSetup() {
        // Was added to test the parallel execution:
        long threadId = Thread.currentThread().getId();
        String processName = ManagementFactory.getRuntimeMXBean().getName();
        log.info(String.format("Started in thread: %s, in JVM: %s", threadId, processName));
    }

    @After
    public void cleanUp() {
        restClient.sendRequestWithoutParams(DELETE, "/employee").then().statusCode(STATUS_OK);
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
                .employeeHash(row.get("employeeHash"))
                .build();
    }

    @ParameterType(value = ".*")
    public Employee singleEmployee(String employeeParams) {
        List<String> params = Stream.of(employeeParams.split(",")).map(String::trim).collect(Collectors.toList());

        return new Employee(Integer.valueOf(params.get(0)), params.get(1), params.get(2), params.get(3), null, null);
    }

    @Given("Employee '{singleEmployee}' added to Employee rest service repository")
    public void addEmployee(Employee employee) {
        restTemplate.put("/employee", employee);

        ResponseEntity<Employee> forEntity = restTemplate.getForEntity("/employee/" + employee.getId(), Employee.class);

        Assert.assertTrue(forEntity.getStatusCode().is2xxSuccessful());
        log.info("Test result {}", forEntity);
    }

    @Given("employees added to Employee rest service repository:")
    public void addListOfEmployees(List<Employee> employees) {
        Response response = restClient.sendRequestWithBody(PUT, "/employee/list", employees);

        if (response.statusCode() != STATUS_OK) {
            log.info("Response is: " + response.prettyPrint());
            throw new TestExecutionException("Test data eas NOT added to service repository");
        }

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
    public void theRequestIsSentToTheEmployeeEndpointWithParams(String requestTypeStr, String endpoint, Map<String, String> params) {
        RequestTypes requestType = RequestTypes.valueOf(requestTypeStr);
        var response = restClient.sendRequestWithParams(requestType, endpoint, params);
        session.put(RESPONSE, response);
    }

    @When("the {string} request is sent to the {string} endpoint without params")
    public void theRequestIsSentToTheEmployeeEndpointWithoutParams(String requestTypeStr, String endpoint) {
        RequestTypes requestType = RequestTypes.valueOf(requestTypeStr);
        var response = restClient.sendRequestWithoutParams(requestType, endpoint);
        session.put(RESPONSE, response);
    }

    @When("the {string} request is sent to the {string} endpoint with body")
    public void theRequestIsSentToTheEmployeeEndpointWithBody(String requestTypeStr, String endpoint, List<Map<String, String>> requestBody) {
        RequestTypes requestType = RequestTypes.valueOf(requestTypeStr);
        Response response;

        if (requestBody.size() == 1) {
            response = restClient.sendRequestWithBody(requestType, endpoint, requestBody.get(FIRST));
        } else {
            response = restClient.sendRequestWithBody(requestType, endpoint, requestBody);
        }

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

    @And("error message contains: {string}")
    public void verifyErrorMessage(String expectedMessage) {
        Response response = session.get(RESPONSE, Response.class);
        String actualMessage = response.getBody().jsonPath().getMap(ALL_VALUES).get("message").toString();

        assertThat(actualMessage).as("Error message mismatch!").contains(expectedMessage);
    }

    @Then("retrieved data is equal to added data")
    public void verifyRetrievedDataIsEqualToAddedData() {
        var responseBody = session.get(RESPONSE, Response.class).jsonPath().get(ALL_VALUES);

        List<Employee> actualResult = convertValueToList(responseBody, employeeListTypeReference);
        List<Employee> expectedResult = convertValueToList(session.get(EXPECTED_RESULT), employeeListTypeReference);
        expectedResult = actualResult.stream().filter(expectedResult::contains).collect(Collectors.toList());

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Then("employee with {int} ID has been added to the repository correctly")
    public void verifyEmployeeHasBeenAdded(int employeeId){
        Response response = restClient.sendRequestWithParams(GET, "/employee/{id}", Map.of("id", employeeId))
                .then().statusCode(STATUS_OK).extract().response();
        Employee actualEmployee = castMapToObject(response.jsonPath().get(ALL_VALUES), Employee.class);

        Employee expectedEmployee = castMapToObject(session.get(EXPECTED_RESULT), Employee.class);

        assertThat(actualEmployee).isEqualTo(expectedEmployee);
    }

    @Then("employee with id {int} is deleted from the repository")
    public void verifyEmployeeIsDeleted(int employeeId) {
        Response response = restClient.sendRequestWithoutParams(GET, "/employee")
                .then().statusCode(STATUS_OK).extract().response();

        List<Employee> actualResponseList = convertValueToList(response.jsonPath().get(ALL_VALUES),
                employeeListTypeReference);

        assertThat(actualResponseList.stream().anyMatch(employee -> employee.getId() == employeeId))
                .as("Employee with id %s was NOT deleted!", employeeId)
                .isFalse();
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

        Employee expected = convertValueToList(session.get(EXPECTED_LIST), employeeListTypeReference)
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
