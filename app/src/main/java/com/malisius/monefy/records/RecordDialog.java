package com.malisius.monefy.records;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malisius.monefy.R;
import com.malisius.monefy.budget.Budget;
import com.malisius.monefy.category.Category;
import com.malisius.monefy.expense.Expense;
import com.malisius.monefy.income.Income;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RecordDialog {
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private ArrayList<String> mCategoriesList = new ArrayList<String>();
    private Category getCat;
    private Date selectedDate;
    private String oldCatName;
    private Boolean isIncome;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showDialog(Context context, Boolean isEdit, Boolean isIncomeParent, String categoryName, int position){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myDialogView = inflater.inflate(R.layout.dialog_edit_record, null);

        myDialog.setView(myDialogView);
        myDialog.setCancelable(true);

        isIncome = isIncomeParent;

        // Handle DatePicker
        DatePicker dateNow = new DatePicker(context);

        TextInputLayout TILAmount = myDialogView.findViewById(R.id.tiamount);
        TextInputLayout TILName = myDialogView.findViewById(R.id.tiname);
        TextInputEditText tidate = myDialogView.findViewById(R.id.tieditdate);
        tidate.setText(sdf.format(new Date()));

        DatePickerDialog datePickerDialog = new DatePickerDialog(context);
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = new Calendar.Builder().setDate(year, month, dayOfMonth).setTimeOfDay(12,00,00).build();
                if(!calendar.getTime().after(new Date())){

                    String date = sdf.format(calendar.getTime());
                    selectedDate = calendar.getTime();
                    tidate.setText(date);
                    dateNow.updateDate(year, month, dayOfMonth);
                }
            }
        });

        tidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        // Handle DatePicker //

        // Handle Dropdown Categories //
        AutoCompleteTextView categoriesName = myDialogView.findViewById(R.id.categoryName);
        categoriesName.setText(categoryName);

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
                        mCategoriesList.add(dataSnapshot.getValue(Category.class).getName());
                        if(dataSnapshot.child("name").getValue().toString().equals(categoryName)){
                            getCat = dataSnapshot.getValue(Category.class);
                            if(isEdit){
                                if(isIncome) {
                                    ArrayList<Income> placeIncome = getCat.getIncomes();
                                    TILAmount.getEditText().setText(String.valueOf(placeIncome.get(position).getValue()));
                                    TILName.getEditText().setText(placeIncome.get(position).getName());
                                    selectedDate = new Date(placeIncome.get(position).getDate());
                                } else {
                                    ArrayList<Expense> placeExpense = getCat.getExpenses();
                                    TILAmount.getEditText().setText(String.valueOf(placeExpense.get(position).getValue()));
                                    TILName.getEditText().setText(placeExpense.get(position).getName());
                                    selectedDate = new Date(placeExpense.get(position).getDate());
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        userDataRef.addListenerForSingleValueEvent(userDataListener);
        ArrayAdapter arrayAdapter = new ArrayAdapter(context, R.layout.dropdown_categories, mCategoriesList);
        categoriesName.setAdapter(arrayAdapter);
        // Handle Dropdown Categories //

        Button submitDialog = myDialogView.findViewById(R.id.btnYes);
        Button cancelDialog = myDialogView.findViewById(R.id.btnNo);
        ImageView deleteDialog = myDialogView.findViewById(R.id.deleteIcon);
        MaterialButtonToggleGroup materialButtonToggleGroup = myDialogView.findViewById(R.id.toggleGroup);

        if(isIncome){
            materialButtonToggleGroup.check(R.id.income);
        }else {
            materialButtonToggleGroup.check(R.id.expense);
        }

        TextView operation = myDialogView.findViewById(R.id.tveditrecord);

        if(isEdit) {
            operation.setText("Edit Record");
            deleteDialog.setVisibility(View.VISIBLE);
            oldCatName = categoryName;
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
                DatabaseReference userCatRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Categories");
                ValueEventListener userCatListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.exists()){
                            Log.w("ExpenseFragment", "No Child Exist");
                        } else {
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                if(dataSnapshot.child("name").getValue().equals(categoriesName.getText().toString())){
                                    String key = dataSnapshot.getKey();
                                    Category category = dataSnapshot.getValue(Category.class);
                                    if(isIncome){
                                        ArrayList<Income> incomes = category.getIncomes();
                                        incomes.remove(position);
                                        category.setIncomes(incomes);
                                        DatabaseReference userIncomeRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Categories");
                                        userIncomeRef.child(key).setValue(category);
                                        dialog.dismiss();
                                    } else {
                                        ArrayList<Expense> expenses = category.getExpenses();
                                        int minusValue = expenses.get(position).getValue();
                                        expenses.remove(position);
                                        category.setExpenses(expenses);
                                        DatabaseReference userIncomeRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Categories");
                                        userIncomeRef.child(key).setValue(category);
                                        DatabaseReference userBudgetRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Budget").child(key);
                                        ValueEventListener userBudgetListener = new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                Budget budget = snapshot.getValue(Budget.class);
                                                int budgetValue = budget.getValue();
                                                budget.setValue(budgetValue - minusValue);
                                                DatabaseReference newBudgetRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Budget").child(key);
                                                newBudgetRef.setValue(budget);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                            }
                                        };
                                        userBudgetRef.addListenerForSingleValueEvent(userBudgetListener);
                                        dialog.dismiss();
                                    }

                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                userCatRef.addListenerForSingleValueEvent(userCatListener);
                dialog.dismiss();
            }
        });

        submitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(materialButtonToggleGroup.getCheckedButtonId() == R.id.income) isIncome = true;
                else isIncome = false;
                if(!TILAmount.getEditText().getText().toString().isEmpty() && !categoriesName.getText().toString().equals("Category name")){
                    Log.i("selected", String.valueOf(categoriesName.getText()));
                    DatabaseReference userCatRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Categories");
                    ValueEventListener userCatListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(!snapshot.exists()){
                                Log.w("ExpenseFragment", "No Child Exist");
                            } else {
                                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                    if(isEdit && dataSnapshot.child("name").getValue().equals(oldCatName) && !categoriesName.getText().toString().equals(oldCatName)){
                                        if(isIncomeParent) {
                                            Category category = dataSnapshot.getValue(Category.class);
                                            String key = dataSnapshot.getKey();
                                            ArrayList<Income> incomes = category.getIncomes();
                                            incomes.remove(position);
                                            category.setIncomes(incomes);
                                            DatabaseReference userIncomeRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Categories");
                                            userIncomeRef.child(key).setValue(category);
                                        } else {
                                            Category category = dataSnapshot.getValue(Category.class);
                                            String key = dataSnapshot.getKey();
                                            ArrayList<Expense> expenses = category.getExpenses();
                                            expenses.remove(position);
                                            category.setExpenses(expenses);
                                            DatabaseReference userIncomeRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Categories");
                                            userIncomeRef.child(key).setValue(category);
                                        }
                                    }
                                    if(dataSnapshot.child("name").getValue().equals(categoriesName.getText().toString())){
                                        String key = dataSnapshot.getKey();
                                        Category category = dataSnapshot.getValue(Category.class);
                                        int oldValue = 0;
                                        int localValue = 0;
                                        if (selectedDate == null){
                                            selectedDate = new Date();
                                        }
                                        if(selectedDate.after(new Date())){
                                            selectedDate = new Date();
                                        }
                                        if(isIncome){
                                            int value = Integer.parseInt(TILAmount.getEditText().getText().toString());
                                            localValue = value;
                                            Income income = new Income(TILName.getEditText().getText().toString(), value, selectedDate.getTime());
                                            ArrayList<Income> incomes = category.getIncomes();
                                            if(incomes == null){
                                                incomes = new ArrayList<Income>();
                                            }
                                            if(isEdit && categoriesName.getText().toString().equals(oldCatName) && isIncomeParent){
                                                incomes.get(position).setValue(value);
                                                incomes.get(position).setName(TILName.getEditText().getText().toString());
                                                incomes.get(position).setDate(selectedDate.getTime());
                                            }
                                            else if(isEdit && categoriesName.getText().toString().equals(oldCatName) && !isIncomeParent){
                                                ArrayList<Expense> expenses = category.getExpenses();
                                                expenses.remove(position);
                                                category.setExpenses(expenses);
                                                incomes.add(income);
                                            }
                                            else {
                                                incomes.add(income);
                                            }
                                            category.setIncomes(incomes);
                                            DatabaseReference userIncomeRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Categories");
                                            userIncomeRef.child(key).setValue(category);
                                        } else {
                                            int value = Integer.parseInt(TILAmount.getEditText().getText().toString());
                                            localValue = value;
                                            Expense expense = new Expense(TILName.getEditText().getText().toString(), value, selectedDate.getTime());
                                            ArrayList<Expense> expenses = category.getExpenses();
                                            if (expenses == null) {
                                                expenses = new ArrayList<Expense>();
                                            }
                                            if (isEdit && categoriesName.getText().toString().equals(oldCatName) && !isIncomeParent) {
                                                oldValue = expenses.get(position).getValue();
                                                expenses.get(position).setValue(value);
                                                expenses.get(position).setName(TILName.getEditText().getText().toString());
                                                expenses.get(position).setDate(selectedDate.getTime());
                                            } else if(isEdit && categoriesName.getText().toString().equals(oldCatName) && isIncomeParent){
                                                ArrayList<Income> incomes = category.getIncomes();
                                                incomes.remove(position);
                                                category.setIncomes(incomes);
                                                expenses.add(expense);
                                            } else {
                                                expenses.add(expense);
                                            }
                                            category.setExpenses(expenses);
                                            DatabaseReference userIncomeRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Categories");
                                            userIncomeRef.child(key).setValue(category);
                                            DatabaseReference userBudgetRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Budget").child(key);
                                            int finalOldValue = oldValue;
                                            int finalLocalValue = localValue;
                                            ValueEventListener userBudgetListener = new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                    Budget budget = snapshot.getValue(Budget.class);
                                                    int budgetValue = budget.getValue();
                                                    if(isEdit){
                                                        budget.setValue(budgetValue - finalOldValue);
                                                        budgetValue = budget.getValue();
                                                    }
                                                    budget.setValue(budgetValue+finalLocalValue);
                                                    DatabaseReference newBudgetRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Budget").child(key);
                                                    newBudgetRef.setValue(budget);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                                }
                                            };
                                            userBudgetRef.addListenerForSingleValueEvent(userBudgetListener);
                                        }

                                        dialog.dismiss();


                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    };
                    userCatRef.addListenerForSingleValueEvent(userCatListener);
                    dialog.dismiss();
                }

            }
        });
        dialog.show();

    }

    private void handleDatePicker(){

    }
}
