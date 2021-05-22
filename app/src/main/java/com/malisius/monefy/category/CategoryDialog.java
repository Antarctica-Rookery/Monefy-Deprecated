package com.malisius.monefy.category;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputLayout;
import com.malisius.monefy.R;

public class CategoryDialog {
    public void showAddDialog(Context context, String name, boolean edit){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myDialogView = inflater.inflate(R.layout.edit_category_layout, null);

        myDialog.setView(myDialogView);
        myDialog.setCancelable(true);

        ImageView deleteIcon = myDialogView.findViewById(R.id.deleteIcon);
        Button submitDialog = myDialogView.findViewById(R.id.btnYes);
        Button cancelDialog = myDialogView.findViewById(R.id.btnNo);

        TextInputLayout categoryName = myDialogView.findViewById(R.id.ticategory);

        if(name != null) {
            categoryName.getEditText().setText(name);
        }

        if(edit){
            deleteIcon.setVisibility(View.VISIBLE);
        } else {
            deleteIcon.setVisibility(View.GONE);
        }

        final AlertDialog dialog = myDialog.create();

        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete category
            }
        });

        cancelDialog.setOnClickListener(new View.OnClickListener() {
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
