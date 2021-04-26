package com.malisius.monefy;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottom_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottom_nav = findViewById(R.id.bottom_nav);
        bottom_nav.setSelectedItemId(R.id.home);
        bottom_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        return true;
                    case R.id.income:
                        startActivity(new Intent(getApplicationContext(), IncomeActivity.class));
                        return true;
                    case R.id.expense:
                        startActivity(new Intent(getApplicationContext(), ExpenseActivity.class));
                        return true;
                    case R.id.budget:
                        startActivity(new Intent(getApplicationContext(), BudgetActivity.class));
                        return true;
                }
                return false;
            }
        });
    }
}
