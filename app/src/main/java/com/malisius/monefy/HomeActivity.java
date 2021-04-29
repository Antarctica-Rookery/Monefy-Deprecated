package com.malisius.monefy;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        BottomNavigationView navView = findViewById(R.id.bottom_nav);

        /* Only use this when using appbar
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment, R.id.incomeFragment, R.id.expenseFragment, R.id.budgetFragment, R.id.settingsFragment)
                .build();
         */

        /* Nitip buat nanti add */
//        if(dataSnapshot.child("name").getValue().equals("Fashion")){
//            String key = dataSnapshot.getKey();
//            mDatabase.getReference().child(mAuth.getUid()).child(key).setValue(cat);
//        }

        NavController navController = Navigation.findNavController(this, R.id.fragment);

        /* Only use this when using appbar
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
         */

        NavigationUI.setupWithNavController(navView, navController);



    }
}

