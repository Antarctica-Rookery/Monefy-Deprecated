package com.malisius.monefy.category;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.malisius.monefy.R;

import java.util.ArrayList;
import java.util.Collections;

public class CategoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageView back;
    private CardView cardView;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    private ArrayList<Category> mCategoriesList = new ArrayList<Category>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        //add new category
        cardView = findViewById(R.id.cardView_newCategory);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //open dialog
                createDialog();
                mCategoriesList.clear();
//                int total_category = categories.size();
//                int color= ((int)(Math.random()*16777215)) | (0xFF << 24);
//                categories.add(new Category("Category " + (total_category + 1), color ));
//                Objects.requireNonNull(recyclerView.getAdapter()).notifyItemInserted(total_category);
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

        //fetch categories from database
        initCategory();

        //init recyclerview
        recyclerView = findViewById(R.id.recyclercategories);
        CategoryListAdapter adapter = new CategoryListAdapter(mCategoriesList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initCategory(){
        DatabaseReference userDataRef = mDatabase.getReference().child("Data").child(mAuth.getCurrentUser().getUid()).child("Categories");

        ValueEventListener userDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Log.w("CategoryActivity", "No Children");
                } else {
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        Log.i("CategoryActivity", dataSnapshot.getValue().toString());

                        mCategoriesList.add(new Category(dataSnapshot.child("name").getValue().toString(),dataSnapshot.child("color").getValue().toString()));
                        Log.i("CategoryActivity", "hello");
                    }
                    Collections.sort(mCategoriesList, Category.categoryNameComparator);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("CategoryActivity", "loadPost:onCancelled", error.toException());
            }
        };
        userDataRef.addValueEventListener(userDataListener);
    }

    private void createDialog() {
        CategoryDialog dialog = new CategoryDialog();
        dialog.showAddDialog(this,null, mCategoriesList, 0);
    }
}