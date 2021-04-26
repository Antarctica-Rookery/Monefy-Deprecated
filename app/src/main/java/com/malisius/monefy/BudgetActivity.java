package com.malisius.monefy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BudgetActivity extends AppCompatActivity {
    private BottomNavigationView bottom_nav;
    private int backpressed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        bottom_nav = findViewById(R.id.bottom_nav);
        bottom_nav.setSelectedItemId(R.id.budget);
        bottom_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
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
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(backpressed == 0){
            Toast.makeText(this, "Press the back button once again to exit", Toast.LENGTH_SHORT).show();
            new CountDownTimer(2000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    backpressed = 0;
                }
            }.start();
        }else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        backpressed++;
    }
}