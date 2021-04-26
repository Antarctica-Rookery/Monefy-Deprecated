package com.malisius.monefy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {
    BottomNavigationView bottom_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        bottom_nav = findViewById(R.id.bottom_nav);
        bottom_nav.setSelectedItemId(R.id.settings);
        bottom_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
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
    }
}