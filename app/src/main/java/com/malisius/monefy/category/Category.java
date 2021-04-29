package com.malisius.monefy.category;

import android.graphics.Color;

import com.malisius.monefy.Expense;
import com.malisius.monefy.Income;

import java.util.ArrayList;
import java.util.Comparator;

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

    public static Comparator<Category> totalIncomeComparatorAsc = new Comparator<Category>() {

        public int compare(Category s1,Category s2) {

            int income1 = s1.getTotalIncome();
            int income2 = s2.getTotalIncome();

            /*For ascending order*/
            return income1-income2;
        }
    };

    public static Comparator<Category> totalIncomeComparatorDesc = new Comparator<Category>() {

        public int compare(Category s1,Category s2) {

            int income1 = s1.getTotalIncome();
            int income2 = s2.getTotalIncome();

            /*For ascending order*/
            return income2-income1;
        }
    };

    public static Comparator<Category> totalExpenseComparatorAsc = new Comparator<Category>() {

        public int compare(Category s1,Category s2) {

            int income1 = s1.getTotalExpense();
            int income2 = s2.getTotalExpense();

            /*For ascending order*/
            return income1-income2;
        }
    };

    public static Comparator<Category> totalExpenseComparatorDesc = new Comparator<Category>() {

        public int compare(Category s1,Category s2) {

            int income1 = s1.getTotalExpense();
            int income2 = s2.getTotalExpense();

            /*For ascending order*/
            return income2-income1;
        }
    };

    public static Comparator<Category> categoryNameComparator = new Comparator<Category>() {

        public int compare(Category s1, Category s2) {
            String StudentName1 = s1.getName().toUpperCase();
            String StudentName2 = s2.getName().toUpperCase();

            //ascending order
            return StudentName1.compareTo(StudentName2);
        }
    };

    public static ArrayList<Category> createCategoryList(int numCategory) {
        ArrayList<Category> contacts = new ArrayList<Category>();

        for (int i = 1; i <= numCategory; i++) {
            int color= ((int)(Math.random()*16777215)) | (0xFF << 24);
            contacts.add(new Category("Category " + i, color));
        }

        return contacts;
    }

}
