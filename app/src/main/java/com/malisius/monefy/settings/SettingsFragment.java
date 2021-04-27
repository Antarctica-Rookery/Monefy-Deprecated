package com.malisius.monefy.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.malisius.monefy.MainActivity;
import com.malisius.monefy.R;
import com.malisius.monefy.about_us.AboutUsActivity;
import com.malisius.monefy.category.CategoryActivity;

public class SettingsFragment extends Fragment {
    LinearLayout aboutUs, categories, logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

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