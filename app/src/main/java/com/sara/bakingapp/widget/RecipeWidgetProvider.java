package com.sara.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.sara.bakingapp.R;
import com.sara.bakingapp.activity.RecipeDetailActivity;

import java.util.ArrayList;

import static com.sara.bakingapp.Constants.FROM_ACTIVITY_INGREDIENTS_LIST;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String recipeName,String ingredients) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        views.setTextViewText(R.id.recipe_widget_title, recipeName);
        views.setTextViewText(R.id.recipe_widget_ingredients, ingredients);
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        UpdateRecipeService.startActionChangeIngredientList(context);
    }

    public static void updateIngredientWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, String recipeName , String ingredients) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipeName ,ingredients);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}