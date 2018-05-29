Feature: Budget

  Scenario: Add a new budget
    When add a budget with month "2018-05" and amount 500
    Then the following budget will be displayed
    | month   | amount |
    | 2018-05 | 500    |