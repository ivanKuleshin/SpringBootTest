package ivan.rest.example.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import ivan.rest.example.clients.RestClient.RequestTypes;
import ivan.rest.example.exceptions.TestExecutionException;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Component
public class WireMockClient {
    public void resetWireMock() {
        WireMock.reset();
    }

    private static ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public void publishMapping(RequestTypes requestType, String url, String requestBody, String responseBody) {
        switch (requestType) {
            case POST:
                WireMock.stubFor(WireMock.post(url)
                        .withRequestBody(WireMock.equalToJson(requestBody, true, false))
                        .willReturn(WireMock.okJson(responseBody)));
                break;
            case PUT:
                WireMock.stubFor(WireMock.put(url)
                        .withRequestBody(WireMock.equalToJson(requestBody, true, false))
                        .willReturn(WireMock.okJson(responseBody)));
                break;
            case DELETE:
                WireMock.stubFor(WireMock.delete(url));
                break;
            default:
                throw new TestExecutionException("Invalid HTTP method received!");
        }
    }

    public void verifyMapping(RequestTypes requestType, String url) {
        switch (requestType) {
            case DELETE:
                WireMock.verify(1, deleteRequestedFor(urlEqualTo(url)));
                break;
            case PUT:
                WireMock.verify(putRequestedFor(urlEqualTo(url)));
                break;
        }
    }
}