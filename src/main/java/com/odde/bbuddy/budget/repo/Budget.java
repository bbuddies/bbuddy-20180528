package com.odde.bbuddy.budget.repo;

import com.odde.bbuddy.budget.domain.Period;
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

    public int getDailyAmount() {
        return amount / getStart().lengthOfMonth();
    }

    public Period getPeriod() {
        return new Period(getStart(), getEnd());
    }

    public int getOverlappingAmount(Period period) {
        return getDailyAmount() * period.getOverlappingDayCount(getPeriod());
    }
}
