package com.malisius.monefy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button loginBtn;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private TextInputLayout TILusername, TILpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        TILusername = findViewById(R.id.tf_username);
        TILpassword = findViewById(R.id.tf_password);
    }

    public void signUp(View view){
        Intent signUpIntent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(signUpIntent);
    }

    public void signIn(View view){
        String username = TILusername.getEditText().getText().toString();
        String password = TILpassword.getEditText().getText().toString();
        Log.i("MainActivity", username);
        if(username.isEmpty()){
            TILusername.setError("Username is Empty");
        } else {
            mDatabase = FirebaseDatabase.getInstance();
            DatabaseReference mUserReference = mDatabase.getReference().child("Users");
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot userDataSnapshot : dataSnapshot.getChildren()) {
                        if (username.equals(userDataSnapshot.child("username").getValue().toString())) {
                            Log.i("MainActivity",userDataSnapshot.child("email").getValue().toString());
                            if(password.isEmpty()){
                                TILpassword.setError("Password must not empty");
                            } else {
                                Query mUserReference = mDatabase.getReference().child("Users");
                                ValueEventListener eventListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Log.i("MainActivity",snapshot.toString());
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.i("MainActivity",error.toString());
                                    }
                                };
                                mUserReference.addListenerForSingleValueEvent(eventListener);
                            }
                        } else {
                            Log.i("MainActivity","userID null");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            mUserReference.addListenerForSingleValueEvent(eventListener);
        }

    }
}