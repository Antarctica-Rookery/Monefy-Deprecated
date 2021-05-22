package com.malisius.monefy.income;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.malisius.monefy.R;
import com.malisius.monefy.category.Category;
import com.malisius.monefy.records.RecordsActivity;

import java.util.ArrayList;

public class IncomeFragmentAdapter extends RecyclerView.Adapter<com.malisius.monefy.income.IncomeFragmentAdapter.IncomeFragViewHolder> {
    private ArrayList<Category> mCategories = new ArrayList<Category>();
    private Context context;

    public IncomeFragmentAdapter(ArrayList<Category> categories){
        mCategories = categories;
    }

    @NonNull
    @Override
    public IncomeFragmentAdapter.IncomeFragViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View incomeView = inflater.inflate(R.layout.income_item_layout, parent, false);

        // Return a new holder instance
        IncomeFragmentAdapter.IncomeFragViewHolder viewHolder = new IncomeFragmentAdapter.IncomeFragViewHolder(incomeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeFragmentAdapter.IncomeFragViewHolder holder, int position) {
        // Get the data model based on position
        Category category = mCategories.get(position);

        // Set item views based on your views and data model
        TextView incomeName = holder.incomeName;
        incomeName.setText(category.getName());

        TextView incomeValue = holder.incomeValue;
        incomeValue.setText(category.getTotalIncome());


    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    public class IncomeFragViewHolder extends RecyclerView.ViewHolder{
        public TextView incomeName,incomeValue;

        public IncomeFragViewHolder(@NonNull View itemView) {
            super(itemView);

            incomeName = itemView.findViewById(R.id.tv_incomecategoryName);
            incomeValue = itemView.findViewById(R.id.income_category_total);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();

                    Context context = itemView.getContext();



                    Bundle bundle = new Bundle();
                    bundle.putString("name", mCategories.get(position).getName());
                    bundle.putString("type", "expense");

                    Intent recordsIntent = new Intent(context, RecordsActivity.class);
                    recordsIntent.putExtra("catBundle", bundle);
                    context.startActivity(recordsIntent);
                }
            });

        }
    }
}



