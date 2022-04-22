package ivan.rest.example.definitionSteps;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import ivan.rest.example.configuration.SpringIntegrationTestConfiguration;
import ivan.rest.example.model.Employee;
import ivan.rest.example.util.session.Session;
import ivan.rest.example.util.session.SessionKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class TestSessionSteps extends SpringIntegrationTestConfiguration {

    @Autowired
    protected Session session;

    @Given("the '{sessionKey}' variable is updated by Employee entity in test session")
    public void updateVariableInTestSession(SessionKey sessionKey, Employee employee){
        session.checkIfExist(sessionKey);
        session.put(sessionKey, employee);
    }

    @ParameterType(".*")
    public SessionKey sessionKey(String sessionKey) {
        return SessionKey.valueOf(sessionKey);
    }
}
