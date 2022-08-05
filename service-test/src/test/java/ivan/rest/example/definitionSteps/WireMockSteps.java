package ivan.rest.example.definitionSteps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import ivan.rest.example.clients.RestClient.RequestTypes;
import ivan.rest.example.clients.WireMockClient;
import ivan.rest.example.util.session.Session;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import static ivan.rest.example.util.session.SessionKey.STUB_REQUEST;
import static ivan.rest.example.util.session.SessionKey.STUB_RESPONSE;
import static ivan.rest.example.util.testUtils.TestUtil.invalidateParam;

public class WireMockSteps {

    @Autowired
    private WireMockClient wireMockClient;
    @Autowired
    private Session session;
    @Autowired
    ObjectMapper objectMapper;

    @ParameterType(value = ".*")
    public RequestTypes requestType(String requestType) {
        return RequestTypes.valueOf(requestType);
    }

    @SneakyThrows
    @Given("wiremock stub is set for {requestType} request with {string} URL")
    public void initializeWireMockStub(RequestTypes requestType, String url) {
        String requestBody = invalidateParam(objectMapper.writeValueAsString(session.get(STUB_REQUEST)));
        String responseBody = invalidateParam(objectMapper.writeValueAsString(session.get(STUB_RESPONSE)));

        wireMockClient.publishMapping(requestType, url, requestBody, responseBody);
    }

    @Then("wiremock stub received {requestType} request with {string} URL")
    public void verifyStub(RequestTypes requestType, String url){
        wireMockClient.verifyMapping(requestType, url);
    }
}
