package com.odde.bbuddy.budget.controller;

import com.odde.bbuddy.budget.domain.Budgets;
import com.odde.bbuddy.budget.repo.Budget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class BudgetController {

    private Budgets budgets;

    @Autowired
    public BudgetController(Budgets budget){
        this.budgets = budget;
    }

    @GetMapping("/budgets/add")
    public String addBudget() {
        return "/budgets/add";
    }

    @PostMapping("/budgets/add")
    public String submitAddBudget(@RequestParam(value = "amount", required = true)int amount,
                                  @RequestParam(value = "month", required = true)String month) {

        Budget budget = new Budget();
        budget.setAmount(amount);
        budget.setMonth(month);

        budgets.add(budget);

        return "/budgets/add";
    }
}
