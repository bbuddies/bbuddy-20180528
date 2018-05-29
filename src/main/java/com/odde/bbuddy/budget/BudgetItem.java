package com.odde.bbuddy.budget;



import org.apache.commons.lang3.StringUtils;


import java.sql.Date;
import java.time.LocalDate;

public class BudgetItem {
    private int year;
    private int month;
    private double amount;

    public BudgetItem(int year, int month, double amount){
        this.year = year;
        this.month = month;
        this.amount = amount;
    }


    public int getYear(){
        return year;
    }


    public int getMonth(){
        return month;
    }



    public double getAmount() {
        return amount;
    }


    public LocalDate getFirstDate(){

        return LocalDate.of(this.year,this.month,1);


    }

    public LocalDate getLastDate(){
        LocalDate firstDate = this.getFirstDate();

      return firstDate.plusDays(firstDate.lengthOfMonth() -1);

    }

    public int getDayofMonth() {
        return this.getFirstDate().lengthOfMonth();
    }

}
