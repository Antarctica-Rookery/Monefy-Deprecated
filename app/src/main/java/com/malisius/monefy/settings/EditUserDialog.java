package com.malisius.monefy.settings;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.malisius.monefy.R;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;


public class EditUserDialog extends DialogFragment {
    private static final int CAMERA_REQ = 1;
    private Button submitDialog, cancelDialog;
    private TextInputLayout TILusername;
    private ImageView imageView;
    private Bitmap photo;
    private Uri downloadUrl;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private FirebaseUser user;
    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_edit_user, container, false);

        submitDialog = rootView.findViewById(R.id.btnYes);
        cancelDialog = rootView.findViewById(R.id.btnNo);
        imageView = rootView.findViewById(R.id.userImage);
        TILusername = rootView.findViewById(R.id.user_username);

        user = mAuth.getCurrentUser();

        Glide.with(getContext()).load(user.getPhotoUrl()).placeholder(R.drawable.ic_baseline_account_circle_24).circleCrop().into(imageView);

        DatabaseReference userReference = mDatabase.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("username");
        ValueEventListener userEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null) {
                    TILusername.getEditText().setText(snapshot.getValue().toString());
                } else {
                    TILusername.getEditText().setText(mAuth.getCurrentUser().getDisplayName());
                    TILusername.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        userReference.addValueEventListener(userEventListener);

        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        submitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference usernameReference = mDatabase.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("username");

                boolean isUsernameOk = validate();
                if(isUsernameOk){
                    usernameReference.setValue(TILusername.getEditText().getText().toString());
                    dismiss();
                }

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("SettingDialog","Camera Pressed");
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQ);
            }
        });

        return rootView;
    }

//    public void showDialog(Context context){
//        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View myDialogView = inflater.inflate(R.layout.dialog_edit_user, null);
//
//        myDialog.setView(myDialogView);
//        myDialog.setCancelable(true);
//
//        Button submitDialog = myDialogView.findViewById(R.id.btnYes);
//        Button cancelDialog = myDialogView.findViewById(R.id.btnNo);
//        ImageView imageView = myDialogView.findViewById(R.id.userImage);
//
//        final AlertDialog dialog = myDialog.create();
//
//        cancelDialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        submitDialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//
//            }
//        });
//
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                context.startActivity(cameraIntent);
//            }
//        });
//
//
//
//
//        dialog.show();
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQ && resultCode == getActivity().RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            Log.i("SettingDialog","Camera Openned");
            submit();
        }
    }

    public void submit(){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] b = stream.toByteArray();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profile_image").child(mAuth.getUid());
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Updating Profile, Please Wait");
        progressDialog.show();
        storageReference.putBytes(b).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        downloadUrl = uri;
                        Log.i("SettingDialog", downloadUrl.toString());

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(uri)
                                .build();


                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            DatabaseReference usernameReference = mDatabase.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("username");

                                            boolean isUsernameOk = validate();
                                            if(isUsernameOk){
                                                usernameReference.setValue(TILusername.getEditText().getText().toString());
                                                progressDialog.dismiss();
                                                dismiss();
                                            }
                                        }
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(getContext(),"failed",Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"failed",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

    }

    private boolean validate(){
        final boolean[] isOk = new boolean[1];
        isOk[0] = true;
        DatabaseReference userDataRef = mDatabase.getReference().child("Users");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userDataSnapshot : dataSnapshot.getChildren()) {
                    Log.i("RegisterActivity", userDataSnapshot.child("username").getValue().toString());
                    if (TILusername.getEditText().getText().toString().equals(userDataSnapshot.child("username").getValue().toString())) {
                        isOk[0] = false;
                        TILusername.setError("Username Has been Taken");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        userDataRef.addListenerForSingleValueEvent(eventListener);

        return isOk[0];
    }

}
