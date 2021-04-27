package com.malisius.monefy.category;

import android.graphics.Color;

import com.malisius.monefy.Expense;
import com.malisius.monefy.Income;

import java.util.ArrayList;

public class Category {
    private String name;
    private int color;
    private ArrayList<Income> incomes;
    private ArrayList<Expense> expenses;
    private int totalIncome = 0;
    private int totalExpense = 0;

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

    public int getTotalIncome() {
        return totalIncome;
    }

    public int getTotalExpense() {
        return totalExpense;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setIncomes(ArrayList<Income> incomes) {
        this.incomes = incomes;
        if(!incomes.isEmpty()){
            for(Income income: incomes){
                totalIncome += income.getValue();
            }
        }
    }

    public void setExpenses(ArrayList<Expense> expenses) {
        this.expenses = expenses;
        if(!expenses.isEmpty()){
            for(Expense expense: expenses){
                totalExpense += expense.getValue();
            }
        }
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
