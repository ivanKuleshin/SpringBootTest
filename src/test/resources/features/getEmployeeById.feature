@employee
Feature: Get Employee by ID
  This feature contains BDD scenarios for Get Employee from the repository

  Scenario: 01 | Get one employee by id - happy path
    Given employees added to Employee rest service repository:
      | id  | name | passportNumber | education  |
      | 108 | Tom  | TM123456       | University |
      | 109 | Sam  | SM456789       | College    |
      | 110 | John | JN789123       | School     |
      | 111 | John | JN789123       | School     |
    When the 'GET' request is sent to the '/employee/{id}' endpoint with params:
      | id | 108 |
    Then the status code is 200
    And retrieved data is equal to added data

  Scenario: 02 | Get one employee by id - negative path
    Given employees added to Employee rest service repository:
      | id  | name | passportNumber | education  |
      | 108 | Tom  | TM123456       | University |
    When the 'GET' request is sent to the '/employee/{employeeId}' endpoint with params:
      | employeeId | 1111 |
    Then the status code is 500
    And error message contains: "Employee with such id = 1111 not found"

