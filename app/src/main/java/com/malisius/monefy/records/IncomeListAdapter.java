package com.malisius.monefy.records;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.malisius.monefy.Income;
import com.malisius.monefy.R;
import com.malisius.monefy.category.Category;
import com.malisius.monefy.category.CategoryDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class IncomeListAdapter extends RecyclerView.Adapter<IncomeListAdapter.IncomeViewHolder> {
    private ArrayList<Income> mIncome = new ArrayList<Income>();
    private Context context;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yy");

    public IncomeListAdapter(ArrayList<Income> income){
        mIncome = income;
    }

    @NonNull
    @Override
    public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View incomeView = inflater.inflate(R.layout.record_item_layout, parent, false);

        // Return a new holder instance
        IncomeViewHolder viewHolder = new IncomeViewHolder(incomeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeViewHolder holder, int position) {
        // Get the data model based on position
        Income income = mIncome.get(position);

        // Set item views based on your views and data model
        TextView recordName = holder.recordName;
        recordName.setText(income.getName());

        TextView recordValue = holder.recordValue;
        recordValue.setText(income.getValue());

        TextView recordDate = holder.recordDate;
        String date = sdf.format(income.getDate());
        recordDate.setText(date);


    }

    @Override
    public int getItemCount() {
        return mIncome.size();
    }

    public class IncomeViewHolder extends RecyclerView.ViewHolder{
        public TextView recordName, recordValue, recordDate;

        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);

            recordName = itemView.findViewById(R.id.tv_recordName);
            recordValue = itemView.findViewById(R.id.record_value);
            recordDate = itemView.findViewById(R.id.record_date);

        }
    }
}
