@employee
Feature: Get All Employees from the repository
  This feature contains BDD scenarios for Get All Employees from the repository

  Background:
    Given User sees that our controller is not NULL

  @testData
  Scenario: 01 | Get all employees - happy path
    Given employees added to Employee rest service repository:
      | id  | name | passportNumber | education  |
      | 108 | Tom  | TM123456       | University |
      | 109 | Sam  | SM456789       | College    |
      | 110 | John | JN789123       | School     |
    When the 'GET' request is sent to the '/employee' endpoint without params
    Then the status code is 200
    And retrieved data is equal to added data

  @testData
  Scenario: 02 | Get all employees with several parameters as null
    Given employees added to Employee rest service repository:
      | id  | name | passportNumber | education  |
      | 111 |      | TM123456       | University |
      | 112 | Sam  |                | College    |
      | 113 | John | JN789123       |            |
      | 114 | Max  | MX159753       | School     |
    When the 'GET' request is sent to the '/employee' endpoint without params
    Then the status code is 200
    And retrieved data is equal to added data

#  Scenario: 04 | Put one Employee to the rest service
#    Given Employee '10, Ivan, MX159753, University' added to Employee rest service repository