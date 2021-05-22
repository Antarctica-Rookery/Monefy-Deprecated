package com.malisius.monefy.income;

import java.util.Date;

public class Income {

    private String name;
    private int value;
    private Date date;

    public Income() {
    }

    public Income(String name, int value,Date date) {
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

    public Date getDate() {return date;};

}
