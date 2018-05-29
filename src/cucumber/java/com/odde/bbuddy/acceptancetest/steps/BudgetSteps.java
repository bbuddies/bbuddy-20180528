package com.odde.bbuddy.acceptancetest.steps;

import com.odde.bbuddy.acceptancetest.data.budget.EditableBudget;
import com.odde.bbuddy.acceptancetest.driver.UiDriver;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.odde.bbuddy.acceptancetest.steps.AssertionHelper.assertListDeepEquals;

public class BudgetSteps {

    @Autowired
    private UiDriver driver;

    @When("^add budget with the following information$")
    public void add_budget_with_the_following_information(List<EditableBudget> budgets) throws Throwable {
        driver.navigateTo("/budgets/add");

        EditableBudget budget = budgets.get(0);
        driver.inputTextByName(budget.getAmount(), "amount");
        driver.inputTextByName(budget.getMonth(), "month");
        driver.clickByText("Add");
    }

    @Then("^the following budget will be created$")
    public void the_following_budget_will_be_created(List<EditableBudget> expected) throws Throwable {
        assertListDeepEquals(expected, budget)
    }

}