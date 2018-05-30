package com.odde.bbuddy.budget.domain;

import java.time.LocalDate;

public class Period {
    private final LocalDate start;
    private final LocalDate end;

    public Period(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    private int getDayCount() {
        if (start.isAfter(end))
            return 0;

        return start.until(end).getDays() + 1;
    }

    public int getOverlappingDayCount(Period another) {
        LocalDate overlappingStart = start.isAfter(another.start) ? start : another.start;
        LocalDate overlappingEnd = end.isBefore(another.end) ? end : another.end;

        return new Period(overlappingStart, overlappingEnd).getDayCount();
    }

    public LocalDate getEnd() {
        return end;
    }

    public LocalDate getStart() {
        return start;
    }
}
