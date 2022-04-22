@smoke
Feature: Update Employee by ID
  This feature contains BDD scenarios for Update Employee rest service requests

  Background:
    Given employees added to Employee rest service repository:
      | id  | name | passportNumber | education  |
      | 115 | Tom  | TM123456       | University |
      | 116 | Sam  | SM456789       | College    |
      | 117 | John | JN789123       | School     |

  Scenario Outline: Update employee by Id
    And the 'EXPECTED_RESULT' variable is updated by Employee entity in test session
      | id   | name    | passportNumber   | education     |
      | <id> | <name>  | <passportNumber> | <education>   |
    When the 'PATCH' request is sent to the '/employee/update' endpoint with params:
      | id             | <id>             |
      | passportNumber | <passportNumber> |
      | education      | <education>      |
      | name           | <name>           |
    Then the status code is 200
    And retrieved data is equal to added data

    Examples:
      | id  | name    | education | passportNumber |
      | 115 | testTom | College   | TM14852Test    |