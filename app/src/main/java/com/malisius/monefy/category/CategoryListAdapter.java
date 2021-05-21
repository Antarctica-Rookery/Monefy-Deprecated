package com.malisius.monefy.category;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.malisius.monefy.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {
    private ArrayList<Category> mCategory = new ArrayList<Category>();
    private Context context;

    public CategoryListAdapter(ArrayList<Category> category){
        mCategory = category;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View categoryView = inflater.inflate(R.layout.category_name_item_layout, parent, false);

        // Return a new holder instance
        CategoryViewHolder viewHolder = new CategoryViewHolder(categoryView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        // Get the data model based on position
        Category category = mCategory.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.categoryName;
        textView.setText(category.getName());

        View view_color = holder.categoryColor;
        view_color.setBackgroundColor(Color.parseColor(category.getColor()));

        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryDialog dialog = new CategoryDialog();

                dialog.showAddDialog(context, category.getName(), mCategory, position);
                mCategory.clear();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategory.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        public TextView categoryName;
        public View categoryColor;
        public LinearLayout ll;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            ll = itemView.findViewById(R.id.ll);
            categoryName = itemView.findViewById(R.id.tv_categoryName);
            categoryColor = itemView.findViewById(R.id.view_categoryColor);
        }
    }
}
