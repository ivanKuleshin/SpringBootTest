package ivan.rest.example.definitionSteps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import ivan.rest.example.clients.RestClient;
import ivan.rest.example.clients.WireMockClient;
import ivan.rest.example.configuration.SpringIntegrationTestConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.management.ManagementFactory;

import static ivan.rest.example.clients.RestClient.RequestTypes.DELETE;

@Slf4j
public class TagHooks extends SpringIntegrationTestConfiguration {
    private static final int STATUS_OK = 200;

    @Autowired
    private WireMockClient wireMockClient;
    @Autowired
    private RestClient restClient;

    /**
     * We can use these type of annotations for perform some logic before some scenario or some feature
     */
    @Before("@resetWireMock")
    public void resetWireMock() {
        wireMockClient.resetWireMock();
        log.info("WireMock instance successfully resetted");
    }

    @Before(value = "@testData")
    public void setUp() {
        System.out.println("This methods performs only for the Scenarios with @testData");
    }

    @Before
    public void doSetup() {
        // Was added to test the parallel execution:
        long threadId = Thread.currentThread().getId();
        String processName = ManagementFactory.getRuntimeMXBean().getName();
        log.info(String.format("Started in thread: %s, in JVM: %s", threadId, processName));
    }

    @After
    public void cleanUp() {
        restClient.sendRequestWithoutParams(DELETE, "/employee").then().statusCode(STATUS_OK);
        session.clear();
    }
}

