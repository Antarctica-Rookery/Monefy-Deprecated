package com.malisius.monefy.expense;

import com.malisius.monefy.category.Category;

import java.util.Comparator;
import java.util.Date;

public class Expense {

    private String name;
    private int value;
    private long date;


    public Expense() {
    }

    public Expense(String name, int value, long date) {
        this.name = name;
        this.value = value;
        this.date = date;
    }

    public String getName() {
        return name;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public static Comparator<Expense> expenseDateComparator = new Comparator<Expense>() {

        public int compare(Expense s1, Expense s2) {
            Date date1 = new Date(s1.getDate());
            Date date2 = new Date(s2.getDate());

            //desc order
            return date1.compareTo(date2);
        }
    };
}

