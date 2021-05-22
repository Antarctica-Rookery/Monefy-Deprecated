package com.malisius.monefy.income;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malisius.monefy.R;
import com.malisius.monefy.budget.Budget;
import com.malisius.monefy.category.Category;
import com.malisius.monefy.category.CategoryListAdapter;
import com.malisius.monefy.expense.CategoryExpenseListAdapter;
import com.malisius.monefy.records.RecordDialog;

import java.util.ArrayList;
import java.util.Collections;

import app.futured.donut.DonutProgressView;
import app.futured.donut.DonutSection;

public class IncomeFragment extends Fragment {
    private DonutProgressView donutProgressView;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private ArrayList<DonutSection> sections = new ArrayList<>();
    private ArrayList<Category> mCategoriesList = new ArrayList<Category>();
    private ArrayList<String> donutColor = new ArrayList<String>();
    private RecyclerView recyclerView;
    private FloatingActionButton fabButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_income, container, false);
        ConstraintLayout rootParent = (ConstraintLayout) container.getParent();

        // Fab controller
        fabButton = rootParent.findViewById(R.id.floatingActionButton);
        fabButton.setVisibility(View.VISIBLE);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                RecordDialog dialog = new RecordDialog();
                dialog.showDialog(getContext(), false, false, null);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        donutProgressView = root.findViewById(R.id.donut_view);
        donutColor.add("#aed581");
        donutColor.add("#8bc34a");
        donutColor.add("#689f38");
        DatabaseReference userDataRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Categories");
        ValueEventListener userDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Log.w("IncomeFragment", "No Child Exist");
                } else {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Log.i("IncomeFragment", dataSnapshot.getValue().toString());
                        mCategoriesList.add(dataSnapshot.getValue(Category.class));
                        Log.i("IncomeFragment", "hello");
                    }
                    Collections.sort(mCategoriesList, Category.totalIncomeComparatorDesc);
                    int totalIncomes = 0;
                    for (int i = 0; i < 3; i++) {
                        totalIncomes = totalIncomes + mCategoriesList.get(i).getTotalIncome();
                    }
                    int j = 0;
                    for (Category category : mCategoriesList) {
                        if (totalIncomes > 0) {
                            if (j < 3) {
                                sections.add(new DonutSection(category.getName(), Color.parseColor(donutColor.get(j)), (float) category.getTotalIncome() / totalIncomes));
                                j++;
                            }
                        } else {
                            if (j < 3) {
                                sections.add(new DonutSection(category.getName(), Color.parseColor(donutColor.get(j)), (float) 1 / 3));
                                j++;
                            }
                        }
                    }
                    donutProgressView.setCap(1);
                    donutProgressView.submitData(sections);
                    recyclerView = root.findViewById(R.id.income_recycler_view);
                    CategoryIncomeListAdapter adapter = new CategoryIncomeListAdapter(mCategoriesList);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        userDataRef.addListenerForSingleValueEvent(userDataListener);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.w("IncomeFragment", "View Created");
        super.onViewCreated(view, savedInstanceState);

    }
}
