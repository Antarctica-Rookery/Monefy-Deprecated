package com.malisius.monefy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {

    private TextInputLayout TILemail;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        TILemail = findViewById(R.id.forgot_email);
        LinearLayout llback = findViewById(R.id.ll_back);
        llback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void resetPasssword(View v){
        email =  TILemail.getEditText().getText().toString();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if(email.isEmpty()){
            TILemail.setError("Email is Empty");
        }
        else {
            if(Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotActivity.this, "Reset Email Sent", Toast.LENGTH_SHORT).show();
                                } else {
                                    TILemail.setError("Email not found");
                                }
                            }
                        });
            } else {
                TILemail.setError("Email is not in format");
            }
        }
    }
}