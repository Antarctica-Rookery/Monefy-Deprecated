package com.malisius.monefy.records;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.malisius.monefy.expense.Expense;
import com.malisius.monefy.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.ExpenseViewHolder> {
    private ArrayList<Expense> mExpense = new ArrayList<Expense>();
    private Context context;
    private TextView viewTitle;
    private String categoryName;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");

    public ExpenseListAdapter(ArrayList<Expense> expense){
        mExpense = expense;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        ConstraintLayout constraintLayout = (ConstraintLayout) parent.getParent();
        viewTitle = constraintLayout.findViewById(R.id.viewTitle);

        categoryName = viewTitle.getText().toString();
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
        recordValue.setText(String.valueOf(expense.getValue()));

        TextView recordDate = holder.recordDate;
        Date expenseDate =  new Date(expense.getDate());
        String date = sdf.format(expenseDate);
        recordDate.setText(date);

        LinearLayout ll = holder.ll;
        ll.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                RecordDialog dialog = new RecordDialog();
                dialog.showDialog(context,true, false, categoryName, position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mExpense.size();
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder{
        public TextView recordName, recordValue, recordDate;
        public LinearLayout ll;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);

            recordName = itemView.findViewById(R.id.tv_recordName);
            recordValue = itemView.findViewById(R.id.record_value);
            recordDate = itemView.findViewById(R.id.record_date);
            ll = itemView.findViewById(R.id.ll);
        }
    }
}
