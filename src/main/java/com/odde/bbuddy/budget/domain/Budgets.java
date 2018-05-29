package com.odde.bbuddy.budget.domain;

import com.odde.bbuddy.budget.repo.Budget;
import com.odde.bbuddy.budget.repo.BudgetRepo;
import com.odde.bbuddy.common.callback.PostActions;
import com.odde.bbuddy.common.validator.FieldCheck;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
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

    public int query(LocalDate startTime, LocalDate stopTime){

        List<Budget> budgets = budgetRepo.findAll();

        int sum = 0;

        String startDateString = startTime.getYear() + "-" + String.format("%02d", startTime.getMonthValue());
        String stopDateString = stopTime.getYear() + "-" + String.format("%02d", stopTime.getMonthValue());

        if(startDateString.compareTo(stopDateString) > 0){
            return 0;
        }

        Budget startMonthValue = null;
        Budget stopMonthValue = null;

        for(int i = 0; i < budgets.size(); ++i){
            Budget budget = budgets.get(i);
            if(budget.getMonth().equals(startDateString)){
                startMonthValue = budget;
            }else if(budget.getMonth().equals(stopDateString)){
                stopMonthValue = budget;
            }else if(budget.getMonth().compareTo(startDateString) > 0 &&
                    budget.getMonth().compareTo(stopDateString) < 0){
                sum += budget.getAmount();
            }
        }

        if(startDateString.equals(stopDateString)){
             sum += getMonthBudget(startTime, stopTime, startMonthValue);
        }else{

            LocalDate firstMonthLastDay = startTime.with(TemporalAdjusters.lastDayOfMonth());
            sum += getMonthBudget(startTime, firstMonthLastDay, startMonthValue);

            LocalDate lastMonthFirstDay = stopTime.with(TemporalAdjusters.firstDayOfMonth());

            sum += getMonthBudget(lastMonthFirstDay, stopTime, stopMonthValue);
        }

        return sum;
    }

    private int getMonthBudget(LocalDate startDay, LocalDate stopDay, Budget monthValue){

        if(monthValue == null){
            return 0;
        }

        int day = stopDay.getDayOfMonth() - startDay.getDayOfMonth() + 1;

        LocalDate endDayOfMonth = startDay.with(TemporalAdjusters.lastDayOfMonth());
        return monthValue.getAmount()*day/endDayOfMonth.getDayOfMonth();
    }
}
