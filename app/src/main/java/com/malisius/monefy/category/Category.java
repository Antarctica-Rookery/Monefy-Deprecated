package com.malisius.monefy.category;

import android.graphics.Color;

import java.util.ArrayList;

public class Category {
    private String mCategoryname;
    private int mColor;

    public Category(String categoryname, int color){
        mCategoryname = categoryname;
        mColor = color;
    }

    public String getCategoryName(){
        return mCategoryname;
    }

    public int getCategoryColor(){
        return mColor;
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
