package com.sara.bakingapp.activity;

import android.content.Intent;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chootdev.recycleclick.RecycleClick;
import com.sara.bakingapp.Constants;
import com.sara.bakingapp.IdlingResource.SimpleIdlingResource;
import com.sara.bakingapp.R;
import com.sara.bakingapp.adapters.RecipeAdapter;
import com.sara.bakingapp.model.Ingredient;
import com.sara.bakingapp.model.Recipe;
import com.sara.bakingapp.model.Step;
import com.sara.bakingapp.service.ApiBuilder;
import com.sara.bakingapp.service.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeActivity extends AppCompatActivity {

    private ImageView back;
    private TextView title;
    private ProgressBar progressBar;
    private RecyclerView mRecipesList;

    ArrayList<Recipe> recipes;
    ArrayList<Step> steps;
    ArrayList<Ingredient> ingredients;
    RecipeAdapter adapter;

    private Parcelable layoutManagerSavedState;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        initViews();

        if (savedInstanceState != null) {
            recipes = savedInstanceState.getParcelableArrayList(Constants.RECIPE);
            layoutManagerSavedState = savedInstanceState.getParcelable(Constants.LIST_STATE);
            initialRecycleData(recipes);
            progressBar.setVisibility(View.GONE);
            Log.e("rotation", "done");
        } else {
            RecipeData();
        }

        getIdlingResource();

    }

    public void initViews() {
        title = findViewById(R.id.main_toolbar_title);
        title.setText(" Baking ");

        back = findViewById(R.id.backPress);
        back.setVisibility(View.INVISIBLE);

        progressBar = findViewById(R.id.main_progressBar);
        mRecipesList = findViewById(R.id.recipes_list);

        recipes = new ArrayList<>();
        steps = new ArrayList<>();
        ingredients = new ArrayList<>();

        if (findViewById(R.id.two_pane) != null) {
            mRecipesList.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        } else {
            mRecipesList.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    public void initialRecycleData(ArrayList recipes) {
        final ArrayList<Recipe> mRecipes = recipes;
        adapter = new RecipeAdapter(mRecipes, this);
        mRecipesList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        onRestoreScrolling();
        RecycleClick.addTo(mRecipesList).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(RecipeActivity.this, RecipeDetailActivity.class);
                intent.putExtra(Constants.RECIPE, mRecipes.get(position).getName());
                intent.putParcelableArrayListExtra(Constants.INGREDIENTS, (ArrayList<? extends Parcelable>) mRecipes.get(position).getIngredients());
                intent.putParcelableArrayListExtra(Constants.STEPS, (ArrayList<? extends Parcelable>) mRecipes.get(position).getSteps());
                startActivity(intent);
            }
        });
    }

    public void RecipeData() {
        ApiInterface apiInterface = ApiBuilder.Retrieve().create(ApiInterface.class);
        final Call<ArrayList<Recipe>> recipe = apiInterface.getRecipe();

        recipe.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                Integer status = response.code();
                Log.v("status code: ", status.toString());

                recipes = response.body();
                progressBar.setVisibility(View.GONE);
                initialRecycleData(recipes);
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.e("http fail: ", t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Constants.RECIPE, recipes);
        outState.putParcelable(Constants.LIST_STATE, mRecipesList.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.getParcelableArrayList(Constants.RECIPE);
        savedInstanceState.getParcelable(Constants.LIST_STATE);
    }

    private void onRestoreScrolling() {
        if (layoutManagerSavedState != null) {
            mRecipesList.getLayoutManager().onRestoreInstanceState(layoutManagerSavedState);
        }
    }

}