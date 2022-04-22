@employee
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
    When the 'DELETE' request is sent to the '/employee/{id}' endpoint with params:
      | id | <employeeId> |
    Then the status code is 200
    And employee with id <employeeId> is deleted from the repository

    Examples:
      | employeeId |
      | 108        |
      | 109        |