package com.malisius.monefy.budget;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malisius.monefy.R;

import java.util.ArrayList;

import app.futured.donut.DonutProgressView;
import app.futured.donut.DonutSection;

public class BudgetFragment extends Fragment {
    private DonutProgressView donutProgressView;
    private ArrayList<DonutSection> sections = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_budget, container, false);

        //using donut progress
        donutProgressView = root.findViewById(R.id.donut_view);

        sections.add(new DonutSection("Category 1", Color.parseColor("#aed581"), 2.0f));
        sections.add(new DonutSection("Category 2", Color.parseColor("#8bc34a"), 0.2f));
        sections.add(new DonutSection("Category 3", Color.parseColor("#689f38"), 0.7f));
        donutProgressView.setCap(3.0f);
        donutProgressView.submitData(sections);

        return root;
    }
}