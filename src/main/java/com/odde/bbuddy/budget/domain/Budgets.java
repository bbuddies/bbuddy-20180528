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

        return getSum(new Period(start, end));
    }

    private int getSum(Period period) {
        int months = period.getMonths();

        List<Budget> budgets = budgetRepo.findAll();

        if (months == 0) {
            for (Budget budget : budgets) {
                if (YearMonth.from(period.getStart()).equals(budget.getYearMonth())) {
                    return budget.getAmount() / period.getStart().lengthOfMonth() * period.getDayCount();
                }
            }
        }

        int sum = 0;
        if (months > 0) {
            for (Budget budget : budgets) {
                if (YearMonth.from(period.getStart()).equals(budget.getYearMonth())) {
                    sum += budget.getAmount() / period.getStart().lengthOfMonth() * new Period(period.getStart(), budget.getEnd()).getDayCount();
                } else if (YearMonth.from(period.getEnd()).equals(budget.getYearMonth())) {
                    sum += budget.getAmount() / period.getEnd().lengthOfMonth() * new Period(budget.getStart(), period.getEnd()).getDayCount();
                } else if (period.getStart().isBefore(budget.getStart()) && period.getEnd().isAfter(budget.getEnd())) {
                    sum += budget.getAmount() / budget.getStart().lengthOfMonth() * new Period(budget.getStart(), budget.getEnd()).getDayCount();
                }
            }
        }

        return sum;
    }

}

