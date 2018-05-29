@user @budget
Feature: Add Budget

  Scenario: successfully add budget
    When add budget with the following information
      | amount | month                 |
      | 20001  | 2019-6                |
    Then the following budget will be created
      | amount | month                 |
      | 20001  | 2019-5                |
