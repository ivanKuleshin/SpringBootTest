package ivan.rest.example.client;

import io.restassured.RestAssured;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ExternalClient {

    @Value("${clients.externalClient.url}")
    private String url;

    public void deleteRequestToExternalService(Integer id) {
        RestAssured.given().log().all().delete(url + id).then().statusCode(200);
    }
}
