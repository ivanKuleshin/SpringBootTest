@employee
@resetWireMock
Feature: Delete Employee by ID
  This feature contains BDD scenarios for Get Employee from the repository

  Scenario Outline: 01 | Delete employee by id - happy path
    Given employees added to Employee rest service repository:
      | id  | name | passportNumber | education  |
      | 108 | Tom  | TM123456       | University |
      | 109 | Sam  | SM456789       | College    |
      | 110 | John | JN789123       | School     |
      | 111 | John | JN789123       | School     |
    And the entity is deleted from 'EXPECTED_RESULT' list by id = <employeeId> in test session

#    configure WireMock
    And the 'STUB_REQUEST' variable is initialized in test session with "null" value
    And the 'STUB_RESPONSE' variable is initialized in test session with "null" value
    And wiremock stub is set for DELETE request with "/externalClient/<employeeId>" URL

    And the 'STUB_REQUEST' variable is initialized in test session with 'null' value
    And the 'STUB_RESPONSE' variable is created in test session
      | employeeHashValue | 12345 |
    And wiremock stub is set for GET request with "/externalClient/" URL

    When the 'DELETE' request is sent to the '/employee/{id}' endpoint with params:
      | id | <employeeId> |
    Then the status code is 200
    And wiremock stub received DELETE request with "/externalClient/<employeeId>" URL
    And employee with id <employeeId> is deleted from the repository

    Examples:
      | employeeId |
      | 108        |
      | 109        |