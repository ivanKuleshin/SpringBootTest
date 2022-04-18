@smoke
Feature: Employee rest service test feature 2
  This feature contains BDD scenarios for Employee rest service

  Scenario: Get all employees test scenario 2
    Given employees added to Employee rest service repository:
      | id  | name | passportNumber | education  |
      | 115 | Tom  | TM123456       | University |
      | 116 | Sam  | SM456789       | College    |
      | 117 | John | JN789123       | School     |
    When we send 'GET' request to the '/employee' endpoint
    Then retrieved data is equal to added data

  Scenario: Get all employees with parameters omitted 2
    Given employees added to Employee rest service repository:
      | id  | name | passportNumber | education  |
      | 118 |      | TM123456       | University |
      | 119 | Sam  |                | College    |
      | 120 | John | JN789123       |            |
      | 121 | Max  | MX159753       | School     |
    When we send 'GET' request to the '/employee' endpoint
    Then retrieved data is equal to added data