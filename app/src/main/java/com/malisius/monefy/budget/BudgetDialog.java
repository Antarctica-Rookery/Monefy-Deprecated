package com.malisius.monefy.budget;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malisius.monefy.R;
import com.malisius.monefy.category.Category;

import java.util.ArrayList;
import java.util.Random;

public class BudgetDialog {
    public void showAddDialog(Context context, String status, ArrayList<Budget> mBudget, Budget oldBudget){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myDialogView = inflater.inflate(R.layout.dialog_edit_budget, null);

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        myDialog.setView(myDialogView);
        myDialog.setCancelable(true);

        Button submitDialog = myDialogView.findViewById(R.id.btnYes);
        Button cancelDialog = myDialogView.findViewById(R.id.btnNo);
        ImageView deleteDialog = myDialogView.findViewById(R.id.deleteIcon_budget);



        TextInputLayout budgetName = myDialogView.findViewById(R.id.budget_text_field);
        TextInputLayout budgetLimit = myDialogView.findViewById(R.id.budget_edit_limit);

        if(status == null) {
            budgetName.getEditText().setText(oldBudget.getName());
            budgetLimit.getEditText().setText(String.valueOf(oldBudget.getLimit()));
        }

        final AlertDialog dialog = myDialog.create();

        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        deleteDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference userBudgetRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Budget");
                ValueEventListener userBudgetListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            Log.w("budgetDialog", "No Children");
                        } else {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (dataSnapshot.child("name").getValue().toString().equals(oldBudget.getName())) {
                                    String key = dataSnapshot.getKey();
                                    DatabaseReference budgetRef = userBudgetRef.child(key);
                                    budgetRef.removeValue();
                                    dialog.dismiss();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                userBudgetRef.addListenerForSingleValueEvent(userBudgetListener);
            }
        });

        submitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status == null) {
                    if (!budgetName.getEditText().getText().toString().isEmpty() && !budgetLimit.getEditText().getText().toString().isEmpty()) {
                        DatabaseReference userDataRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Budget");
                        ValueEventListener userDataListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (!snapshot.exists()) {
                                    Log.w("budgetDialog", "No Children");
                                } else {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        if (dataSnapshot.child("name").getValue().toString().equals(oldBudget.getName())) {
                                            String key = dataSnapshot.getKey();
                                            int limit = Integer.parseInt(budgetLimit.getEditText().getText().toString());
                                            DatabaseReference dataRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Budget").child(key);
                                            DatabaseReference catRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Categories").child(key);
                                            ValueEventListener catValueEventListener = new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    int value = snapshot.child("totalIncome").getValue().hashCode();
                                                    value = value + snapshot.child("totalExpense").getValue().hashCode();
                                                    Budget newBudget = new Budget(limit, value, budgetName.getEditText().getText().toString());
                                                    dataRef.setValue(newBudget);
                                                    dialog.dismiss();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            };
                                            catRef.addListenerForSingleValueEvent(catValueEventListener);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.w("HomeFragment", "loadPost:onCancelled", error.toException());
                            }
                        };
                        userDataRef.addListenerForSingleValueEvent(userDataListener);
                    }
                }else{
                    if (!budgetName.getEditText().getText().toString().isEmpty() && !budgetLimit.getEditText().getText().toString().isEmpty()) {

                            DatabaseReference userDataRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Categories");
                            DatabaseReference userBudgetRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Budget");

                            Random obj = new Random();
                            int rand_num = obj.nextInt(0xffffff + 1);
                            String colorCode = String.format("#%06x", rand_num);
                            Category category = new Category(budgetName.getEditText().getText().toString(), colorCode);

                            String categoryKey = userDataRef.push().getKey();
                            userDataRef.child(categoryKey).setValue(category);
                            int limit = Integer.parseInt(budgetLimit.getEditText().getText().toString());
                            userBudgetRef.child(categoryKey).setValue(new Budget(limit, 0, budgetName.getEditText().getText().toString()));
                            dialog.dismiss();
                    }
                }

            }
        });
        dialog.show();
    }
}