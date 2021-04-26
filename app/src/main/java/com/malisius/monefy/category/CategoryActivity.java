package com.malisius.monefy.category;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.malisius.monefy.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

public class CategoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageView back;
    private CardView cardView;

    ArrayList<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        //add new category
        cardView = findViewById(R.id.cardView_newCategory);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total_category = categories.size();
                int color= ((int)(Math.random()*16777215)) | (0xFF << 24);
                categories.add(new Category("Category " + (total_category + 1), color ));
                Objects.requireNonNull(recyclerView.getAdapter()).notifyItemInserted(total_category);
            }
        });

        //go to previous activity
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclercategories);
        categories = Category.createCategoryList(20);
        CategoryListAdapter adapter = new CategoryListAdapter(categories);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}