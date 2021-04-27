package com.malisius.monefy.expense;

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

public class ExpenseFragment extends Fragment {
    private DonutProgressView donutProgressView;
    private ArrayList<DonutSection> sections = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_expense, container, false);

        //using donut progress
        donutProgressView = root.findViewById(R.id.donut_view);

        sections.add(new DonutSection("Category 1", Color.parseColor("#ffab91"), 1.2f));
        sections.add(new DonutSection("Category 2", Color.parseColor("#ff8a65"), 0.7f));
        sections.add(new DonutSection("Category 3", Color.parseColor("#ff7043"), 0.3f));
        donutProgressView.setCap(3.0f);
        donutProgressView.submitData(sections);

        return root;
    }
}