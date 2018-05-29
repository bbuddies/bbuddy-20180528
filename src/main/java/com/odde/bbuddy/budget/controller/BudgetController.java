package com.odde.bbuddy.budget.controller;

import com.odde.bbuddy.budget.domain.Budgets;
import com.odde.bbuddy.budget.repo.Budget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BudgetController {

    @Autowired
    Budgets budgets;

    @GetMapping("/budget/add")
    public String addBudget() {
        return "/budgets/add";
    }

    @PostMapping("/budget/add")
    public String submitAddBudget(@RequestParam(value = "amount", required = true)Double amount,
                                  @RequestParam(value = "month", required = true)String month) {

        Budget budget = new Budget();
        budget.setAmount(amount);
        budget.setMonth(month);
        budgets.add(budget);

        return "/budgets/index";
    }
}
