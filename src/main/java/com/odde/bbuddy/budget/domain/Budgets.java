package com.odde.bbuddy.budget.domain;

import com.odde.bbuddy.budget.repo.Budget;
import com.odde.bbuddy.budget.repo.BudgetRepo;
import com.odde.bbuddy.common.callback.PostActions;
import com.odde.bbuddy.common.validator.FieldCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import static com.odde.bbuddy.common.callback.PostActionsFactory.failed;
import static com.odde.bbuddy.common.callback.PostActionsFactory.success;

@Service
public class Budgets implements FieldCheck<String> {
    private final BudgetRepo budgetRepo;

    @Autowired
    public Budgets(BudgetRepo budgetRepo) {this.budgetRepo = budgetRepo; }

    public PostActions add(Budget budget) {
        try {
            budgetRepo.save(budget);
            return success();
        } catch (IllegalArgumentException e) {
            return failed();
        }
    }

    @Override
    public boolean isValueUnique(String month) {return !budgetRepo.existsByName(month);}

    public int queryBudgets(LocalDate start, LocalDate end) {
        if (end.isBefore(start)) {
            return 0;
        }

        String startString = start.getYear() + "-" + start.getMonthValue();
        String endString = end.getYear() + "-" + end.getMonthValue();

        List<Budget> budgets = new ArrayList<>();

        for (Budget budget : budgetRepo.findAll()) {
            String month = budget.getMonth();
            String[] yearAndMonth = month.split("-");
            if(yearAndMonth.length != 2) {
                break;
            }
            int y = Integer.parseInt(yearAndMonth[0]);
            int m = Integer.parseInt(yearAndMonth[1]);
            if (!isBefore(y, m, start.getYear(), start.getMonthValue())
                    && !isBefore(end.getYear(), end.getMonthValue(), y, m)) {
               budgets.add(budget);
            }
        }

        int sum = 0;
        for (Budget budget : budgets) {
           int amount = budget.getAmount();
           sum += amount;
           if (budget.getMonth().equals(startString)) {
               sum -= beforeAmount(start, amount);
           }
           if (budget.getMonth().equals(endString)) {
               sum -= lastAmount(end, amount);
           }
        }

        return sum;
    }

    private boolean isBefore(int y1, int m1, int y2, int m2) {
        if (y1 < y2) {
            return true;
        }

        if (m1 < m2) {
            return true;
        }

        return false;
    }

    private int beforeAmount(LocalDate start, int amount) {
        int between = start.getDayOfMonth() - 1;
        int daysOfMonth = start.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
        return between * amount / daysOfMonth;
    }

    private int lastAmount(LocalDate end, int amount) {
        int daysOfMonth = end.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
        int between = daysOfMonth - end.getDayOfMonth();
        return between * amount / daysOfMonth;
    }
}

