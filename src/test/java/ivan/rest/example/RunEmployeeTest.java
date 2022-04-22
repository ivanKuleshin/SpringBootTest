package ivan.rest.example;

import ivan.rest.example.configuration.SpringIntegrationTestConfiguration;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

//mvn clean test -Dcucumber.filter.tags="@employee"
//to run this class with different tags

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "ivan.rest.example.definitionSteps",
        tags = "@employee or @smoke")
public class RunEmployeeTest extends SpringIntegrationTestConfiguration {
}
