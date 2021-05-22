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

import com.malisius.monefy.income.Income;
import com.malisius.monefy.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class IncomeListAdapter extends RecyclerView.Adapter<IncomeListAdapter.IncomeViewHolder> {
    private ArrayList<Income> mIncome = new ArrayList<Income>();
    private Context context;
    private TextView viewTitle;
    private String categoryName;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");

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

        ConstraintLayout constraintLayout = (ConstraintLayout) parent.getParent();
        viewTitle = constraintLayout.findViewById(R.id.viewTitle);

        categoryName = viewTitle.getText().toString();

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
        recordValue.setText(String.valueOf(income.getValue()));

        TextView recordDate = holder.recordDate;
        Date incomeDate = new Date(income.getDate());
        String date = sdf.format(incomeDate);
        recordDate.setText(date);

        LinearLayout ll = holder.ll;
        ll.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                RecordDialog dialog = new RecordDialog();
                dialog.showDialog(context,true, true, categoryName, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mIncome.size();
    }

    public class IncomeViewHolder extends RecyclerView.ViewHolder{
        public TextView recordName, recordValue, recordDate;
        public LinearLayout ll;

        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);

            recordName = itemView.findViewById(R.id.tv_recordName);
            recordValue = itemView.findViewById(R.id.record_value);
            recordDate = itemView.findViewById(R.id.record_date);
            ll = itemView.findViewById(R.id.ll);
        }
    }
}
