package com.malisius.monefy.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.Collections;
import java.util.Random;

public class HomeFragment extends Fragment {

    private LinearLayout incomeInsertPoint, expenseInsertPoint, budgetInsertPoint;
    private View categoryItem, show_more;

    private ArrayList<TextView> textViews = new ArrayList<TextView>();

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private ArrayList<Category> mCategoriesList = new ArrayList<Category>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_home, container, false);

        //get insert point for category item
        incomeInsertPoint = root.findViewById(R.id.inflater_income);
        handleIncome();

        expenseInsertPoint = root.findViewById(R.id.inflater_expense);
        handleExpense();

        budgetInsertPoint = root.findViewById(R.id.inflater_budget);
        handleBudget();

        return root;
    }

    private void handleIncome(){
        for(int i=0; i < 4; i++ ){
            categoryItem = getLayoutInflater().inflate(R.layout.income_item_layout, null);
            incomeInsertPoint.addView(categoryItem);
        }
        show_more = getLayoutInflater().inflate(R.layout.show_more_layout, null);
        incomeInsertPoint.addView(show_more);
    }

    private void handleExpense(){
        for(int i=0; i < 4; i++ ){
            categoryItem = getLayoutInflater().inflate(R.layout.expense_item_layout, null);
            expenseInsertPoint.addView(categoryItem);
        }
        show_more = getLayoutInflater().inflate(R.layout.show_more_layout, null);
        expenseInsertPoint.addView(show_more);
    }

    private void handleBudget(){
        for(int i=0; i < 4; i++ ){
            categoryItem = getLayoutInflater().inflate(R.layout.budget_item_layout, null);
            budgetInsertPoint.addView(categoryItem);
        }
        show_more = getLayoutInflater().inflate(R.layout.show_more_layout, null);
        budgetInsertPoint.addView(show_more);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
//        Category cat = new Category("Fashion", 00400);
        ArrayList<Income> income = new ArrayList<Income>();
//        income.add(new Income("gaji", 10000));
//        income.add(new Income("parttime", 10000));
        ArrayList<Expense> expense = new ArrayList<Expense>();
//        expense.add(new Expense("baso", 2000));
//        expense.add(new Expense("nasgor", 2000));
//        cat.setExpenses(expense);
//        cat.setIncomes(income);
//        String categoryKey = mDatabase.getReference().child(mAuth.getUid()).push().getKey();
//        mDatabase.getReference().child(mAuth.getUid()).child(categoryKey).setValue(cat);
//        mDatabase.getReference().child(mAuth.getUid()).child("food").child("Expense").push().setValue(new Expense("Baso",5000));
//        mDatabase.getReference().child(mAuth.getUid()).child("food").child("Expense").push().setValue(new Expense("Baso",5000));
//        DatabaseReference categoryDataRef = mDatabase.getReference().child("Data").child(userDataRef).child("Categories");
//        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.inflater_income, userDataRef);

        DatabaseReference userDataRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Categories");

        ValueEventListener userDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Log.w("HomeFragment", "No Children");
                } else {
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
//                        for(int i=0;i<dataSnapshot.child(""))
//                        for(int i=0;i<dataSnapshot.getChildrenCount();i++) {
                            Log.i("HomeFragment", dataSnapshot.getValue().toString());

                            mCategoriesList.add(new Category(dataSnapshot.child("name").getValue().toString(),dataSnapshot.child("color").getValue().hashCode()));
//                            mCategoriesList.get(i).setName(dataSnapshot.child("name").getValue().toString());
//                            mCategoriesList.get(i).setColor(dataSnapshot.child("color").getValue().hashCode());
//                            mCategoriesList.get(i).setIncomes(expense.add(new Income(dataSnapshot.child("totalIncome").getValue().toString());
//                            mCategoriesLis.new ().dataSnapshot.child("name").getValue().toString()

//                        mCategoriesList.add(dataSnapshot.getValue(Category.class));
                            Log.i("HomeFragment", "hello");

//                        }
                    }
                    Collections.sort(mCategoriesList, Category.categoryNameComparator);

//                    for(int i = 0; i < 4; i++){
//                        TextView textView = textViews.get(i);
//                        textView.setText(mCategoriesList.get(i).getName());
//                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("HomeFragment", "loadPost:onCancelled", error.toException());
            }
        };
        userDataRef.addValueEventListener(userDataListener);

    }
}