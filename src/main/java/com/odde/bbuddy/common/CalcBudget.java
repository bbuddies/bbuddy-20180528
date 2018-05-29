package com.odde.bbuddy.common;



import ca.digitalcave.moss.common.DateUtil;
import com.odde.bbuddy.budget.repo.Budget;
import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import jersey.repackaged.com.google.common.collect.ImmutableCollection;
import jersey.repackaged.com.google.common.collect.UnmodifiableIterator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;


public class CalcBudget {

    Set<Budget> _budgets;
    public CalcBudget(Set<Budget> budget)
    {
        _budgets = budget;
    }

    public double GetBudget(LocalDate start, LocalDate end) {
        long during = DAYS.between(start,end) + 1;
     if(during <0) {
         throw new RuntimeException("Invalid Input date");
     }
     else if(during == 0)
     {
         return 0d;
     }

     Double totalBudget= 0.0d;

        HashMap<String,Double> budget = ConvertBudgetToDay();
     Set<String> duringDays = new HashSet<String>();

        for (int i = 0; i <during ; i++) {
            LocalDate current = start.plusDays(i);
            totalBudget+= budget.getOrDefault(current.format(DateTimeFormatter.ISO_DATE),0d);
        }

        return totalBudget;
    }

    public HashMap<String,Double> ConvertBudgetToDay()
    {
        HashMap<String, Double> map = new HashMap<String, Double>();

        Set<Budget> budgets = _budgets;
        for (Budget budget :
             budgets) {
        LocalDate month = LocalDate.parse(budget.getMonth() +"-01");
        //assume month is first date of current month.
        int days = month.lengthOfMonth();
        double dayPerBudget = budget.getAmount() / days;
            for (int i = 0; i <days ; i++) {
                LocalDate current = month.plusDays(i);
                map.put(current.format(DateTimeFormatter.ISO_DATE),dayPerBudget);
            }
        }

        return map;
    }

}
