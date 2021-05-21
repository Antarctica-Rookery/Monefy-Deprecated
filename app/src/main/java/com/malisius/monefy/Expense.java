package com.malisius.monefy;

import java.util.Date;

public class Expense {

    private String name;
    private int value;
    private Date date;


    public Expense() {
    }

    public Expense(String name, int value, Date date) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

