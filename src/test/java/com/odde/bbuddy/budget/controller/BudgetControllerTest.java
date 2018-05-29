package com.odde.bbuddy.budget.controller;

import com.odde.bbuddy.budget.domain.Budgets;
import com.odde.bbuddy.budget.repo.Budget;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BudgetControllerTest {

    Budgets mockBudgets = mock(Budgets.class);
    BudgetController controller = new BudgetController(mockBudgets);

    @Test
    public void addBudget() {
        assertEquals("/budgets/add", controller.addBudget());
    }

    @Test
    public void submitAddBudget() {
        controller.submitAddBudget(1000, "2017-09");

        ArgumentCaptor<Budget> captor = ArgumentCaptor.forClass(Budget.class);
        verify(mockBudgets).add(captor.capture());
        assertThat(captor.getValue()).isEqualToComparingFieldByField(new Budget("2017-09", 1000));

        //assertThat(controller.submitAddBudget("2000", "12").instanceof(SuccessPostActions));
    }
}