package com.sara.bakingapp.activity;

import android.content.Intent;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sara.bakingapp.Constants;
import com.sara.bakingapp.R;
import com.sara.bakingapp.adapters.IngredientsDetailsAdapter;
import com.sara.bakingapp.model.Ingredient;

import java.util.ArrayList;

public class IngredientDetailsActivity extends AppCompatActivity {

    ImageView back;
    TextView title;
    RecyclerView ingredientList;

    IngredientsDetailsAdapter adapter;
    ArrayList<Ingredient> ingredients;
    private Parcelable layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_details);

        initViews();
        if (savedInstanceState != null) {
            layoutManager = savedInstanceState.getParcelable(Constants.LIST_STATE);
        }
    }

    public void initViews(){
        title = findViewById(R.id.main_toolbar_title);
        title.setText("Ingredients");

        back = findViewById(R.id.backPress);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        if (intent != null){
            ingredients = intent.getParcelableArrayListExtra(Constants.INGREDIENTS);
        }

        ingredientList = findViewById(R.id.ingredient_details);
        ingredientList.setLayoutManager(new LinearLayoutManager(this));
        if (ingredients != null){
            adapter = new IngredientsDetailsAdapter(ingredients, this);
        }
        ingredientList.setAdapter(adapter);
        if (layoutManager != null){
            ingredientList.getLayoutManager().onRestoreInstanceState(layoutManager);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable(Constants.LIST_STATE, ingredientList.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.getParcelable(Constants.LIST_STATE);
    }
}