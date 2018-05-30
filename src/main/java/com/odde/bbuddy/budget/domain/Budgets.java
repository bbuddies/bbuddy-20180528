package com.odde.bbuddy.budget.domain;

import com.odde.bbuddy.budget.repo.Budget;
import com.odde.bbuddy.budget.repo.BudgetRepo;
import com.odde.bbuddy.common.callback.PostActions;
import com.odde.bbuddy.common.validator.FieldCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static com.odde.bbuddy.common.callback.PostActionsFactory.failed;
import static com.odde.bbuddy.common.callback.PostActionsFactory.success;

@Service
public class Budgets implements FieldCheck<String> {
    private final BudgetRepo budgetRepo;

    @Autowired
    public Budgets(BudgetRepo budgetRepo) {
        this.budgetRepo = budgetRepo;
    }

    public PostActions add(Budget budget) {
        try {
            budgetRepo.save(budget);
            return success();
        } catch (IllegalArgumentException e) {
            return failed();
        }
    }

    @Override
    public boolean isValueUnique(String month) {
        return !budgetRepo.existsByName(month);
    }


    public int queryBudgetSum(LocalDate start, LocalDate end) {
        if (end.isBefore(start)) {
            return 0;
        }

        int months = start.until(end).getMonths();

        List<Budget> budgets = budgetRepo.findAll();

        if (months == 0) {
            for (Budget budget : budgets) {
                if (YearMonth.from(start).equals(budget.getYearMonth())) {
                    return budget.getAmount() / start.lengthOfMonth() * (start.until(end).getDays() + 1);
                }
            }
        }

        int sum = 0;
        if (months > 0) {
            for (Budget budget : budgets) {
                if (YearMonth.from(start).equals(budget.getYearMonth())) {
                    sum += budget.getAmount() / start.lengthOfMonth() * (start.until(budget.getEnd()).getDays() + 1);
                } else if (YearMonth.from(end).equals(budget.getYearMonth())) {
                    sum += budget.getAmount() / end.lengthOfMonth() * (budget.getStart().until(end).getDays() + 1);
                } else if (start.isBefore(budget.getStart()) && end.isAfter(budget.getEnd())) {
                    sum += budget.getAmount() / budget.getStart().lengthOfMonth() * (budget.getStart().until(budget.getEnd()).getDays() + 1);
                }
            }
        }

        return sum;
    }

}

