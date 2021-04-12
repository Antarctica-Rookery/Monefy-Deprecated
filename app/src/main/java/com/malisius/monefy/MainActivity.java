package com.malisius.monefy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button loginBtn, signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openLoginActivity(View v){
        Log.i("MainActivity","Button Login Pressed");
    }

    public void openRegisterActivity(View v){
        Log.i("MainActivity","Button Signup Pressed");
    }

    public void openAboutActivity(View v){
        Log.i("MainActivity","About application Pressed");
    }
}