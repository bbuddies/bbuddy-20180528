package com.odde.bbuddy.acceptancetest.steps;

import com.odde.bbuddy.acceptancetest.driver.UiDriver;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

public class BudgetSteps {

    @Autowired
    UiDriver driver;

    @When("^add a budget with month \"([^\"]*)\" and amount (\\d+)$")
    public void add_a_budget_with_month_and_amount(String month, int amount) throws Throwable {
        driver.navigateTo("/budget/add");
        driver.inputTextByName(month, "month");
        driver.inputTextByName(String.valueOf(amount), "amount");
        driver.clickByText("save");

    }

    @Then("^the following budget will be displayed$")
    public void the_following_budget_will_be_displayed(DataTable dt) throws Throwable {

    }

}
