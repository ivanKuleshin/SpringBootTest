package ivan.rest.example.spring;

import ivan.rest.example.EmployeeRestServiceNoDbApplication;
import ivan.rest.example.controller.EmployeeController;
import ivan.rest.example.util.session.Session;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import javax.annotation.PostConstruct;

//The @SpringBootTest annotation tells Spring Boot to look for a main configuration class (one with @SpringBootApplication, for instance)
// and use that to start a Spring application context
// RestTemplate already knows about host and port
// *for integration tests*
@SpringBootTest(classes = EmployeeRestServiceNoDbApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ContextConfiguration
//To make Cucumber aware of your test configuration you can annotate a configuration class with @CucumberContextConfiguration
@CucumberContextConfiguration
public abstract class SpringIntegrationTest {

    protected String baseUrl;

    @Autowired
    protected TestRestTemplate restTemplate;
    @Autowired
    protected Session session;
    @Autowired
    protected EmployeeController controller;

    @LocalServerPort
    private int port;
    @Value("${employee.service.host}")
    private String employeeServiceHost;

    @PostConstruct
    private void init() {
        baseUrl = String.format("http://%s:%s", employeeServiceHost, port);
    }
}
