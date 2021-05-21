package com.malisius.monefy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.malisius.monefy.budget.Budget;
import com.malisius.monefy.category.Category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button loginBtn;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private TextInputLayout TILusername, TILpassword;
    private ConstraintLayout constraintLayout;
    private GoogleSignInClient mGoogleSignInClient;
    private static int RC_SIGN_IN = 69;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            Intent iWannaGoHome = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(iWannaGoHome);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        TILusername = findViewById(R.id.tf_username);
        TILpassword = findViewById(R.id.tf_password);
        constraintLayout = findViewById(R.id.main_constraint);

        //declare Google sign in button
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);

        createRequest();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });
    }

    private void createRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public void signUp(View view){
        Intent signUpIntent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(signUpIntent);
    }

    public void forgotPassword(View view){
        Intent forgotIntent = new Intent(MainActivity.this, ForgotActivity.class);
        startActivity(forgotIntent);
    }

    public void googleSignIn(){
        //Google Sign-In
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signIn(View view){
        //Basic Sign-In
        String username = TILusername.getEditText().getText().toString();
        String password = TILpassword.getEditText().getText().toString();
        Log.i("MainActivity", username);
        if(username.isEmpty()) {
            TILusername.setError("Username is Empty");
        } else {
            if(!Patterns.EMAIL_ADDRESS.matcher(username).matches()){

                DatabaseReference mUserReference = mDatabase.getReference().child("Users");
                mUserReference.keepSynced(true);
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean foundUser = false;
                        for (DataSnapshot userDataSnapshot : dataSnapshot.getChildren()) {
                            if (username.equals(userDataSnapshot.child("username").getValue().toString())) {
                                foundUser = true;
                                Log.i("MainActivity",userDataSnapshot.child("email").getValue().toString());
                                if(password.isEmpty()){
                                    TILpassword.setError("Password must not empty");
                                } else {
                                    String email = userDataSnapshot.child("email").getValue().toString();
                                    mAuth.signInWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        // Sign in success, update UI with the signed-in user's information
                                                        Log.d("MainActivity", "signInWithEmail:success");
                                                        FirebaseUser user = mAuth.getCurrentUser();
                                                        Bundle userBundle = new Bundle();
                                                        userBundle.putString("username", username);
                                                        userBundle.putString("uid", user.getUid());
                                                        Snackbar.make(constraintLayout,"Login Sucessful",Snackbar.LENGTH_SHORT).show();
                                                        Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                                                        homeIntent.putExtra("USER_DATA", userBundle);
                                                        startActivity(homeIntent);
                                                    } else {
                                                        // If sign in fails, display a message to the user.
                                                        Log.w("MainActivity",  task.getException());
                                                        if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                                            TILpassword.setError("Invalid Password");
                                                        }
                                                        else if (task.getException() instanceof FirebaseTooManyRequestsException) {
                                                            Snackbar.make(constraintLayout,"Too Many Request",Snackbar.LENGTH_SHORT)
                                                                    .setBackgroundTint(ResourcesCompat.getColor(getResources(), R.color.red, null))
                                                                    .setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null))
                                                                    .show();
                                                        } else {
                                                            Snackbar.make(constraintLayout,"Unkown Error",Snackbar.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                        if(foundUser == false){
                            TILusername.setError("User Not Found");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                mUserReference.addListenerForSingleValueEvent(eventListener);
            }
            else {
                if(password.isEmpty()){
                    TILpassword.setError("Password must not empty");
                } else {
                    mAuth.signInWithEmailAndPassword(username, password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("MainActivity", "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Bundle userBundle = new Bundle();
                                        userBundle.putString("username", username);
                                        userBundle.putString("uid", user.getUid());
//                                        initiateCategory();
                                        Snackbar.make(constraintLayout,"Login Successful",Snackbar.LENGTH_SHORT).show();
                                        Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                                        homeIntent.putExtra("USER_DATA", userBundle);
                                        startActivity(homeIntent);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("MainActivity",  task.getException());
                                        if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                            TILpassword.setError("Invalid Email or Password");
                                            TILusername.setError("Invalid Email or Password");
                                        }
                                        else if(task.getException() instanceof FirebaseAuthInvalidUserException){
                                            TILusername.setError("No User with this Email Found");
                                        }
                                        else if (task.getException() instanceof FirebaseTooManyRequestsException) {
                                            Snackbar.make(constraintLayout,"Too Many Request",Snackbar.LENGTH_SHORT)
                                                    .setBackgroundTint(ResourcesCompat.getColor(getResources(), R.color.red, null))
                                                    .setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null))
                                                    .show();
                                        } else {
                                            Snackbar.make(constraintLayout,"Unknown Error",Snackbar.LENGTH_SHORT).show();
                                        }
                                    }

                                }
                            });
                }
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed
                Log.w("GoogleSignIn", e.getMessage());
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent iWannaGoHome = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(iWannaGoHome);
                        } else {
                            // Sign in failed
                            Toast.makeText(MainActivity.this, "Authorization Failed", Toast.LENGTH_SHORT).show();
                       }
                    }
                });
    }


    private void initiateCategory() {
        DatabaseReference userDataRef = mDatabase.getReference("Data").child(mAuth.getCurrentUser().getUid()).child("Categories");
        ValueEventListener userDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    DatabaseReference userDataChildRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Categories");
                    DatabaseReference userBudgetRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Budget");
                    ArrayList<Category> categoriesName = new ArrayList<Category>();
                    ArrayList<String> names = new ArrayList<String>();
                    names.add("Food");
                    names.add("Shopping");
                    names.add("Housing");
                    names.add("Transportation");
                    names.add("Financial");
                    for(int i = 0; i < 5; i++){
                        Random obj = new Random();
                        int rand_num = obj.nextInt(0xffffff + 1);
                        String colorCode = String.format("#%06x", rand_num);
                        categoriesName.add(new Category(names.get(i),colorCode));
                    }

                    for(Category category : categoriesName){
                        String categoryKey = userDataChildRef.push().getKey();
                        userDataChildRef.child(categoryKey).setValue(category);
                        userBudgetRef.child(categoryKey).setValue(new Budget(0,0, category.getName()));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("MainActivity", "loadPost:onCancelled", error.toException());
            }
        };
        userDataRef.addValueEventListener(userDataListener);
    }
}