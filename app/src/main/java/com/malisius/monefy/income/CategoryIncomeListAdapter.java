package com.malisius.monefy.income;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.malisius.monefy.R;
import com.malisius.monefy.category.Category;
import com.malisius.monefy.category.CategoryDialog;
import com.malisius.monefy.records.RecordsActivity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CategoryIncomeListAdapter extends RecyclerView.Adapter<com.malisius.monefy.income.CategoryIncomeListAdapter.CategoryIncomeViewHolder> {
    private ArrayList<Category> mCategory = new ArrayList<Category>();
    private Context context;

    public CategoryIncomeListAdapter(ArrayList<Category> category){
        mCategory = category;
    }

    private String formatRupiah(int number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    @NonNull
    @Override
    public CategoryIncomeListAdapter.CategoryIncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View categoryView = inflater.inflate(R.layout.income_item_layout, parent, false);

        // Return a new holder instance
        CategoryIncomeListAdapter.CategoryIncomeViewHolder viewHolder = new CategoryIncomeListAdapter.CategoryIncomeViewHolder(categoryView);
        return viewHolder;
    }

    private void openDetail(Bundle bundle){
        Intent recordsIntent = new Intent(context, RecordsActivity.class);
        recordsIntent.putExtra("catBundle", bundle);
        context.startActivity(recordsIntent);

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryIncomeListAdapter.CategoryIncomeViewHolder holder, int position) {
        // Get the data model based on position
        Category category = mCategory.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.categoryName;
        textView.setText(category.getName());

        TextView categoryTotal = holder.categoryTotal;
        categoryTotal.setText(formatRupiah(category.getTotalIncome()));

        View colorBar = holder.colorBar;
        colorBar.setBackgroundColor(Color.parseColor(category.getColor()));

        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("name", category.getName());
                bundle.putString("type", "income");
                openDetail(bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategory.size();
    }

    public class CategoryIncomeViewHolder extends RecyclerView.ViewHolder{
        public TextView categoryName, categoryTotal;
        public View colorBar;
        public LinearLayout ll;

        public CategoryIncomeViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.tv_income_categoryName);
            categoryTotal = itemView.findViewById(R.id.income_category_total);
            colorBar = itemView.findViewById(R.id.income_color_bar);
            ll = itemView.findViewById(R.id.ll);
        }
    }
}
