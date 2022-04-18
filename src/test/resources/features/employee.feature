@employee
Feature: Employee rest service test feature
  This feature contains BDD scenarios for Employee rest service

  Background:
    Given User sees that our controller is not NULL

  @testData
  Scenario: Get all employees test scenario
    Given employees added to Employee rest service repository:
      | id  | name | passportNumber | education  |
      | 108 | Tom  | TM123456       | University |
      | 109 | Sam  | SM456789       | College    |
      | 110 | John | JN789123       | School     |
    When we send 'GET' request to the '/employee' endpoint
    Then retrieved data is equal to added data

  @testData
  Scenario: Get all employees with parameters omitted
    Given employees added to Employee rest service repository:
      | id  | name | passportNumber | education  |
      | 111 |      | TM123456       | University |
      | 112 | Sam  |                | College    |
      | 113 | John | JN789123       |            |
      | 114 | Max  | MX159753       | School     |
    When we send 'GET' request to the '/employee' endpoint
    Then retrieved data is equal to added data

  Scenario:Get one employee by id
    Given employees added to Employee rest service repository:
      | id  | name | passportNumber | education  |
      | 108 | Tom  | TM123456       | University |
      | 109 | Sam  | SM456789       | College    |
      | 110 | John | JN789123       | School     |
    When we send 'GET' request to the '/employee' endpoint with 108 id
    Then retrieved data is equal to added data for specified id

    Scenario: Put one Employee to the rest service
      Given Employee 10, Ivan, MX159753, University added to Employee rest service repository