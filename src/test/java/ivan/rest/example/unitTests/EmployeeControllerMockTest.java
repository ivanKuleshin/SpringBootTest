package ivan.rest.example.unitTests;

import ivan.rest.example.controller.EmployeeController;
import ivan.rest.example.exceptions.TestExecutionException;
import ivan.rest.example.model.Employee;
import ivan.rest.example.service.EmployeeService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Random;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Launches only certain controller
 * The full Spring application context is started but without the server. We can perform the tests to only the web layer
 * for unit test
 *
 * @Autowired - by declaring all the bean dependencies in a Spring configuration file, the Spring container can autowire relationships between collaborating beans
 * We can use the @MockBean to add mock objects to the Spring application context. The mock will replace any existing bean of the same type in the application context.
 **/
@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(value = EmployeeController.class)
public class EmployeeControllerMockTest {

    @Autowired
    EmployeeController employeeController;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    EmployeeService employeeServiceMock;

    private final Employee employee = new Employee();

    private static final String EMPLOYEE_SUCCESSFULLY_ADDED = "Employee with id = %s has been added!";
    private static final String EMPLOYEES_SUCCESSFULLY_DELETED = "All employees have been successfully deleted!";

    @Test
    public void addEmployeeTest() {
        String actualResult = employeeController.add(employee);
        log.info("Actual result from \"{}\" method is: {}", "add", actualResult);

        Assert.assertEquals(String.format(EMPLOYEE_SUCCESSFULLY_ADDED, employee.getId()), actualResult);
        Mockito.verify(employeeServiceMock).add(employee);
        Mockito.verifyNoMoreInteractions(employeeServiceMock);
    }


    @Test
    public void deleteAllEmployeesViaRest() throws TestExecutionException {
        try {
            mockMvc.perform(delete("/employee"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(EMPLOYEES_SUCCESSFULLY_DELETED));
        } catch (Exception e) {
            throw new TestExecutionException(e.getMessage());
        }

        Mockito.verify(employeeServiceMock).clear();
    }

    @SneakyThrows
    @Test
    public void getEmployeeById() {
        employee.setId(new Random().nextInt(10));

        Mockito.doReturn(employee).when(employeeServiceMock).getById(anyInt());

        mockMvc.perform(get("/employee/" + employee.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(employee))
                .andExpect(jsonPath("$.id").value(employee.getId()));

        Mockito.verify(employeeServiceMock).getById(anyInt());
    }
}