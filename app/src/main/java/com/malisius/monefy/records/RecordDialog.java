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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malisius.monefy.R;
import com.malisius.monefy.category.Category;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RecordDialog {
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private ArrayList<String> mCategoriesList = new ArrayList<String>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showDialog(Context context, Boolean isEdit, Boolean isIncome, String categoryName){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myDialogView = inflater.inflate(R.layout.dialog_edit_record, null);

        myDialog.setView(myDialogView);
        myDialog.setCancelable(true);

        // Handle DatePicker
        DatePicker dateNow = new DatePicker(context);

        TextInputEditText tidate = myDialogView.findViewById(R.id.tieditdate);
        tidate.setText(sdf.format(new Date()));

        DatePickerDialog datePickerDialog = new DatePickerDialog(context);
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = new Calendar.Builder().setDate(year, month, dayOfMonth).build();
                if(!calendar.getTime().after(new Date())){
                    String date = sdf.format(calendar.getTime());
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
                dialog.dismiss();
            }
        });

        submitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("selected", String.valueOf(categoriesName.getText()));
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void handleDatePicker(){

    }
}
