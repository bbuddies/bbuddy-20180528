package com.odde.bbuddy.budget.repo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.YearMonth;

@Getter
@Setter
@Entity
@Table(name = "budgets")
public class Budget {

    @Id
    @GeneratedValue
    private long id;

    private String month;
    private int amount;

    public YearMonth getYearMonth() {
        return YearMonth.parse(month);
    }

    public LocalDate getStart() {
        return getYearMonth().atDay(1);
    }

    public LocalDate getEnd() {
        return getYearMonth().atEndOfMonth();
    }
}
