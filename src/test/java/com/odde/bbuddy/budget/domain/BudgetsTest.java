package com.odde.bbuddy.budget.domain;

import com.odde.bbuddy.budget.repo.Budget;
import com.odde.bbuddy.budget.repo.BudgetRepo;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @Author wangfd@zailingtech.com
 * @Description
 * @Data Created in  2018/5/29 16:53
 * @Corp New Zailing
 */
public class BudgetsTest {

    private BudgetRepo repo = mock(BudgetRepo.class);
    private Budgets budgets = new Budgets(repo);

    private void assertBudgets(int expected, String startDate, String stopDate){
        int sum = budgets.query(LocalDate.parse(startDate), LocalDate.parse(stopDate));
        assertEquals(expected, sum);
    }

    private void givenBudgets(Budget ...budgets){
        when(repo.findAll()).thenReturn(Arrays.asList(budgets));
    }

    @Test
    public void test_two_month(){
        givenBudgets(
                new Budget(){{
                    setMonth("2018-05");
                    setAmount(310);
                }},
                new Budget(){{
                    setMonth("2018-06");
                    setAmount(300);
                }}
        );

        assertBudgets(310+300/30, "2018-05-01", "2018-06-01");
    }

    @Test
    public void test_part_of_one_month(){
        givenBudgets(
                new Budget(){{
                    setMonth("2018-05");
                    setAmount(310);
                }}
        );

        assertBudgets(310/31*10, "2018-05-01", "2018-05-10");
    }

    @Test
    public void test_whole_month(){
        givenBudgets(
                new Budget(){{
                    setMonth("2018-05");
                    setAmount(310);
                }}
        );

        assertBudgets(310, "2018-05-01", "2018-05-31");
    }

    @Test
    public void test_one_day(){
        givenBudgets(
                new Budget(){{
                    setMonth("2018-05");
                    setAmount(310);
                }}
        );

        assertBudgets(10, "2018-05-31", "2018-05-31");
    }

    @Test
    public void test_start_month_amont_zero(){
        givenBudgets(
                new Budget(){{
                    setMonth("2018-02");
                    setAmount(280);
                }},
                new Budget(){{
                    setMonth("2018-03");
                    setAmount(310);
                }},
                new Budget(){{
                    setMonth("2018-04");
                    setAmount(300);
                }}
        );

        assertBudgets(280+310+160, "2018-01-21", "2018-04-16");
    }

    @Test
    public void test_second_month_amont_zero(){
        givenBudgets(
                new Budget(){{
                    setMonth("2018-01");
                    setAmount(310);
                }},
                new Budget(){{
                    setMonth("2018-03");
                    setAmount(310);
                }},
                new Budget(){{
                    setMonth("2018-04");
                    setAmount(300);
                }}
        );

        assertBudgets(100+310+100, "2018-01-22", "2018-04-10");
    }

    @Test
    public void test_more_than_one_year(){
        givenBudgets(
                new Budget(){{
                    setMonth("2016-02");
                    setAmount(290);
                }},
                new Budget(){{
                    setMonth("2017-02");
                    setAmount(280);
                }},
                new Budget(){{
                    setMonth("2017-10");
                    setAmount(310);
                }},
                new Budget(){{
                    setMonth("2018-02");
                    setAmount(280);
                }}
        );

        assertBudgets(100+280+310+100, "2016-02-20", "2018-02-10");
    }

    @Test
    public void test_start_date_less_than_stop_date (){
        givenBudgets(
                new Budget(){{
                    setMonth("2018-07");
                    setAmount(310);
                }},
                new Budget(){{
                    setMonth("2018-08");
                    setAmount(310);
                }}
        );

        assertBudgets(0, "2018-08-01", "2018-07-22");
    }

}