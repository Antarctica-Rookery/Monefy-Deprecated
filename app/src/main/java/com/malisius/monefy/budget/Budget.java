package com.malisius.monefy.budget;

import com.malisius.monefy.category.Category;

import java.util.Comparator;

public class Budget {
    private int limit;
    private int value;

    public Budget() {
    }

    public Budget(int limit, int value) {
        this.limit = limit;
        this.value = value;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static Comparator<Budget> budgetComparatorAsc = new Comparator<Budget>() {

        public int compare(Budget s1, Budget s2) {

            int val1 = s1.getValue();
            int val2 = s2.getValue();

            /*For ascending order*/
            return val1-val2;
        }
    };

    public static Comparator<Budget> budgetComparatorDesc = new Comparator<Budget>() {

        public int compare(Budget s1, Budget s2) {

            int val1 = s1.getValue();
            int val2 = s2.getValue();

            /*For ascending order*/
            return val2-val1;
        }
    };
}
