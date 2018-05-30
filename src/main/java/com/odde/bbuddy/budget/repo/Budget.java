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

    private YearMonth getYearMonth() {
        return YearMonth.parse(month);
    }

    private LocalDate getStart() {
        return getYearMonth().atDay(1);
    }

    private LocalDate getEnd() {
        return getYearMonth().atEndOfMonth();
    }

    private int getDailyAmount() {
        return amount / getStart().lengthOfMonth();
    }

    private Period getPeriod() {
        return new Period(getStart(), getEnd());
    }

    public int getOverlappingAmount(Period period) {
        return getDailyAmount() * period.getOverlappingDayCount(getPeriod());
    }
}
