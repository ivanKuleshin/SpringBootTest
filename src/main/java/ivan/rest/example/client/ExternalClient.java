package ivan.rest.example.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Optional;

import static io.restassured.RestAssured.given;

@Component
public class ExternalClient {

    @Value("${clients.externalClient.url}")
    private String url;

    public void deleteRequestToExternalService(Integer id) {
        given().log().all().delete(url + id).then().statusCode(200);
    }

    public void getAllEmployees(){
        given().log().all().get(url).then().statusCode(200);
    }

    public String getEmployeeHash(Integer employeeId) {
        String hash = given().log().all().get(url + employeeId).jsonPath().getString("employeeHashValue");

        return Optional.ofNullable(hash).orElse("").isEmpty() ? null : employeeId + hash;
    }
}
