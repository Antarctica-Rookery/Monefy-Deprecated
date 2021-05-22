package com.malisius.monefy.category;

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
import com.malisius.monefy.budget.Budget;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class CategoryDialog {
    public void showAddDialog(Context context, String name, ArrayList<Category> mCategory, int index){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myDialogView = inflater.inflate(R.layout.edit_category_layout, null);

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        myDialog.setView(myDialogView);
        myDialog.setCancelable(true);

        Button submitDialog = myDialogView.findViewById(R.id.btnYes);
        Button cancelDialog = myDialogView.findViewById(R.id.btnNo);
        ImageView deleteDialog = myDialogView.findViewById(R.id.deleteIcon_category);

        TextInputLayout categoryName = myDialogView.findViewById(R.id.ticategory);

        if(name != null) {
            categoryName.getEditText().setText(name);
            deleteDialog.setVisibility(View.VISIBLE);
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
                if(!categoryName.getEditText().getText().toString().isEmpty()) {
                    DatabaseReference userDataRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Categories");

                    ValueEventListener userDataListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) {
                                Log.w("CatFragment", "No Children");
                            } else {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Log.i("CatFragment", dataSnapshot.child("name").getValue().toString());
                                    if (dataSnapshot.child("name").getValue().toString().equals(name)) {
                                        Log.i("CatFragment", "found");
                                        String key = dataSnapshot.getKey();
                                        DatabaseReference dataRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Categories").child(key);
                                        dataRef.removeValue();
                                        DatabaseReference budgetRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Budget").child(key);
                                        budgetRef.removeValue();
                                        mCategory.clear();
                                        dialog.dismiss();
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
            }
        });

        submitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name == null && !categoryName.getEditText().getText().toString().isEmpty()) {
                    Random obj = new Random();
                    int rand_num = obj.nextInt(0xffffff + 1);
                    String colorCode = String.format("#%06x", rand_num);
//                    mCategory.add(new Category(categoryName.getEditText().getText().toString(), colorCode));
                    Category newCategory = new Category(categoryName.getEditText().getText().toString(), colorCode);
                    DatabaseReference userDataRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Categories");
                    DatabaseReference userBudgetRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Budget");
                    String categoryKey = userDataRef.push().getKey();
                    userDataRef.child(categoryKey).setValue(newCategory);
                    userBudgetRef.child(categoryKey).setValue(new Budget(0,0, categoryName.getEditText().getText().toString()));
                    mCategory.clear();
                    dialog.dismiss();
                } else {
                    if(!categoryName.getEditText().getText().toString().isEmpty()) {
                        DatabaseReference userDataRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Categories");

                        ValueEventListener userDataListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (!snapshot.exists()) {
                                    Log.w("CatFragment", "No Children");
                                } else {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        Log.i("CatFragment", dataSnapshot.child("name").getValue().toString());
                                        if (dataSnapshot.child("name").getValue().toString().equals(name)) {
                                            Log.i("CatFragment", "found");
                                            String key = dataSnapshot.getKey();
                                            DatabaseReference dataRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Categories").child(key);
                                            String color = dataSnapshot.child("color").getValue().toString();
                                            dataRef.setValue(new Category(categoryName.getEditText().getText().toString(), color));
                                            mCategory.clear();
                                            dialog.dismiss();
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
                }
            }
        });
        dialog.show();
    }

}
