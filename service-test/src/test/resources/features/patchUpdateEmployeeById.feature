@employee
Feature: Update Employee by ID
  This feature contains BDD scenarios for Update Employee in the repository

  Background:
    Given employees added to Employee rest service repository:
      | id  | name | passportNumber | education  |
      | 115 | Tom  | TM123456       | University |
      | 116 | Sam  | SM456789       | College    |
      | 117 | John | JN789123       | School     |

  Scenario Outline: 01.1 | Update employee by Id - happy path
    And the 'EXPECTED_RESULT' variable is updated by Employee entity in test session
      | id   | name    | passportNumber   | education     |
      | <id> | <name>  | <passportNumber> | <education>   |
    When the PATCH request is sent to the '/employee/update' endpoint with body
      | id    | passportNumber   | education   | name   |
      | <id>  | <passportNumber> | <education> | <name> |
    Then the status code is 200
    And retrieved data is equal to added data

    Examples:
      | id  | name    | education | passportNumber |
      | 115 | testTom | College   | TM14852Test    |

  Scenario: 01.2 | Update employee by invalid Id - negative path
    When the PATCH request is sent to the '/employee/update' endpoint with body
      | id    | passportNumber |
      |       | TM14852Test    |
    Then the status code is 500
    And error message contains: "id cannot be null"