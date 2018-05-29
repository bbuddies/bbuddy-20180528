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
    public void queryBudgetSumWithSameDay() {

        when(stubRepo.findAll()).thenReturn(Arrays.asList());

        int sum = budgets.queryBudgetSum(LocalDate.parse("2018-04-01"), LocalDate.parse("2018-04-01"));

        assertEquals(0, sum);
    }

    @Test
    public void queryBudgetSumWithDiffMonths() {
        when(stubRepo.findAll()).thenReturn(Arrays.asList(new Budget(){{
            setMonth("2018-5");
            setAmount(310);
        }}));

        int sum = budgets.queryBudgetSum(LocalDate.parse("2018-05-01"), LocalDate.parse("2018-05-01"));

        assertEquals(10, sum);
    }

    @Test
    public void query
}