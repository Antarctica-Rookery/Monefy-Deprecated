package com.malisius.monefy.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.malisius.monefy.records.RecordsActivity;
import com.malisius.monefy.budget.Budget;
import com.malisius.monefy.category.Category;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private LinearLayout incomeInsertPoint, expenseInsertPoint, budgetInsertPoint;
    private View categoryItem, show_more;

    private ArrayList<TextView> textViews = new ArrayList<TextView>();

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private ArrayList<Category> mCategoriesList = new ArrayList<Category>();
    private ArrayList<Budget> mBudgetList = new ArrayList<Budget>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_home, container, false);


        handleTotal(root);

        //get insert point for category item
        incomeInsertPoint = root.findViewById(R.id.inflater_income);

        expenseInsertPoint = root.findViewById(R.id.inflater_expense);

        budgetInsertPoint = root.findViewById(R.id.inflater_budget);


        return root;
    }

    private String formatRupiah(int number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    private void handleTotal(View root){
        TextView TvTotalIncome = root.findViewById(R.id.tv_total_income);
        TextView TvTotalExpense = root.findViewById(R.id.tv_total_expense);
        TextView TvTotalBudget = root.findViewById(R.id.tv_total_budget);
        int totalIncome = 0;
        int totalExpense = 0;
        int totalBudget = 0;
        for(Category category: mCategoriesList){
            totalIncome = totalIncome + category.getTotalIncome();
            totalExpense = totalExpense + category.getTotalExpense();
        }
        for(Budget budget: mBudgetList){
            totalBudget = totalBudget + budget.getLimit();
        }
        TvTotalIncome.setText(formatRupiah(totalIncome));
        TvTotalExpense.setText(formatRupiah(totalExpense));
        TvTotalBudget.setText(formatRupiah(totalBudget));
    }

    private void handleIncome(){

        for(int i=0; i < 4; i++ ){
            categoryItem = getLayoutInflater().inflate(R.layout.income_item_layout, null);
            TextView categoryName = categoryItem.findViewById(R.id.tv_incomecategoryName);
            TextView categoryTotal = categoryItem.findViewById(R.id.income_category_total);
            ImageView detailButton = categoryItem.findViewById(R.id.detail_income_button);
            View categoryBar = categoryItem.findViewById(R.id.income_color_bar);
            categoryBar.setBackgroundColor(Color.parseColor(mCategoriesList.get(i).getColor()));
            categoryName.setText(mCategoriesList.get(i).getName());
            categoryTotal.setText(formatRupiah(mCategoriesList.get(i).getTotalIncome()));
            String catName = mCategoriesList.get(i).getName();
            detailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("name", catName);
                    bundle.putString("type", "income");
                    openDetail(bundle);
                }
            });
            incomeInsertPoint.addView(categoryItem);
        }
        show_more = getLayoutInflater().inflate(R.layout.show_more_layout, null);

        //go to income activity
        show_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), getActivity().findViewById(R.id.fragment).getId());
                navController.navigate(R.id.incomeFragment);
            }
        });

        incomeInsertPoint.addView(show_more);
    }




    private void handleExpense(){

        for(int i=0; i < 4; i++ ){
            categoryItem = getLayoutInflater().inflate(R.layout.expense_item_layout, null);
            TextView categoryName = categoryItem.findViewById(R.id.tv_expense_categoryName);
            TextView categoryTotal = categoryItem.findViewById(R.id.expense_category_total);
            View categoryBar = categoryItem.findViewById(R.id.expense_color_bar);
            ImageView detailButton = categoryItem.findViewById(R.id.detail_expense_button);
            categoryBar.setBackgroundColor(Color.parseColor(mCategoriesList.get(i).getColor()));
            categoryName.setText(mCategoriesList.get(i).getName());
            categoryTotal.setText(formatRupiah(mCategoriesList.get(i).getTotalExpense()));
            String catName = mCategoriesList.get(i).getName();
            detailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("name", catName);
                    bundle.putString("type", "expense");
                    openDetail(bundle);
                }
            });
            expenseInsertPoint.addView(categoryItem);
        }
        show_more = getLayoutInflater().inflate(R.layout.show_more_layout, null);

        //go to expense activity
        show_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), getActivity().findViewById(R.id.fragment).getId());
                navController.navigate(R.id.expenseFragment);
            }
        });

        expenseInsertPoint.addView(show_more);
    }

    private void openDetail(Bundle bundle){
        Intent recordsIntent = new Intent(getContext(), RecordsActivity.class);
        recordsIntent.putExtra("catBundle", bundle);
        startActivity(recordsIntent);

    }
    private void handleBudget(){
        for(int i=0; i < 4; i++ ){
            categoryItem = getLayoutInflater().inflate(R.layout.budget_item_layout, null);
            TextView budgetName = categoryItem.findViewById(R.id.item_budget_name);
            TextView budgetLimit = categoryItem.findViewById(R.id.item_budget_total);
            TextView budgetValue = categoryItem.findViewById(R.id.item_budget_left);
            ProgressBar progressBar = categoryItem.findViewById(R.id.progressBar4);
            budgetName.setText(mBudgetList.get(i).getName());
            budgetLimit.setText(formatRupiah(mBudgetList.get(i).getLimit()));
            budgetValue.setText(formatRupiah(mBudgetList.get(i).getValue()));
            float progress;
            if(mBudgetList.get(i).getLimit() > 0){
                progress = (float) mBudgetList.get(i).getValue()/mBudgetList.get(i).getLimit();
            } else {
                progress = 100;
            }
            progressBar.setProgress((int) progress);

            budgetInsertPoint.addView(categoryItem);
        }
        show_more = getLayoutInflater().inflate(R.layout.show_more_layout, null);

        //go to budget activity
        show_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), getActivity().findViewById(R.id.fragment).getId());
                navController.navigate(R.id.budgetFragment);
            }
        });

        budgetInsertPoint.addView(show_more);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        ArrayList<Income> income = new ArrayList<Income>();

        ArrayList<Expense> expense = new ArrayList<Expense>();

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


                            mCategoriesList.add(new Category(dataSnapshot.child("name").getValue().toString(),dataSnapshot.child("color").getValue().toString()));
                            Log.i("HomeFragment", "hello");

                    }
                    Collections.sort(mCategoriesList, Category.categoryNameComparator);

                    handleIncome();
                    handleExpense();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("HomeFragment", "loadPost:onCancelled", error.toException());
            }
        };
        userDataRef.addValueEventListener(userDataListener);

        DatabaseReference userBudgetRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Budget");

        ValueEventListener userBudgetListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Log.w("HomeFragment", "No Children");
                } else {
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {

                        mBudgetList.add(new Budget(dataSnapshot.child("limit").getValue().hashCode(), dataSnapshot.child("value").getValue().hashCode(),dataSnapshot.child("name").getValue().toString()));
                        Log.i("HomeFragment", "hello");

//                        }
                    }
                    Collections.sort(mBudgetList, Budget.budgetComparatorDesc);

                    handleBudget();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("HomeFragment", "loadPost:onCancelled", error.toException());
            }
        };
        userBudgetRef.addValueEventListener(userBudgetListener);
    }
}