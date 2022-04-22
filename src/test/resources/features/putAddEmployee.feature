@employee
Feature: Add Employee into repository
  This feature contains BDD scenarios for Add Employee into the repository

  Scenario Outline: 01.1 | Add employee into the repository - happy path
    Given the 'EXPECTED_RESULT' variable is created in test session
      | id             | <id>             |
      | passportNumber | <passportNumber> |
      | education      | <education>      |
      | name           | <name>           |
    When the 'PUT' request is sent to the '/employee' endpoint with body
      | id   | passportNumber   | education   | name   |
      | <id> | <passportNumber> | <education> | <name> |
    Then the status code is 200
    And employee with <id> ID has been added to the repository correctly

    Examples:
      | id  | name    | education | passportNumber |
      | 115 | testTom | College   | TM14852Test    |

  Scenario: 01.2 | Add employee into the repository when Employee is already exists
    Given employees added to Employee rest service repository:
      | id  | name | passportNumber | education  |
      | 108 | Tom  | TM123456       | University |
    When the 'PUT' request is sent to the '/employee' endpoint with body
      | id   | passportNumber   | education   | name   |
      | 108  | TM123456         | University  | Tom    |
    Then the status code is 500
    And error message contains: "Employee with such id = 108 already exists"
