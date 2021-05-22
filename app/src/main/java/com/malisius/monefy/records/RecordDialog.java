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
import com.malisius.monefy.category.Category;
import com.malisius.monefy.expense.Expense;
import com.malisius.monefy.income.Income;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showDialog(Context context, Boolean isEdit, Boolean isIncome, String categoryName, int position){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myDialogView = inflater.inflate(R.layout.dialog_edit_record, null);

        myDialog.setView(myDialogView);
        myDialog.setCancelable(true);

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
                                        expenses.remove(position);
                                        category.setExpenses(expenses);
                                        DatabaseReference userIncomeRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Categories");
                                        userIncomeRef.child(key).setValue(category);
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
                                    if(dataSnapshot.child("name").getValue().equals(categoriesName.getText().toString())){
                                        String key = dataSnapshot.getKey();
                                        Category category = dataSnapshot.getValue(Category.class);
                                        if (selectedDate == null){
                                            selectedDate = new Date();
                                        }
                                        if(selectedDate.after(new Date())){
                                            selectedDate = new Date();
                                        }
                                        if(isIncome){
                                            int value = Integer.parseInt(TILAmount.getEditText().getText().toString());
                                            Income income = new Income(TILName.getEditText().getText().toString(), value, selectedDate.getTime());
                                            ArrayList<Income> incomes = category.getIncomes();
                                            if(incomes == null){
                                                incomes = new ArrayList<Income>();
                                            }
                                            if(isEdit){
                                                incomes.get(position).setValue(value);
                                                incomes.get(position).setName(TILName.getEditText().getText().toString());
                                                incomes.get(position).setDate(selectedDate.getTime());
                                            }else {
                                                incomes.add(income);
                                            }
                                            category.setIncomes(incomes);
                                            DatabaseReference userIncomeRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Categories");
                                            userIncomeRef.child(key).setValue(category);
                                            dialog.dismiss();
                                        } else {
                                            int value = Integer.parseInt(TILAmount.getEditText().getText().toString());
                                            Expense expense = new Expense(TILName.getEditText().getText().toString(), value, selectedDate.getTime());
                                            ArrayList<Expense> expenses = category.getExpenses();
                                            if(expenses == null){
                                                expenses = new ArrayList<Expense>();
                                            }
                                            if(isEdit){
                                                expenses.get(position).setValue(value);
                                                expenses.get(position).setName(TILName.getEditText().getText().toString());
                                                expenses.get(position).setDate(selectedDate.getTime());
                                            } else {
                                                expenses.add(expense);
                                            }
                                            category.setExpenses(expenses);
                                            DatabaseReference userIncomeRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Categories");
                                            userIncomeRef.child(key).setValue(category);
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

            }
        });
        dialog.show();

    }

    private void handleDatePicker(){

    }
}
