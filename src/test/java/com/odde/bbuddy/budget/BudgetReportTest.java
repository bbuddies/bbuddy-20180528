package com.odde.bbuddy.budget;

import org.junit.Test;
import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.*;

public class BudgetReportTest {

    private List<BudgetItem> budgetItems;
    private LocalDate beginDate = LocalDate.of(2018,1,10);
    private LocalDate endDate = LocalDate.of(2018,4,20);

    @Test
    public void total_amount_skip(){

        beginDate = LocalDate.parse("2018-01-10");
        endDate = LocalDate.parse("2018-04-20");
        budgetItems = new ArrayList<>();
        budgetItems.add(new BudgetItem(2018,1,310));
        budgetItems.add(new BudgetItem(2018,3,200));

        BudgetReport  budgetReport = new BudgetReport();
        assertEquals(budgetReport.getTotalBudget(beginDate, endDate, budgetItems), 420, 0.001d);

    }


    @Test
    public void total_amount_no_budget_in_first_month(){
        beginDate = LocalDate.parse("2018-01-10");
        endDate = LocalDate.parse("2018-04-20");
        budgetItems = new ArrayList<>();
        budgetItems.add(new BudgetItem(2018,2,100));
        budgetItems.add(new BudgetItem(2018,3,100));
        budgetItems.add(new BudgetItem(2018,4,300));
        BudgetReport  budgetReport = new BudgetReport();
        assertEquals(budgetReport.getTotalBudget(beginDate, endDate, budgetItems), 400, 0.001d);

    }


    @Test
    public void total_amount_no_budget_in_first_and_last_month(){
        beginDate = LocalDate.parse("2018-01-10");
        endDate = LocalDate.parse("2018-04-20");
        budgetItems = new ArrayList<>();
        budgetItems.add(new BudgetItem(2018,2,100));
        budgetItems.add(new BudgetItem(2018,3,100));
        BudgetReport  budgetReport = new BudgetReport();
        assertEquals(budgetReport.getTotalBudget(beginDate, endDate, budgetItems), 200, 0.001d);

    }



    @Test
    public void total_amount_have_budget_in_every_month(){
        beginDate = LocalDate.parse("2018-01-10");
        endDate = LocalDate.parse("2018-04-20");
        budgetItems = new ArrayList<>();
        budgetItems.add(new BudgetItem(2018,1,310));
        budgetItems.add(new BudgetItem(2018,2,100));
        budgetItems.add(new BudgetItem(2018,3,100));
        budgetItems.add(new BudgetItem(2018,4,300));

        BudgetReport  budgetReport = new BudgetReport();
        assertEquals(budgetReport.getTotalBudget(beginDate, endDate, budgetItems), 620, 0.001d);

    }

    @Test
    public void total_amount_cross_year(){
        beginDate = LocalDate.parse(("2017-12-10"));
        endDate = LocalDate.parse("2018-04-20");
        budgetItems = new ArrayList<>();
        budgetItems.add(new BudgetItem(2017,12,310));
        budgetItems.add(new BudgetItem(2018,1,310));
        budgetItems.add(new BudgetItem(2018,2,100));
        budgetItems.add(new BudgetItem(2018,3,100));
        budgetItems.add(new BudgetItem(2018,4,300));

        BudgetReport  budgetReport = new BudgetReport();
        assertEquals(budgetReport.getTotalBudget(beginDate, endDate, budgetItems), 930, 0.001d);

    }

}