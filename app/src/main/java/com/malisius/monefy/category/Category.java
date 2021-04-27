package com.malisius.monefy.category;

import android.graphics.Color;

import com.malisius.monefy.Expense;
import com.malisius.monefy.Income;

import java.util.ArrayList;

public class Category {
    private String name;
    private int color;
    private ArrayList<Income> incomes;
    private  ArrayList<Expense> expenses;

    public Category() {
    }

    public Category(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Income> getIncomes() {
        return incomes;
    }

    public ArrayList<Expense> getExpenses() {
        return expenses;
    }

    public int getColor() {
        return color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setIncomes(ArrayList<Income> incomes) {
        this.incomes = incomes;
    }

    public void setExpenses(ArrayList<Expense> expenses) {
        this.expenses = expenses;
    }

    public static ArrayList<Category> createCategoryList(int numCategory) {
        ArrayList<Category> contacts = new ArrayList<Category>();

        for (int i = 1; i <= numCategory; i++) {
            int color= ((int)(Math.random()*16777215)) | (0xFF << 24);
            contacts.add(new Category("Category " + i, color));
        }

        return contacts;
    }

}
