package com.malisius.monefy.income;

import com.malisius.monefy.expense.Expense;

import java.util.Comparator;
import java.util.Date;

public class Income {

    private String name;
    private int value;
    private long date;

    public Income() {
    }

    public Income(String name, int value,long date) {
        this.name = name;
        this.value = value;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setDate(long date) {
        this.date = date;
    }



    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public long getDate() {return date;};

    public static Comparator<Income> incomeDateComparator = new Comparator<Income>() {

        public int compare(Income s1, Income s2) {
            Date date1 = new Date(s1.getDate());
            Date date2 = new Date(s2.getDate());

            //desc order
            return date1.compareTo(date2);
        }
    };
}
