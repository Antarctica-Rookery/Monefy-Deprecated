package com.malisius.monefy;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout inflaterIncome, inflaterExpense, inflaterBudget;
    private View incomeCategory,expenseCategory,budgetCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottom_nav = findViewById(R.id.bottom_nav);
        bottom_nav.setSelectedItemId(R.id.home);
        bottom_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.income:
                        startActivity(new Intent(getApplicationContext(), IncomeActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.expense:
                        startActivity(new Intent(getApplicationContext(), ExpenseActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.budget:
                        startActivity(new Intent(getApplicationContext(), BudgetActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        inflaterIncome = findViewById(R.id.inflater_income);
        inflaterExpense = findViewById(R.id.inflater_expense);
        inflaterBudget = findViewById(R.id.inflater_budget);

        budgetCategory = getLayoutInflater().inflate(R.layout.inflater_income, inflaterBudget, false);
        expenseCategory = getLayoutInflater().inflate(R.layout.inflater_expense, inflaterExpense, false);
        incomeCategory = getLayoutInflater().inflate(R.layout.inflater_income, inflaterIncome, false);


        inflaterBudget.addView(budgetCategory);
        inflaterExpense.addView(expenseCategory);
        inflaterIncome.addView(incomeCategory);
    }
}

