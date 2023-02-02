@employee
Feature: External CLient Mockito Test
  This feature contains BDD scenarios for ExternalClient Mock Test

  Background:
    Given reset all mocks

  Scenario Outline: 1 | Positive: Mock getEmployeeHash method while call to getEmployeeById endpoint
    Given expected behaviour was set for ExternalClient.getEmployeeHash with '<employeeId>' employeeId and expected "<expectedHash>" hash

    And employees added to Employee rest service repository:
      | id  | name | passportNumber | education  |
      | 108 | Tom  | TM123456       | University |
      | 109 | Sam  | SM456789       | College    |
      | 110 | John | JN789123       | School     |
      | 111 | John | JN789123       | School     |
    And the 'EXPECTED_RESULT' variable is updated by Employee entity in test session
      | id  | name | passportNumber | education  | employeeHash   |
      | 108 | Tom  | TM123456       | University | <expectedHash> |

    When the GET request is sent to the '/employee/{id}' endpoint with params:
      | id | <employeeId> |

    Then the status code is 200
    And retrieved data is equal to added data

    Examples:
      | employeeId | expectedHash |
      | 108        | TestHash     |

  Scenario Outline: 2 | Positive: Mock addAddress method while call to postAddAddressToEmployee endpoint
    Given the Address 'EXPECTED_MOCK_ADDRESS' variable is created in test session from "expectedMockAddress" file with "external-client-mock-test" template
    And expected behaviour was set for ExternalClient.addAddress with '<id>' employeeId and Address entry:
      | city   | country   | zip   |
      | <city> | <country> | <zip> |

    And employees added to Employee rest service repository:
      | id   | name   | passportNumber   | education   |
      | <id> | <name> | <passportNumber> | <education> |
    And the Employee 'EXPECTED_RESULT' variable is created in test session from "employeeWithAddress" file with "add-address-to-employee" template
    And the 'EXPECTED_RESULT' variable is updated by Employee entity in test session
      | id   | name   | passportNumber   | education   | address.city   | address.country   | address.zip   |
      | <id> | <name> | <passportNumber> | <education> | <expectedCity> | <expectedCountry> | <expectedZip> |

    When the POST request is sent to the '/employee/address/<id>' endpoint with body
      | city   | country   | zip   |
      | <city> | <country> | <zip> |
    Then the status code is 200
    And retrieved data is equal to added data

    Examples:
      | id  | name | passportNumber | education  | city | country | zip  | expectedCity | expectedZip | expectedCountry |
      | 108 | Tom  | TM123456       | University | Kyiv | Ukraine | Test | Zagreb       | Test123     | Croatia         |

  Scenario Outline: 3 | Negative: Mock addAddress method returns exception while call to postAddAddressToEmployee endpoint
    Given expected unsuccessful behaviour was set for ExternalClient.addAddress with '108' employeeId and Address entry:
      | city   | country   | zip   |
      | <city> | <country> | <zip> |

    And employees added to Employee rest service repository:
      | id   | name   | passportNumber   | education   |
      | <id> | <name> | <passportNumber> | <education> |
    And the Employee 'EXPECTED_RESULT' variable is created in test session from "employeeWithAddress" file with "add-address-to-employee" template
    And the 'EXPECTED_RESULT' variable is updated by Employee entity in test session
      | id   | name   | passportNumber   | education   | address.city   | address.country   | address.zip   |
      | <id> | <name> | <passportNumber> | <education> | <expectedCity> | <expectedCountry> | <expectedZip> |

    When the POST request is sent to the '/employee/address/<id>' endpoint with body
      | city   | country   | zip   |
      | <city> | <country> | <zip> |
    Then the status code is 500
    And error message contains: "Error during adding an address to Object"

    Examples:
      | id  | name | passportNumber | education  | city | country | zip  | expectedCity | expectedZip | expectedCountry |
      | 108 | Tom  | TM123456       | University | Kyiv | Ukraine | Test | Zagreb       | Test123     | Croatia         |