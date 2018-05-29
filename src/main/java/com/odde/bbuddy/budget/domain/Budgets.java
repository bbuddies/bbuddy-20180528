package com.odde.bbuddy.budget.domain;

import com.odde.bbuddy.budget.repo.Budget;
import com.odde.bbuddy.budget.repo.BudgetRepo;
import com.odde.bbuddy.common.callback.PostActions;
import com.odde.bbuddy.common.validator.FieldCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
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

        List<Budget> budgets = budgetRepo.findAll();

        Period period = start.until(end);
        int months = period.getMonths();


        if (months < 0) {
            return 0;
        }


        int daysOfStartMonth = start.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
        int daysOfEndMonth = end.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();

        if (months == 0) {
            int days = period.getDays();
            if (days < 0) {
                return 0;
            }

            String startMonth = start.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            for (Budget budget : budgets) {
                if (startMonth.equals(budget.getMonth())) {
                    return budget.getAmount() / daysOfStartMonth * (days + 1);
                }
            }
        }

        int month = start.getMonthValue();
        int sum = 0;
        int monthBudget;
        if (months > 0) {
//            for (int i = 0; i < months; i++) {
//                month += i;
                String startMonth = start.format(DateTimeFormatter.ofPattern("yyyy-MM"));
                String endMonth = end.format(DateTimeFormatter.ofPattern("yyyy-MM"));
                for (Budget budget : budgets) {
                    if (startMonth.equals(budget.getMonth())) {
//                        if (i == 0) {
                            monthBudget = budget.getAmount() / daysOfStartMonth * (daysOfStartMonth + 1 - start.getDayOfMonth());
                            sum += monthBudget;
//                        } else if (months == 1) {
//                            monthBudget = end.getDayOfMonth() / daysOfEndMonth * budget.getAmount();
//                            sum += monthBudget;
//                        } else {
//                            sum += budget.getAmount();
//                        }
                    } else if (endMonth.equals(budget.getMonth())) {
                        sum += budget.getAmount() / daysOfEndMonth * (YearMonth.parse(budget.getMonth()).atDay(1).until(end).getDays() + 1);
                    } else if (start.isBefore(YearMonth.parse(budget.getMonth()).atDay(1)) && end.isAfter(YearMonth.parse(budget.getMonth()).atEndOfMonth())) {
                        sum += budget.getAmount();
                    }
                }
            }
//        }

        return sum;
    }
}

