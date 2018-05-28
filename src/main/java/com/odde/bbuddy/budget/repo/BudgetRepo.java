package com.odde.bbuddy.budget.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface BudgetRepo extends Repository<Budget, Long> {

    void save(Budget budget);

    @Query("SELECT CASE WHEN COUNT(month) > 0 THEN 'true' ELSE 'false' END from Budget budget where budget.month = ?1")
    boolean existsByName(String name);
}
