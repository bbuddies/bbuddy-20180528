package com.odde.bbuddy.budget;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BudgetReport {

    public double getTotalBudget(LocalDate beginDate, LocalDate endDate, List<BudgetItem> budgetItems) {
        if (budgetItems.isEmpty()) {
            return 0;
        }
        List<BudgetItem> allBudgetsItems = budgetItems.stream().filter( budgetItem ->
                budgetItem.getFirstDate().isAfter(beginDate) && budgetItem.getFirstDate().isBefore(endDate) ||
                budgetItem.getLastDate().isAfter(beginDate) && budgetItem.getLastDate().isBefore(endDate))
                 .collect(Collectors.toList());

        double totalBudgets = allBudgetsItems.stream().mapToDouble(BudgetItem::getAmount).sum();


        Optional<BudgetItem> firstMonthBudget = allBudgetsItems.stream().filter(budgetItem -> budgetItem.getYear() == beginDate.getYear() &&
        budgetItem.getMonth() == beginDate.getMonthValue()).findFirst();

        if (firstMonthBudget.isPresent()) {

            totalBudgets = totalBudgets - firstMonthBudget.get().getAmount() * (beginDate.getDayOfMonth() - 1)/firstMonthBudget.get().getDayofMonth();
        }

        Optional<BudgetItem> lastMonthBudget = allBudgetsItems.stream().filter(budgetItem -> budgetItem.getYear() == endDate.getYear() &&
                budgetItem.getMonth() == endDate.getMonthValue()).findFirst();

        if (lastMonthBudget.isPresent()) {

            int lastDayOfMonth = lastMonthBudget.get().getDayofMonth();
            totalBudgets = totalBudgets - lastMonthBudget.get().getAmount() * (lastDayOfMonth  - endDate.getDayOfMonth())/lastDayOfMonth;
        }
        return totalBudgets;

    }
}
