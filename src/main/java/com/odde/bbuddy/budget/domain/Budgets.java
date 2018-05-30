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

        Period period = start.until(end);
        int months = period.getMonths();

        List<Budget> budgets = budgetRepo.findAll();

        if (months == 0) {
            for (Budget budget : budgets) {
                if (YearMonth.from(start).equals(budget.getYearMonth())) {
                    return budget.getAmount() / start.lengthOfMonth() * (period.getDays() + 1);
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
                            monthBudget = budget.getAmount() / start.lengthOfMonth() * (start.lengthOfMonth() + 1 - start.getDayOfMonth());
                            sum += monthBudget;
//                        } else if (months == 1) {
//                            monthBudget = end.getDayOfMonth() / daysOfEndMonth * budget.getAmount();
//                            sum += monthBudget;
//                        } else {
//                            sum += budget.getAmount();
//                        }
                    } else if (endMonth.equals(budget.getMonth())) {
                        sum += budget.getAmount() / end.lengthOfMonth() * (budget.getYearMonth().atDay(1).until(end).getDays() + 1);
                    } else if (start.isBefore(budget.getYearMonth().atDay(1)) && end.isAfter(budget.getYearMonth().atEndOfMonth())) {
                        sum += budget.getAmount();
                    }
                }
            }
//        }

        return sum;
    }

}

