package com.malisius.monefy.settings;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malisius.monefy.HomeActivity;
import com.malisius.monefy.MainActivity;
import com.malisius.monefy.R;
import com.malisius.monefy.about_us.AboutUsActivity;
import com.malisius.monefy.category.CategoryActivity;
import com.malisius.monefy.records.RecordDialog;

import java.io.ByteArrayOutputStream;

public class SettingsFragment<ImageView> extends Fragment {
    private static final int CAMERA_REQ = 1;
    LinearLayout aboutUs, categories, logout;
    ImageView imageView;
    TextView usernameTv, emailTv;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    private FloatingActionButton fabButton;
    private CardView cardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        ConstraintLayout rootParent = (ConstraintLayout) container.getParent();

        cardView = root.findViewById(R.id.user);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EditUserDialog editUserDialog = new EditUserDialog();
//                editUserDialog.showDialog(getContext());
                FragmentManager fm = getChildFragmentManager();
                EditUserDialog dialogFragment = new EditUserDialog ();
                dialogFragment.show(fm, "Sample Fragment");
            }
        });

        imageView = (ImageView) root.findViewById(R.id.user_image);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Glide.with(getContext()).load(user.getPhotoUrl()).placeholder(R.drawable.ic_baseline_account_circle_24).circleCrop().into((android.widget.ImageView) imageView);

        fabButton = rootParent.findViewById(R.id.floatingActionButton);
        fabButton.setVisibility(View.GONE);

        //init Firebase to get username and email
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        //change username and email placeholder
        usernameTv = root.findViewById(R.id.username);
        emailTv = root.findViewById(R.id.email);
        DatabaseReference userReference = mDatabase.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("username");
        ValueEventListener userEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null) {
                    usernameTv.setText(snapshot.getValue().toString());
                } else {
                    usernameTv.setText(mAuth.getCurrentUser().getDisplayName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        userReference.addValueEventListener(userEventListener);
        emailTv.setText(mAuth.getCurrentUser().getEmail());

        //go to about us
        aboutUs = root.findViewById(R.id.aboutus);
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutUsActivity.class);
                startActivity(intent);
            }
        });

        //go to categories
        categories = root.findViewById(R.id.categories);
        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                startActivity(intent);
            }
        });

        //go to main activity
        logout = root.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        return root;
    }


}
