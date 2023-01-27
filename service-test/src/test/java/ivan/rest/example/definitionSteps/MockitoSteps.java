package ivan.rest.example.definitionSteps;

import io.cucumber.java.en.Given;
import ivan.rest.example.client.ExternalClient;
import ivan.rest.example.model.Address;
import ivan.rest.example.test.utils.TestUtil;
import ivan.rest.example.test.utils.session.Session;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import static ivan.rest.example.test.utils.session.SessionKey.EXPECTED_MOCK_ADDRESS;

public class MockitoSteps {

  @Autowired ExternalClient externalClient;
  @Autowired protected Session session;

  @Given("expected behaviour was set for ExternalClient.getEmployeeHash with '{int}' employeeId and expected {string} hash")
  public void setExpectedBehaviourForGetEmployeeHash(Integer employeeId, String expectedHash) {

//    Mockito.when(externalClient.getEmployeeHash(employeeId)).thenReturn(expectedHash);
    Mockito.doReturn(expectedHash).when(externalClient).getEmployeeHash(employeeId);
  }

  @Given("expected behaviour was set for ExternalClient.addAddress with '{int}' employeeId and Address entry:")
  public void setExpectedBehaviourForAddAddress(Integer employeeId, Address address) {
    Address expectedMockAddress =
        TestUtil.castMapToObject(session.get(EXPECTED_MOCK_ADDRESS), Address.class);

    Mockito.doReturn(expectedMockAddress).when(externalClient).addAddress(employeeId, address);
  }

  @Given("reset all mocks")
  public void resetAllMocks() {
    Mockito.reset(externalClient);
  }
}
