@employee
Feature: Employee rest service test feature 1
  This feature contains BDD scenarios for Employee rest service

  Scenario: Get all employees test scenario 1
    Given employees added to Employee rest service repository:
      | id  | name | passportNumber | education  |
      | 101 | Tom  | TM123456       | University |
      | 102 | Sam  | SM456789       | College    |
      | 103 | John | JN789123       | School     |
    When we send 'GET' request to the '/employee' endpoint
    Then retrieved data is equal to added data

  Scenario: Get all employees with parameters omitted 1
    Given employees added to Employee rest service repository:
      | id  | name | passportNumber | education  |
      | 101 |      | TM123456       | University |
      | 102 | Sam  |                | College    |
      | 103 | John | JN789123       |            |
      | 104 | Max  | MX159753       | School     |
    When we send 'GET' request to the '/employee' endpoint
    Then retrieved data is equal to added data