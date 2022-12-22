package test.java.ivan.rest.example;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import test.java.ivan.rest.example.configuration.SpringIntegrationTestConfiguration;

//mvn clean test -Dcucumber.filter.tags="@employee"
//to run this class with different tags

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "service-test/src/test/resources/features",
        glue = "test.java.ivan.rest.example.definitionSteps",
        tags = "@employee or @test")
public class RunEmployeeTest extends SpringIntegrationTestConfiguration {
}
