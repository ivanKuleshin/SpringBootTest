package ivan.rest.example.clients;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static ivan.rest.example.configuration.SpringIntegrationTestConfiguration.baseUrl;
import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;

@Slf4j
@Component
public class RestClient {
    private static final String LOG_MESSAGE = "[{}] request was sent for URI: {}\nWith url parameter: {}";
    private static final String NULL = "null";

    public Response sendRequestWithParams(Method requestType, String url, Map<String, ?> requestParams) {
        return sendRequestForHttpMethod(requestType, url, null, requestParams);
    }

    public Response sendRequestWithoutParams(Method httpMethod, String url) {
        return sendRequestForHttpMethod(httpMethod, url, null, emptyMap());
    }

    public Response sendRequestWithBody(Method httpMethod, String url, Object requestBody) {
        return sendRequestForHttpMethod(httpMethod, url, requestBody, emptyMap());
    }

    private Response sendRequestForHttpMethod(Method httpMethod, String url, Object requestBody, Map<String, ?> parameters) {
        RequestSpecification requestSpecification = given()
                .contentType(ContentType.JSON)
                .body(ofNullable(requestBody).orElse(NULL))
                .pathParams(parameters);

        log.info(LOG_MESSAGE, requestBody, baseURI + basePath + url, parameters);
        return requestSpecification.request(httpMethod, baseUrl + url);
    }

    public enum RequestTypes {
        GET("GET"),
        POST("POST"),
        PUT("PUT"),
        DELETE("DELETE"),
        PATCH("PATCH");
        private final String requestType;

        RequestTypes(String requestType) {
            this.requestType = requestType;
        }

        public String getValue() {
            return requestType;
        }
    }
}
