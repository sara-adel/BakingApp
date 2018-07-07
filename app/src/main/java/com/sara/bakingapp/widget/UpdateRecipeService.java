package com.sara.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.sara.bakingapp.Constants;
import com.sara.bakingapp.activity.RecipeDetailActivity;

import java.util.ArrayList;

import static com.sara.bakingapp.Constants.FROM_ACTIVITY_INGREDIENTS_LIST;

/**
 * Created by sara on 4/18/2018.
 */

public class UpdateRecipeService extends IntentService {

    public UpdateRecipeService() {
        super("UpdateRecipeService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (Constants.INGREDIENT_SERVICE_ACTION.equals(action)) {
                handleActionChangeIngredientList();
            }
        }
    }

    public static boolean startActionChangeIngredientList(Context context) {
        Intent intent = new Intent(context, UpdateRecipeService.class);
        intent.setAction(Constants.INGREDIENT_SERVICE_ACTION);

        // a temporary solution for Android 8.0
        try {
            context.startService(intent);
            return true;
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private void handleActionChangeIngredientList() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        RecipeWidgetProvider.updateIngredientWidgets(this, appWidgetManager, appWidgetIds, RecipeDetailActivity.titleActionBar, RecipeDetailActivity.INGREDIENTS);
    }
}
