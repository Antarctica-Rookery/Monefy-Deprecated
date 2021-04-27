package com.malisius.monefy.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malisius.monefy.Expense;
import com.malisius.monefy.Income;
import com.malisius.monefy.R;
import com.malisius.monefy.category.Category;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private ArrayList<Category> mCategoriesList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
//        Category cat = new Category("Food", 00020);
//        ArrayList<Income> income = new ArrayList<Income>();
//        income.add(new Income("gaji", 10000));
//        ArrayList<Expense> expense = new ArrayList<Expense>();
//        expense.add(new Expense("baso", 2000));
//        cat.setExpenses(expense);
//        cat.setIncomes(income);
//        String categoryKey = mDatabase.getReference().child(mAuth.getUid()).push().getKey();
//        mDatabase.getReference().child(mAuth.getUid()).child(categoryKey).setValue(cat);
//        mDatabase.getReference().child(mAuth.getUid()).child("food").child("Expense").push().setValue(new Expense("Baso",5000));
//        mDatabase.getReference().child(mAuth.getUid()).child("food").child("Expense").push().setValue(new Expense("Baso",5000));
        DatabaseReference userDataRef = mDatabase.getReference().child(mAuth.getCurrentUser().getUid());
        ValueEventListener userDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Log.i("HomeFragment", dataSnapshot.getValue().toString());
                    Category category = dataSnapshot.getValue(Category.class);
                    Log.i("HomeFragment", "hello");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("HomeFragment", "loadPost:onCancelled", error.toException());
            }
        };
        userDataRef.addValueEventListener(userDataListener);

//        mDatabase.getReference().child(mAuth.getUid()).setValue(new Category("Gaji", 10020));
//        mDatabase.getReference().child(mAuth.getUid()).child("Gaji").setValue(new Income("Bulanan", 10000));
    }
}