package com.malisius.monefy.budget;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malisius.monefy.R;
import com.malisius.monefy.category.Category;
import com.malisius.monefy.category.CategoryListAdapter;

import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import app.futured.donut.DonutProgressView;
import app.futured.donut.DonutSection;

public class BudgetFragment extends Fragment {
    private DonutProgressView donutProgressView;
    private ArrayList<DonutSection> sections = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private ArrayList<Budget> mBudgetList = new ArrayList<Budget>();
    private ArrayList<String> donutColor = new ArrayList<String>();
    private RecyclerView budgetRecycle;
    private TextView budgetLeft, budgetTotal, budgetTotalLeft;
    private FloatingActionButton fabButton;

    private String formatRupiah(int number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_budget, container, false);
        ConstraintLayout rootParent = (ConstraintLayout) container.getParent();

        fabButton = rootParent.findViewById(R.id.floatingActionButton);
        fabButton.setVisibility(View.VISIBLE);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("BudgetFragment", "fabButton pressed");
                BudgetDialog dialog = new BudgetDialog();
                dialog.showAddDialog(getContext(),false, mBudgetList, null);
            }
        });

        budgetTotalLeft = root.findViewById(R.id.totalLeft);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        budgetRecycle = root.findViewById(R.id.budget_recycle);
        donutColor.add("#aed581");
        donutColor.add("#8bc34a");
        donutColor.add("#689f38");
        budgetLeft = root.findViewById(R.id.budget_left);
        budgetTotal = root.findViewById(R.id.budget_total);

        DatabaseReference userBudgetRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Budget");
        ValueEventListener userBudgetListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sections.clear();
                mBudgetList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Log.i("BudgetFragmet", dataSnapshot.getValue().toString());
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
                int totalLeft = 0;
                for(int i = 0; i< 3; i++){
                    totalLeft = totalLeft + mBudgetList.get(i).getValue();
                }

                int j = 0;
                int totalBudgetLeft = 0;
                int totalBudgetLimit = 0;
                for(Budget budget: mBudgetList){
                    totalBudgetLeft = totalBudgetLeft + budget.getValue();
                    totalBudgetLimit = totalBudgetLimit + budget.getLimit();
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
                budgetLeft.setText(formatRupiah(totalBudgetLeft));
                budgetTotal.setText(formatRupiah(totalBudgetLimit));

                budgetTotalLeft.setText(formatRupiah(totalBudgetLimit - totalBudgetLeft));

                donutProgressView.setCap(1);
                donutProgressView.submitData(sections);
                BudgetListAdapter adapter = new BudgetListAdapter(mBudgetList);
                budgetRecycle.setAdapter(adapter);
                budgetRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("HomeFragment", "loadPost:onCancelled", error.toException());
            }
        };
        userBudgetRef.addValueEventListener(userBudgetListener);



        return root;
    }
}