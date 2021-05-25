package com.malisius.monefy.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.malisius.monefy.R;


public class EditUserDialog {
    public void showDialog(Context context){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myDialogView = inflater.inflate(R.layout.dialog_edit_user, null);

        myDialog.setView(myDialogView);
        myDialog.setCancelable(true);

        Button submitDialog = myDialogView.findViewById(R.id.btnYes);
        Button cancelDialog = myDialogView.findViewById(R.id.btnNo);
        ImageView imageView = myDialogView.findViewById(R.id.userImage);

        final AlertDialog dialog = myDialog.create();

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
