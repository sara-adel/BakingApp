package com.sara.bakingapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sara.bakingapp.Constants;
import com.sara.bakingapp.R;
import com.sara.bakingapp.fragment.IngredientDetailsFragment;
import com.sara.bakingapp.fragment.IngredientFragment;
import com.sara.bakingapp.fragment.StepDetailsFragment;
import com.sara.bakingapp.fragment.StepsFragment;
import com.sara.bakingapp.model.Ingredient;
import com.sara.bakingapp.model.Step;
import com.sara.bakingapp.widget.UpdateRecipeService;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity
        implements IngredientFragment.OnIngredientClickListener, StepsFragment.OnStepClickListener {

    ArrayList<Ingredient> ingredients;
    ArrayList<Step> steps;
    IngredientFragment ingredientFragment;
    StepsFragment stepsFragment;
    IngredientDetailsFragment ingredientDetailsFragment;
    StepDetailsFragment stepDetailsFragment;
    FragmentManager manager;
    public static String titleActionBar;
    public static String INGREDIENTS;
    boolean twoPaneMode;
    SharedPreferences preferences;
    String recipeName;

    ImageView back;
    TextView title, addWidget;
    FrameLayout recipeIngredientsFrame, recipeStepsFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        initViews();

        if (savedInstanceState != null) {
            steps = savedInstanceState.getParcelableArrayList(Constants.STEPS);
            ingredients = savedInstanceState.getParcelableArrayList(Constants.INGREDIENTS);
            ingredientFragment.setIngredients(ingredients);
            stepsFragment.setSteps(steps);
            titleActionBar = savedInstanceState.getString(Constants.RECIPE);
        }
    }

    public void initViews() {
        title = findViewById(R.id.main_toolbar_title);
        recipeIngredientsFrame = findViewById(R.id.recipe_ingredient_container);
        recipeStepsFrame = findViewById(R.id.recipe_steps_container);
        back = findViewById(R.id.backPress);
        addWidget = findViewById(R.id.addWidget);
        addWidget.setVisibility(View.VISIBLE);

        preferences = getSharedPreferences(Constants.INGREDIENTS, MODE_PRIVATE);
        fragments();
        Intent intent = this.getIntent();

        if (intent != null) {
            steps = intent.getParcelableArrayListExtra(Constants.STEPS);
            ingredients = intent.getParcelableArrayListExtra(Constants.INGREDIENTS);
            ingredientFragment.setIngredients(ingredients);
            stepsFragment.setSteps(steps);
            titleActionBar = intent.getStringExtra(Constants.RECIPE);
        }

        title.setText(titleActionBar);

        if (findViewById(R.id.details_container) != null) {
            twoPaneMode = true;
        }
        if (ingredients != null || ingredients.size() > 0) {
            Log.e("sizeOfIngrredients", ingredients.size() + "");
            setIngredients(ingredients);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        addWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ingredients != null){
                    if (UpdateRecipeService.startActionChangeIngredientList(RecipeDetailActivity.this)){
                        Toast.makeText(RecipeDetailActivity.this, "" + titleActionBar + "'s Recipe Added", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }

    private void fragments() {
        ingredientFragment = new IngredientFragment();
        stepsFragment = new StepsFragment();

        ingredientDetailsFragment = new IngredientDetailsFragment();
        stepDetailsFragment = new StepDetailsFragment();

        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.recipe_ingredient_container, ingredientFragment, "ingredientFragment")
                .add(R.id.recipe_steps_container, stepsFragment, "stepsFragment")
                .commit();
    }

    @Override
    public void onIngredientClicked(ArrayList<Ingredient> ingredients) {
        if (twoPaneMode) {
            ingredientDetailsFragment.setIngredients(ingredients);
            manager.beginTransaction().replace(R.id.details_container, ingredientDetailsFragment)
                    .commit();
        } else {
            Intent intent = new Intent(RecipeDetailActivity.this, IngredientDetailsActivity.class);
            intent.putParcelableArrayListExtra(Constants.INGREDIENTS, ingredients);
            startActivity(intent);
        }
    }

    @Override
    public void onStepClicked(String description, String video, String thumbnail) {
        if (stepDetailsFragment != null) {
            if (twoPaneMode) {
                stepDetailsFragment.setVideo(video);
                stepDetailsFragment.setDescription(description);
                stepDetailsFragment.setThumbnail(thumbnail);
                manager.beginTransaction().replace(R.id.details_container, stepDetailsFragment, "StepDetailsFragment")
                        .detach(stepDetailsFragment)
                        .attach(stepDetailsFragment)
                        .commit();
            } else {
                Intent intent = new Intent(RecipeDetailActivity.this, StepDetailsActivity.class);
                intent.putExtra(Constants.VIDEO, video);
                intent.putExtra(Constants.DESCRIPTION, description);
                intent.putExtra(Constants.THUMBNAIL, thumbnail);
                startActivity(intent);
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Constants.STEPS, steps);
        outState.putParcelableArrayList(Constants.INGREDIENTS, ingredients);
        outState.putString(Constants.RECIPE, titleActionBar);
//        outState.putParcelable(Constants.DETAIL_STATE, steps.);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.getParcelableArrayList(Constants.STEPS);
        savedInstanceState.getParcelableArrayList(Constants.INGREDIENTS);
        savedInstanceState.getString(Constants.RECIPE);
    }

    public void setIngredients(List<Ingredient> ingredients) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < ingredients.size(); i++) {
            stringBuilder.append("" + (i + 1) + ". " + ingredients.get(i).getIngredient() + " - " + ingredients.get(i).getQuantity()
                    + " " + ingredients.get(i).getMeasure() + "\n");
        }
        INGREDIENTS = stringBuilder.toString();
    }
}