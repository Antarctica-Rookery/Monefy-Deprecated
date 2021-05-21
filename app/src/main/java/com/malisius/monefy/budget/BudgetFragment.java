package com.malisius.monefy.budget;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.malisius.monefy.R;
import com.malisius.monefy.category.Category;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

import app.futured.donut.DonutProgressView;
import app.futured.donut.DonutSection;

public class BudgetFragment extends Fragment {
    private DonutProgressView donutProgressView;
    private ArrayList<DonutSection> sections = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private ArrayList<Budget> mBudgetList = new ArrayList<Budget>();
    private ArrayList<String> donutColor = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_budget, container, false);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        donutColor.add("#aed581");
        donutColor.add("#8bc34a");
        donutColor.add("#689f38");

        DatabaseReference userBudgetRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Budget");
        ValueEventListener userBudgetListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    mBudgetList.add(new Budget(dataSnapshot.child("limit").getValue().hashCode(),
                            dataSnapshot.child("value").getValue().hashCode(),
                            dataSnapshot.child("name").getValue().toString()));
                }
                donutProgressView = root.findViewById(R.id.donut_view);
                Collections.sort(mBudgetList, Budget.budgetComparatorDesc);
                int totalLimit = 0;
                for(int i = 0; i < 3; i++){
                    totalLimit = totalLimit + mBudgetList.get(i).getLimit();
                }
                int j = 0;
                for(Budget budget: mBudgetList){
                    if(totalLimit > 0) {
                        if (j < 3) {
                            sections.add(new DonutSection(budget.getName(), Color.parseColor(donutColor.get(j)), (float) budget.getLimit() / totalLimit));
                            j++;
                        }
                    } else {
                        if (j < 3) {
                            sections.add(new DonutSection(budget.getName(), Color.parseColor(donutColor.get(j)), (float) 1/3));
                            j++;
                        }
                    }
                }
                donutProgressView.setCap(1);
                donutProgressView.submitData(sections);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("HomeFragment", "loadPost:onCancelled", error.toException());
            }
        };
        userBudgetRef.addValueEventListener(userBudgetListener);
        //using donut progress


        return root;
    }
}