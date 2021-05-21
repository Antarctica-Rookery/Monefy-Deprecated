package com.malisius.monefy.budget;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.malisius.monefy.R;
import com.malisius.monefy.category.Category;
import com.malisius.monefy.category.CategoryDialog;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class BudgetListAdapter extends RecyclerView.Adapter<BudgetListAdapter.BudgetViewHolder> {
    private ArrayList<Budget> mBudget = new ArrayList<Budget>();
    private Context context;

    public BudgetListAdapter(ArrayList<Budget> budget){
        mBudget = budget;
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View BudgetView = inflater.inflate(R.layout.budget_item_layout, parent, false);

        // Return a new holder instance
        BudgetViewHolder viewHolder = new BudgetViewHolder(BudgetView);
        return viewHolder;
    }

    private String formatRupiah(int number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(number);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        // Get the data model based on position
        Budget budget = mBudget.get(position);

        TextView budgetName = holder.budgetName;
        budgetName.setText(budget.getName());

        TextView budgetValue = holder.budgetValue;
        budgetValue.setText(formatRupiah(budget.getValue()));

        TextView budgetLimit = holder.budgetLimit;
        budgetLimit.setText(formatRupiah(budget.getLimit()));

        ProgressBar progressBar = holder.progressBar;
        if(budget.getLimit() > 0) {
            float progressFloat = ((float) budget.getValue() / budget.getLimit())*100;
            if(progressFloat > 100) {
                progressFloat = 100;
                progressBar.getProgressDrawable().setTint(context.getColor(R.color.red));
            }
            progressBar.setProgress((int) progressFloat);
        } else {
            progressBar.setProgress(100);
        }

        LinearLayout linearLayout = holder.linearLayout;
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BudgetDialog dialog = new BudgetDialog();
                dialog.showAddDialog(context,null, mBudget, budget);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBudget.size();
    }

    public class BudgetViewHolder extends RecyclerView.ViewHolder{
        public TextView budgetName, budgetLimit, budgetValue;
        public ProgressBar progressBar;
        public LinearLayout linearLayout;

        public BudgetViewHolder(@NonNull View itemView) {
            super(itemView);

            budgetName = itemView.findViewById(R.id.item_budget_name);
            budgetLimit = itemView.findViewById(R.id.item_budget_total);
            budgetValue = itemView.findViewById(R.id.item_budget_left);
            progressBar = itemView.findViewById(R.id.progressBar4);
            linearLayout = itemView.findViewById(R.id.budget_item_linear);

        }
    }
}
