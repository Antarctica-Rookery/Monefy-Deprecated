package com.malisius.monefy.income;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.malisius.monefy.Income;
import com.malisius.monefy.R;
import com.malisius.monefy.category.Category;
import com.malisius.monefy.records.RecordsActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class IncomeFragmentAdapter extends RecyclerView.Adapter<IncomeFragmentAdapter.IncomeFragViewHolder> {

    private final ArrayList<Category> categories;
    private LayoutInflater mInflater;

    IncomeFragmentAdapter(Context context, ArrayList<Category> mCategoriesList) {
        mInflater = LayoutInflater.from(context);
        categories = mCategoriesList;
    }

    @NonNull
    @Override
    public IncomeFragmentAdapter.IncomeFragViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.income_item_layout,
                parent, false);
        return new IncomeFragViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeFragmentAdapter.IncomeFragViewHolder holder, int position) {
        Category title = categories.get(position);

        TextView IncomeItemView = holder.IncomeItemView;
        IncomeItemView.setText(title.getName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class IncomeFragViewHolder extends RecyclerView.ViewHolder {
        public final TextView IncomeItemView;
        final ListAdapter iAdapter;

        public IncomeFragViewHolder(@NonNull View itemView, ListAdapter incomeAdapter) {
            super(itemView);
            IncomeItemView = itemView.findViewById(R.id.tv_incomecategoryName);
            this.iAdapter = incomeAdapter;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();

                    Context context = itemView.getContext();

                    Bundle bundle = new Bundle();
                    bundle.putString("name", categories.get(position).getName());
                    bundle.putString("type", "expense");

                    Intent recordsIntent = new Intent(context, RecordsActivity.class);
                    recordsIntent.putExtra("catBundle", bundle);
                    context.startActivity(recordsIntent);
                }
            });
        }
    }
}



