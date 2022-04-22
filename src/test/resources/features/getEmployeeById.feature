@employee
Feature: Employee rest service test feature 1
  This feature contains BDD scenarios for Employee rest service

  Scenario: 01 | Get one employee by id
    Given employees added to Employee rest service repository:
      | id  | name | passportNumber | education  |
      | 108 | Tom  | TM123456       | University |
      | 109 | Sam  | SM456789       | College    |
      | 110 | John | JN789123       | School     |
    When the 'GET' request is sent to the '/employee' endpoint with params:
      | id | 108 |
    Then the status code is 200
    And retrieved data is equal to added data

