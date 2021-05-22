package com.malisius.monefy.records;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.malisius.monefy.expense.Expense;
import com.malisius.monefy.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.ExpenseViewHolder> {
    private ArrayList<Expense> mExpense = new ArrayList<Expense>();
    private Context context;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yy");

    public ExpenseListAdapter(ArrayList<Expense> expense){
        mExpense = expense;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View incomeView = inflater.inflate(R.layout.record_item_layout, parent, false);

        // Return a new holder instance
        ExpenseViewHolder viewHolder = new ExpenseViewHolder(incomeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        // Get the data model based on position
        Expense expense = mExpense.get(position);

        // Set item views based on your views and data model
        TextView recordName = holder.recordName;
        recordName.setText(expense.getName());

        TextView recordValue = holder.recordValue;
        recordValue.setText(expense.getValue());

        TextView recordDate = holder.recordDate;
        String date = sdf.format(expense.getDate());
        recordDate.setText(date);


    }

    @Override
    public int getItemCount() {
        return mExpense.size();
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder{
        public TextView recordName, recordValue, recordDate;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);

            recordName = itemView.findViewById(R.id.tv_recordName);
            recordValue = itemView.findViewById(R.id.record_value);
            recordDate = itemView.findViewById(R.id.record_date);

        }
    }
}
