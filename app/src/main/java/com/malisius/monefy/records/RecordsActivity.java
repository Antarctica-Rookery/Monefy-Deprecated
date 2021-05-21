package com.malisius.monefy.records;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.malisius.monefy.category.CategoryListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RecordsActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Category category;
    private String key;
    private RecyclerView recyclerView;
    private TextView tvStartDate, tvEndDate, tvNoRecords;
    private Button btnStartDate, btnEndDate;
    private ArrayList<Income> mIncome = new ArrayList<Income>();
    private ArrayList<Expense> mExpense = new ArrayList<Expense>();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yy");
    private DatePicker startDatePicker, endDatePicker;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        Bundle catBundle = getIntent().getBundleExtra("catBundle");
        String name = catBundle.get("name").toString();
        String type = catBundle.get("type").toString();
        DatabaseReference catDataRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Categories");
        tvStartDate = findViewById(R.id.tv_start_date);
        tvEndDate = findViewById(R.id.tv_end_date);
        tvNoRecords = findViewById(R.id.tv_no_record);
        recyclerView = findViewById(R.id.recyclerView_records);
        btnStartDate = findViewById(R.id.button_start_date);
        btnEndDate = findViewById(R.id.button_end_date);
        startDatePicker = new DatePicker(this);
        endDatePicker = new DatePicker(this);
        ValueEventListener catDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Log.w("HomeFragment", "No Children");
                } else {
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        if(dataSnapshot.child("name").getValue().toString().equals(name)){
                            Log.w("HomeFragment", "Found");
                            key = dataSnapshot.getKey();
                            category = new Category(dataSnapshot.child("name").getValue().toString(), dataSnapshot.child("color").getValue().toString());
                            setData(type);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("HomeFragment", "loadPost:onCancelled", error.toException());
            }
        };
        catDataRef.addValueEventListener(catDataListener);

        DatePickerDialog startDatePickerDialog = new DatePickerDialog(RecordsActivity.this);
        startDatePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        endDatePicker = view;
                Calendar cal = new Calendar.Builder().setDate(year, month, dayOfMonth).build();
                if(!cal.getTime().after(new Date())){
                    String date = sdf.format(cal.getTime());
                    tvEndDate.setText(date);
                }
                endDatePicker.updateDate(year, month, dayOfMonth);
            }
        });

        DatePickerDialog endDatePickerDialog = new DatePickerDialog(RecordsActivity.this);
        endDatePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar cal = new Calendar.Builder().setDate(year, month, dayOfMonth).build();
                if (!cal.getTime().after(new Date())) {
                    String date = sdf.format(cal.getTime());
                    tvEndDate.setText(date);
                }
                endDatePicker.updateDate(year, month, dayOfMonth);
            }
        });

        btnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDatePickerDialog.show();
            }
        });

        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDatePickerDialog.show();
            }
        });

    }


    private void setData(String type){
        if(type.equals("income")){

            if(category.getIncomes() != null){
                mIncome = category.getIncomes();
                recyclerView.setVisibility(View.VISIBLE);
                tvNoRecords.setVisibility(View.GONE);
                Date startDate = mIncome.get(0).getDate();
                Date endDate = mIncome.get(mIncome.size() - 1).getDate();
                tvStartDate.setText(sdf.format(startDate));
                tvEndDate.setText(sdf.format(endDate));
                IncomeListAdapter adapter = new IncomeListAdapter(category.getIncomes());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            } else {
                tvStartDate.setText(sdf.format(new Date()));
                tvEndDate.setText(sdf.format(new Date()));
            }
        } else {
            if(category.getExpenses() != null){
                mExpense = category.getExpenses();
                recyclerView.setVisibility(View.VISIBLE);
                tvNoRecords.setVisibility(View.GONE);
                Date startDate = mExpense.get(0).getDate();
                Date endDate = mExpense.get(mExpense.size() - 1).getDate();
                tvStartDate.setText(sdf.format(startDate));
                tvEndDate.setText(sdf.format(endDate));
                ExpenseListAdapter adapter = new ExpenseListAdapter(category.getExpenses());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            } else {
                Date now = new Date();
                tvStartDate.setText(sdf.format(now));
                tvEndDate.setText(sdf.format(now));
            }
        }

    }

}
