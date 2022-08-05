package ivan.rest.example.clients;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import ivan.rest.example.exceptions.TestExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

import static io.restassured.RestAssured.*;
import static ivan.rest.example.configuration.SpringIntegrationTestConfiguration.baseUrl;

@Slf4j
@Component
public class RestClient {

    public Response sendRequestWithParams(RequestTypes requestType, String url, Map<String, ?> requestParams) {
        return sendRequestForHttpMethodWithParams(requestType, url, requestParams);
    }

    public Response sendRequestWithoutParams(RequestTypes requestType, String url) {
        return sendRequestForHttpMethodWithParams(requestType, url, Map.of());
    }

    public Response sendRequestWithBody(RequestTypes requestType, String url, Object requestBody) {
        return sendRequestForHttpMethodWithBody(requestType, url, requestBody);
    }

    private Response sendRequestForHttpMethodWithParams(RequestTypes requestType, String url, Map<String, ?> parameters) {
        Response response;
        RequestSpecification requestSpecification = given().pathParams(parameters);

        switch (requestType) {
            case GET:
                response = requestSpecification.get(baseUrl + url);
                log.info("[GET] request was sent for URI: {}\nWith url parameter: {}", baseURI + basePath + url, parameters);
                break;
            case PUT:
                response = requestSpecification.put(baseUrl + url);
                log.info("[PUT] request was sent for URI: {}\nWith url parameter: {}", baseURI + basePath + url, parameters);
                break;
            case PATCH:
                response = requestSpecification.patch(baseUrl + url);
                log.info("[PATCH] request was sent for URI: {}\nWith url parameter: {}", baseURI + basePath + url, parameters);
                break;
            case DELETE:
                response = requestSpecification.delete(baseUrl + url);
                log.info("[DELETE] request was sent for URI: {}\nWith url parameter: {}", baseURI + basePath + url, parameters);
                break;
            default:
                throw new TestExecutionException("Invalid HTTP method received!");
        }
        return response;
    }

    private Response sendRequestForHttpMethodWithBody(RequestTypes requestType, String url, Object requestBody) {
        Response response;
        RequestSpecification requestSpecification = given().header("Content-Type", "application/json").body(requestBody);

        switch (requestType) {
            case GET:
                response = requestSpecification.get(baseUrl + url);
                log.info("[GET] request was sent for URI: {}\n With body: {}", baseURI + basePath + url, requestBody);
                break;
            case PUT:
                response = requestSpecification.put(baseUrl + url);
                log.info("[PUT] request was sent for URI: {}\n With body: {}", baseURI + basePath + url, requestBody);
                break;
            case PATCH:
                response = requestSpecification.body(requestBody).patch(baseUrl + url);
                log.info("[PATCH] request was sent for URI: {}\n With body: {}", baseURI + basePath + url, requestBody);
                break;
            case POST:
                response = requestSpecification.body(requestBody).post(baseUrl + url);
                log.info("[POST] request was sent for URI: {}\n With body: {}", baseURI + basePath + url, requestBody);
                break;
            case DELETE:
                response = requestSpecification.delete(baseUrl + url);
                log.info("[DELETE] request was sent for URI: {}\n With body: {}", baseURI + basePath + url, requestBody);
                break;
            default:
                throw new TestExecutionException("Invalid HTTP method received!");
        }
        return response;
    }

    public enum RequestTypes {
        GET("get"),
        POST("post"),
        PUT("put"),
        DELETE("delete"),
        PATCH("patch");
        private final String requestType;

        RequestTypes(String requestType) {
            this.requestType = requestType;
        }

        public String getValue() {
            return requestType;
        }
    }
}
