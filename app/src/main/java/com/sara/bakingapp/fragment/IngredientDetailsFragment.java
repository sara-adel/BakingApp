package com.sara.bakingapp.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sara.bakingapp.Constants;
import com.sara.bakingapp.R;
import com.sara.bakingapp.adapters.IngredientsDetailsAdapter;
import com.sara.bakingapp.model.Ingredient;

import java.util.ArrayList;

/**
 * Created by sara on 4/14/2018.
 */

public class IngredientDetailsFragment extends Fragment {

    View view;
    RecyclerView mIngredientDetailsList;

    IngredientsDetailsAdapter adapter;
    ArrayList<Ingredient> ingredients;
    private Parcelable layoutManager;

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_ingredient_detail, container, false);
        initViews();
        if (savedInstanceState != null) {
            layoutManager = savedInstanceState.getParcelable(Constants.LIST_STATE);
            ingredients = savedInstanceState.getParcelableArrayList(Constants.INGREDIENT);
        }

        return view;
    }

    public void initViews() {
        mIngredientDetailsList = view.findViewById(R.id.ingredient_detail_list);
        mIngredientDetailsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (ingredients != null) {
            adapter = new IngredientsDetailsAdapter(ingredients, getActivity());
        }
        mIngredientDetailsList.setAdapter(adapter);
        if (layoutManager != null) {
            mIngredientDetailsList.getLayoutManager().onRestoreInstanceState(layoutManager);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.LIST_STATE, mIngredientDetailsList.getLayoutManager().onSaveInstanceState());
    }
}