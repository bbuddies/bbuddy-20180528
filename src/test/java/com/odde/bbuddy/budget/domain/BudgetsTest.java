package com.odde.bbuddy.budget.domain;

import com.odde.bbuddy.budget.repo.Budget;
import com.odde.bbuddy.budget.repo.BudgetRepo;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BudgetsTest {
    BudgetRepo stubRepo = mock(BudgetRepo.class);
    Budgets budgets = new Budgets(stubRepo);

    @Test
    public void no_budget() {
        givenBudgets();

        assertEquals(0, getSum("2018-04-01", "2018-04-01"));
    }

    @Test
    public void start_and_end_are_same_day() {
        givenBudgets(budget("2018-05", 310));

        assertEquals(10, getSum("2018-05-01", "2018-05-01"));
    }

    @Test
    public void start_and_end_are_in_same_month_with_budget() {
        givenBudgets(budget("2018-05", 310));

        assertEquals(80, getSum("2018-05-05", "2018-05-12"));
    }

    @Test
    public void start_is_before_first_day_of_month() {
        givenBudgets(budget("2018-05", 310));

        assertEquals(100, getSum("2018-04-10", "2018-05-10"));
    }

    @Test
    public void end_is_before_first_day_of_month() {
        givenBudgets(budget("2018-05", 310));

        assertEquals(0, getSum("2018-04-10", "2018-04-20"));
    }

    @Test
    public void end_is_after_last_day_of_month() {
        givenBudgets(budget("2018-05", 310));

        assertEquals(220, getSum("2018-05-10", "2018-06-20"));
    }

    @Test
    public void start_is_after_last_day_of_month() {
        givenBudgets(budget("2018-05", 310));

        assertEquals(220, getSum("2018-05-10", "2018-06-20"));
    }

    @Test
    public void two_months() {
        givenBudgets(
                budget("2018-05", 310),
                budget("2018-06", 300));

        assertEquals(220 + 200, getSum("2018-05-10", "2018-06-20"));
    }

    @Test
    public void three_months() {
        givenBudgets(
                budget("2018-05", 310),
                budget("2018-06", 300),
                budget("2018-07", 310));

        assertEquals(220 + 300 + 180, getSum("2018-05-10", "2018-07-18"));
    }

    @Test
    public void three_months_but_missing_one_month_in_middle() {
        givenBudgets(
                budget("2018-05", 310),
                budget("2018-07", 310));

        assertEquals(220 + 150, getSum("2018-05-10", "2018-07-15"));
    }

    @Test
    public void different_year() {
        givenBudgets(
                budget("2017-12", 310),
                budget("2018-01", 310));

        assertEquals(220 + 150, getSum("2017-12-10", "2018-01-15"));
    }

    @Test
    public void start_is_after_end() {
        givenBudgets(
                budget("2018-07", 310));

        assertEquals(0, getSum("2018-07-10", "2018-07-06"));
    }

    private Budget budget(final String month, final int amount) {
        return new Budget() {{
            setMonth(month);
            setAmount(amount);
        }};
    }

    private int getSum(String start, String end) {
        return budgets.queryBudgetSum(LocalDate.parse(start), LocalDate.parse(end));
    }

    private void givenBudgets(Budget... budgets) {
        when(stubRepo.findAll()).thenReturn(Arrays.asList(budgets));
    }

}