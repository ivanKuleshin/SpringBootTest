package test.java.ivan.rest.example.clients;

import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import test.java.ivan.rest.example.clients.RestClient.RequestTypes;
import test.java.ivan.rest.example.exceptions.TestExecutionException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Component
public class WireMockClient {
    private static final String INVALID_HTTP_METHOD_MESSAGE = "'%s' invalid HTTP method received!";

    public void resetWireMock() {
        WireMock.reset();
    }

    @SneakyThrows
    public void publishMapping(RequestTypes requestType, String url, String requestBody, String responseBody) {
        switch (requestType) {
            case PUT, POST -> WireMock
                    .stubFor(request(requestType.getValue(), urlEqualTo(url))
                            .withRequestBody(equalToJson(requestBody, true, false))
                            .willReturn(okJson(responseBody)));
            case DELETE -> WireMock.stubFor(delete(url));
            case GET -> WireMock
                    .stubFor(get(url)
                            .willReturn(okJson(responseBody)));
            default -> throw new TestExecutionException(INVALID_HTTP_METHOD_MESSAGE, requestType);
        }
    }

    public void verifyMapping(RequestTypes requestType, String url, int count) {
        switch (requestType) {
            case DELETE -> WireMock.verify(count, deleteRequestedFor(urlEqualTo(url)));
            case PUT -> WireMock.verify(count, putRequestedFor(urlEqualTo(url)));
            default -> throw new TestExecutionException(INVALID_HTTP_METHOD_MESSAGE, requestType);
        }
    }
}
