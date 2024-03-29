package ivan.rest.example.definitionSteps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import ivan.rest.example.test.clients.RestClient.RequestTypes;
import ivan.rest.example.test.clients.WireMockClient;
import ivan.rest.example.test.utils.session.Session;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import static ivan.rest.example.test.utils.TestUtil.invalidateParam;
import static ivan.rest.example.test.utils.session.SessionKey.STUB_REQUEST;
import static ivan.rest.example.test.utils.session.SessionKey.STUB_RESPONSE;

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
