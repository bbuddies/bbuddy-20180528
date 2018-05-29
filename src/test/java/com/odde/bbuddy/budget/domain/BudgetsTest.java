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
    public void queryBudgetWithNullBudgets() {
        GivenBudgets();
        int sum = budgets.queryBudgets(LocalDate.parse("2018-05-01"), LocalDate.parse("2018-05-31"));
        assertEquals(0, sum);
    }


    @Test
    public void queryBudgetWithEarlyStartDate() {
        int sum = budgets.queryBudgets(LocalDate.parse("2018-04-01"), LocalDate.parse("2018-03-01"));
        assertEquals(0, sum);
    }

    @Test
    public void queryBudgetWithSameMonth() {
        GivenBudgets(new Budget("2018-4", 300));
        int sum = budgets.queryBudgets(LocalDate.parse("2018-04-01"), LocalDate.parse("2018-04-01"));
        assertEquals(10, sum);
    }

    @Test
    public void queryBudgetWithTwoMonths() {
        GivenBudgets(new Budget("2018-5", 310),
                new Budget("2018-6", 300));
        int sum = budgets.queryBudgets(LocalDate.parse("2018-05-01"), LocalDate.parse("2018-06-10"));
        assertEquals(410, sum);
    }

    @Test
    public void queryBudgetWithMoreThanTwoMonths() {
        GivenBudgets(new Budget("2018-5", 310),
                    new Budget("2018-6", 300),
                    new Budget("2018-7", 310));
        int sum = budgets.queryBudgets(LocalDate.parse("2018-05-15"), LocalDate.parse("2018-07-15"));
        assertEquals(620, sum);
    }

    @Test
    public void queryBudgetWithRandomMonths() {
        GivenBudgets(new Budget("2018-5", 310),
                     new Budget("2018-9", 300));
        int sum = budgets.queryBudgets(LocalDate.parse("2018-05-15"), LocalDate.parse("2018-12-04"));
        assertEquals(470, sum);
    }

    private void GivenBudgets(Budget... budgets) {
        when(stubRepo.findAll()).thenReturn(Arrays.asList(budgets));
    }
}