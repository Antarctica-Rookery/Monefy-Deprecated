package com.malisius.monefy.records;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButtonToggleGroup;
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

import java.util.ArrayList;
import java.util.Random;

public class RecordDialog {
    public void showDialog(Context context, Boolean isEdit, Boolean isIncome, String categoryName){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myDialogView = inflater.inflate(R.layout.dialog_edit_record, null);

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        myDialog.setView(myDialogView);
        myDialog.setCancelable(true);

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
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
