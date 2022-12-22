package main.java.ivan.rest.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

@SpringBootApplication
public class EmployeeRestServiceNoDbApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeRestServiceNoDbApplication.class, args);
    }

}
