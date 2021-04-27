package com.malisius.monefy.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.malisius.monefy.R;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {
    private List<Category> mCategory;

    public CategoryListAdapter(List<Category> category){
        mCategory = category;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
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
        textView.setText(category.getCategoryName());

        View view_color = holder.categoryColor;
        view_color.setBackgroundColor(category.getCategoryColor());
    }

    @Override
    public int getItemCount() {
        return mCategory.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        public TextView categoryName;
        public View categoryColor;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.tv_categoryName);
            categoryColor = itemView.findViewById(R.id.view_categoryColor);
        }
    }
}
