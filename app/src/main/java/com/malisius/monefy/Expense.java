package com.malisius.monefy;

public class Expense {

    private String name;
    private int value;


    public Expense() {
    }

    public Expense(String name, int value) {
        this.name = name;
        this.value = value;
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
}

