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

        return queryBudgetSum(new Period(start, end));
    }

    private int queryBudgetSum(Period period) {
        int months = period.getMonths();

        List<Budget> budgets = budgetRepo.findAll();

        if (months == 0) {
            for (Budget budget : budgets) {
                if (YearMonth.from(period.getStart()).equals(budget.getYearMonth())) {
                    return budget.getDailyAmount() * period.getDayCount();
                }
            }
        }

        int sum = 0;
        if (months > 0) {
            for (Budget budget : budgets) {
                sum += getAmount(period, budget);
            }
        }

        return sum;
    }

    private int getAmount(Period period, Budget budget) {
        return budget.getDailyAmount() * getOverlappingDayCount(period, budget);
    }

    private int getOverlappingDayCount(Period period, Budget budget) {
        LocalDate overlappingStart = period.getStart().isAfter(budget.getStart()) ? period.getStart() : budget.getStart();
        LocalDate overlappingEnd = period.getEnd().isBefore(budget.getEnd()) ? period.getEnd() : budget.getEnd();

        if (overlappingStart.isAfter(overlappingEnd))
            return 0;

        return new Period(overlappingStart, overlappingEnd).getDayCount();
    }

}

