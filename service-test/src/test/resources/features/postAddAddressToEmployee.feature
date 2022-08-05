@employee
Feature: Add Employee Address into repository
  This feature contains BDD scenarios for Add Employee Address into the repository

  Scenario Outline: 01.1 | Add employee address into the repository - happy path
    Given employees added to Employee rest service repository:
      | id   | name   | passportNumber   | education   |
      | <id> | <name> | <passportNumber> | <education> |
    And the 'EXPECTED_RESULT' variable is updated by Employee entity in test session
      | id   | name    | passportNumber   | education     | address.city | address.country | address.zip |
      | <id> | <name>  | <passportNumber> | <education>   | <city>       | <country>       | <zip>       |

#    configure WireMock
    And the 'STUB_REQUEST' variable is created in test session
      | city    | <city>    |
      | country | <country> |
      | zip     | <zip>     |
    And the 'STUB_RESPONSE' variable is created in test session
      | city    | <city>    |
      | country | <country> |
      | zip     | <zip>     |
    And wiremock stub is set for POST request with "/externalClient/address/<id>" URL

    When the 'POST' request is sent to the '/employee/address/<id>' endpoint with body
      | city   | country    | zip   |
      | <city> | <country>  | <zip> |
    Then the status code is 200
    And retrieved data is equal to added data

    Examples:
       | id   | name    | passportNumber   | education     | city  | country    | zip |
       | 108  | Tom     | TM123456         | University    | Kyiv  | Ukraine    | 044 |

  Scenario Outline: 01.2 | Add employee address into the repository while address is already exists - negative path
    Given employees added to Employee rest service repository:
      | id   | name   | passportNumber   | education   | address.city | address.country | address.zip |
      | <id> | <name> | <passportNumber> | <education> | <city>       | <country>       | <zip>       |
    When the 'POST' request is sent to the '/employee/address/<id>' endpoint with body
      | city   | country   | zip   |
      | <city> | <country> | <zip> |
    Then the status code is 500
    And error message contains: "Employee already has an address. Please use update instead of create."

    Examples:
      | id  | name | passportNumber | education  | city | country | zip |
      | 108 | Tom  | TM123456       | University | Kyiv | Ukraine | 044 |