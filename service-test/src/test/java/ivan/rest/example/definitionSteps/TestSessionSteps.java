package ivan.rest.example.definitionSteps;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import ivan.rest.example.configuration.SpringIntegrationTestConfiguration;
import ivan.rest.example.model.Employee;
import ivan.rest.example.utils.ReadFileUtils;
import ivan.rest.example.utils.session.Session;
import ivan.rest.example.utils.session.SessionKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ivan.rest.example.definitionSteps.EmployeeSteps.employeeListTypeReference;
import static ivan.rest.example.utils.TestUtil.convertValueToList;

@Slf4j
public class TestSessionSteps extends SpringIntegrationTestConfiguration {

    @Autowired
    protected Session session;

    @Given("the '{sessionKey}' variable is updated by Employee entity in test session")
    public void updateVariableInTestSessionByEmployee(SessionKey sessionKey, Employee employee) {
        session.checkIfExist(sessionKey);
        session.put(sessionKey, employee);
    }

    @Given("the '{sessionKey}' variable is created in test session")
    public void updateVariableInTestSession(SessionKey sessionKey, Map<String, String> map){
        session.put(sessionKey, map);
    }

    @Given("the '{sessionKey}' variable is created in test session from {string} file with {string} template")
    public void updateVariableInTestSession(SessionKey sessionKey, String fileName, String templateName){
        Employee employee = ReadFileUtils.getRequestAs(templateName, fileName, Employee.class);

        session.put(sessionKey, employee);
    }

    @Given("the '{sessionKey}' variable is initialized in test session with {string} value")
    public void initializeVariableInTestSession(SessionKey sessionKey, String value){
        session.put(sessionKey, value);
    }

    @Given("the entity is deleted from '{sessionKey}' list by id = {int} in test session")
    public void deleteEntityFromTestSessionByParam(SessionKey sessionKey, int paramValue) {

        List<Employee> updatedList = convertValueToList(session.get(sessionKey), employeeListTypeReference)
                .stream().filter(x -> x.getId() == paramValue).collect(Collectors.toList());

        session.put(sessionKey, updatedList);
    }

    @ParameterType(".*")
    public SessionKey sessionKey(String sessionKey) {
        return SessionKey.valueOf(sessionKey);
    }
}
