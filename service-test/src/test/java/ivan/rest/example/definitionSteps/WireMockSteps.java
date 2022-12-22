package test.java.ivan.rest.example.definitionSteps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import test.java.ivan.rest.example.clients.RestClient;
import test.java.ivan.rest.example.clients.RestClient.RequestTypes;
import test.java.ivan.rest.example.clients.WireMockClient;
import test.java.ivan.rest.example.configuration.SpringIntegrationTestConfiguration;
import test.java.ivan.rest.example.util.session.Session;

import static test.java.ivan.rest.example.util.session.SessionKey.STUB_REQUEST;
import static test.java.ivan.rest.example.util.session.SessionKey.STUB_RESPONSE;
import static test.java.ivan.rest.example.util.testUtils.TestUtil.invalidateParam;

public class WireMockSteps {

    @Autowired
    private WireMockClient wireMockClient;
    @Autowired
    private Session session;
    @Autowired
    ObjectMapper objectMapper;

    @SneakyThrows
    @Given("wiremock stub is set for {requestType} request with {string} URL")
    public void initializeWireMockStub(RequestTypes requestType, String url) {
        String requestBody = invalidateParam(objectMapper.writeValueAsString(session.get(STUB_REQUEST)));
        String responseBody = invalidateParam(objectMapper.writeValueAsString(session.get(STUB_RESPONSE)));

        wireMockClient.publishMapping(requestType, url, requestBody, responseBody);
    }

    @Then("wiremock stub received {requestType} request with {string} URL")
    public void verifyStub(RequestTypes requestType, String url){
        wireMockClient.verifyMapping(requestType, url, 1);
    }
}
