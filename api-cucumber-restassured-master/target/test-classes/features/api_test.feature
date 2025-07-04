Feature: Login API Scenarios

  Scenario: Valid login with correct credentials
    Given I set the login API endpoint
    And I set request body with mobile "00959751346816" and password "123456"
    When I send the login POST request
    Then the response status code should be 200
    And the ResultCode should be 0
    And the ResultDescription should be "Transaction Successful"
    And I save the securetoken for reuse

  Scenario: Invalid login with wrong mobile
    Given I set the login API endpoint
    And I set request body with mobile "009597513468160" and password "123456"
    When I send the login POST request
    Then the response status code should be 200
    And the ResultCode should be 9
    And the ResultDescription should be "Client Not Found"

  Scenario: Invalid login with wrong password
    Given I set the login API endpoint
    And I set request body with mobile "00959751346816" and password "1234567"
    When I send the login POST request
    Then the response status code should be 200
    And the ResultCode should be 60
    And the ResultDescription should be "Invalid PIN"
