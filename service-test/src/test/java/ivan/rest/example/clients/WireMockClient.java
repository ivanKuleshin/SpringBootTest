package ivan.rest.example.clients;

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

    @SneakyThrows
    public void publishMapping(RequestTypes requestType, String url, String requestBody, String responseBody) {
        switch (requestType) {
            case POST:
                WireMock.stubFor(post(url)
                        .withRequestBody(equalToJson(requestBody, true, false))
                        .willReturn(okJson(responseBody)));
                break;
            case PUT:
                WireMock.stubFor(put(url)
                        .withRequestBody(equalToJson(requestBody, true, false))
                        .willReturn(okJson(responseBody)));
                break;
            case DELETE:
                WireMock.stubFor(delete(url));
                break;
            case GET:
                WireMock.stubFor(get(url)
                        .willReturn(okJson(responseBody)));
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
                WireMock.verify(1, putRequestedFor(urlEqualTo(url)));
                break;
        }
    }
}
