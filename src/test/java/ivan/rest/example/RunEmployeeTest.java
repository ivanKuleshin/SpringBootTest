package ivan.rest.example;

import io.cucumber.spring.CucumberContextConfiguration;
import ivan.rest.example.spring.SpringIntegrationTest;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

//mvn clean test -Dcucumber.filter.tags="@employee"
//to run this class with different tags

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "ivan.rest.example.employeeSteps",
        tags = "@employee or @smoke")
public class RunEmployeeTest extends SpringIntegrationTest {
}
