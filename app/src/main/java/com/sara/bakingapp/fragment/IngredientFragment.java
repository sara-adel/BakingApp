package com.sara.bakingapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sara.bakingapp.R;
import com.sara.bakingapp.model.Ingredient;
import com.sara.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import static com.sara.bakingapp.Constants.SELECTED_RECIPES;

/**
 * Created by sara on 4/8/2018.
 */

public class IngredientFragment extends Fragment {

    ArrayList<Ingredient> ingredients;
    OnIngredientClickListener onIngredientClickListener;

    CardView ingredientCard;
    View rootView;

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);
        initViews();
        return rootView;
    }

    public void initViews(){
        ingredientCard = rootView.findViewById(R.id.ingredient_card);
        ingredientCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onIngredientClickListener.onIngredientClicked(ingredients);
            }
        });
    }

    public interface OnIngredientClickListener {
        void onIngredientClicked(ArrayList<Ingredient> ingredients);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onIngredientClickListener = (OnIngredientClickListener) context;
        } catch (Exception e) {
            Log.e("IngredientFragment", e.getMessage());
        }
    }

}