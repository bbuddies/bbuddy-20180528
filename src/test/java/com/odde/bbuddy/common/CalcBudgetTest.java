package com.odde.bbuddy.common;

import com.odde.bbuddy.budget.repo.Budget;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class CalcBudgetTest {

    private CalcBudget calcBudget;

    public Set<Budget> GetBudgets() {
        Budget bd;

        Set<Budget> budgets = new HashSet<>();

        bd = new Budget();
        bd.setMonth("2017-02");
        bd.setAmount(10000d);
        budgets.add(bd);

        bd = new Budget();
        bd.setMonth("2017-12");
        bd.setAmount(10000d);
        budgets.add(bd);


        bd = new Budget();
        bd.setMonth("2018-05");
        bd.setAmount(10000d);
        budgets.add(bd);

        bd = new Budget();
        bd.setMonth("2018-07");
        bd.setAmount(10000d);
        budgets.add(bd);

        bd = new Budget();
        bd.setMonth("2018-08");
        bd.setAmount(10000d);
        budgets.add(bd);

        return budgets;
    }

    @Test
    public void query_whole_month() {
        givenBudgets(new Budget() {{
            setMonth("2018-05");
            setAmount(10000d);
        }});

        assertBudget(10000d, "2018-05-01", "2018-05-31");
    }

    private void assertBudget(double expected, String start, String end) {
        Double budget = calculate(start, end);

        assertEquals(expected, budget, 0.001d);
    }

    private void givenBudgets(Budget... budgetList) {
        Set<Budget> budgets = new HashSet<>(Arrays.asList(budgetList));
        calcBudget = new CalcBudget(budgets);
    }

    private Double calculate(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return calcBudget.GetBudget(start, end);
    }


    @Test
    public void query_missing_budget() {
        givenBudgets(
                new Budget() {{
                    setMonth("2018-05");
                    setAmount(10000d);
                }},
                new Budget() {{
                    setMonth("2018-07");
                    setAmount(10000d);
                }}
        );

        assertBudget(20000d, "2018-04-01", "2018-08-31");
    }

    @Test(expected = RuntimeException.class)
    public void TestEndEarlyStart() {
        Set<Budget> budgets = GetBudgets();
        CalcBudget c = new CalcBudget(budgets);

        LocalDate start = LocalDate.of(2018, 4, 1);
        LocalDate end = LocalDate.of(2017, 8, 31);
        Double budget = c.GetBudget(start, end);

        fail();
    }

    @Test
    public void TestStartEqualsEnd() {
        Set<Budget> budgets = GetBudgets();
        CalcBudget c = new CalcBudget(budgets);
        LocalDate start = LocalDate.of(2018, 5, 1);
        LocalDate end = start;
        Double budget = c.GetBudget(start, end);

        assertEquals(322.5806, budget, 0.001d);
    }

    @Test
    public void TestStartEndWithInMonth() {
        Set<Budget> budgets = GetBudgets();
        CalcBudget c = new CalcBudget(budgets);
        LocalDate start = LocalDate.of(2018, 7, 1);
        LocalDate end = LocalDate.of(2018, 7, 16);
        Double budget = c.GetBudget(start, end);

        assertEquals(5161.2903, budget, 0.001d);
    }

    @Test
    public void TestStartRenYear() {
        Set<Budget> budgets = GetBudgets();
        CalcBudget c = new CalcBudget(budgets);
        LocalDate start = LocalDate.of(2017, 2, 1);
        LocalDate end = LocalDate.of(2018, 10, 15);
        Double budget = c.GetBudget(start, end);

        assertEquals(50000d, budget, 0.001d);
    }
}