package com.malisius.monefy.expense;

import android.graphics.Color;
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
import com.malisius.monefy.R;
import com.malisius.monefy.category.Category;

import java.util.ArrayList;

import app.futured.donut.DonutProgressView;
import app.futured.donut.DonutSection;

public class ExpenseFragment extends Fragment {
    private DonutProgressView donutProgressView;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private ArrayList<DonutSection> sections = new ArrayList<>();
    private ArrayList<Category> mCategoriesList = new ArrayList<Category>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_expense, container, false);

        //using donut progress
        donutProgressView = root.findViewById(R.id.donut_view);

        sections.add(new DonutSection("Category 1", Color.parseColor("#ffab91"), 1.2f));
        sections.add(new DonutSection("Category 2", Color.parseColor("#ff8a65"), 0.7f));
        sections.add(new DonutSection("Category 3", Color.parseColor("#ff7043"), 0.3f));
        donutProgressView.setCap(3.0f);
        donutProgressView.submitData(sections);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.w("ExpenseFragment", "View Created");
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userDataRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Categories");
        ValueEventListener userDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Log.w("ExpenseFragment", "No Child Exist");
                } else {
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        Log.i("ExpenseFragment", dataSnapshot.getValue().toString());
                        mCategoriesList.add(dataSnapshot.getValue(Category.class));
                        Log.i("ExpenseFragment", "hello");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        userDataRef.addValueEventListener(userDataListener);
    }
}