package ivan.rest.example.clients;

import io.restassured.response.Response;
import ivan.rest.example.exceptions.TestExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

import static io.restassured.RestAssured.*;
import static ivan.rest.example.configuration.SpringIntegrationTestConfiguration.baseUrl;

@Slf4j
@Component
public class RestClient {

    public Response sendRequestWithParams(RequestTypes requestType, String url, Map<String, String> requestParams) {
        return sendRequestForHttpMethod(requestType, url, requestParams);
    }

//    public Response sendRequestWithParams(RequestTypes requestType, String url, List<Object> requestParams, String paramName){
//        return sendRequestForHttpMethod(requestType, url, requestParams, paramName);
//    }

    public Response sendRequestWithoutParams(RequestTypes requestType, String url) {
        return sendRequestForHttpMethod(requestType, url, null);
    }

    private Response sendRequestForHttpMethod(RequestTypes requestType, String url, Map<String, String> parameters) {
        Response response;

        switch (requestType) {
            case GET:
                response = when().get(baseUrl + url);
                log.info("[GET] request was sent for URI: {}", baseURI + basePath + url);
                break;
            case PUT:
                response = given().formParams(parameters).when().put(baseUrl + url);
                log.info("[PUT] request was sent for URI: {}", baseURI + basePath + url);
                break;
            case PATCH:
                response = given().header("Content-Type", "application/json").body(parameters).patch(baseUrl + url);
                break;
            case DELETE:
                response = when().delete(baseUrl + url);
                log.info("[DELETE] request was sent for URI: {}", baseURI + basePath + url);
                break;
            default:
                throw new TestExecutionException("Invalid HTTP method received!");
        }

        return response;
    }

//    private Response sendRequestForHttpMethod(RequestTypes requestType, String url, List<Object> parameters, String name) {
//        Response response;
//
//        switch (requestType) {
//            case PUT:
//                response = given().param(name, parameters).put(baseUrl + url);
//                log.info("[PUT] request was sent for URI: {}", baseURI + basePath + url);
//                break;
//            default:
//                throw new TestExecutionException("Invalid HTTP method received!");
//        }
//
//        return response;
//    }

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
