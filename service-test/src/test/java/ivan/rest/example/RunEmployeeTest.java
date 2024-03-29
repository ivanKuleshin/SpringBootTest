package ivan.rest.example;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

// mvn clean test -Dcucumber.filter.tags="@employee"
// to run this class with different tags

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"ivan.rest.example.definitionSteps", "ivan.rest.example.configuration"},
    tags = "(@employee or @test) and not @ignore")
public class RunEmployeeTest {

}
