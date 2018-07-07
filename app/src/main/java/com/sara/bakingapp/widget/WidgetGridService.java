package com.sara.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sara.bakingapp.Constants;
import com.sara.bakingapp.R;
import com.sara.bakingapp.model.Ingredient;

import java.util.ArrayList;


/**
 * Created by sara on 4/18/2018.
 */

public class WidgetGridService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetListViewFactory(this.getApplicationContext());
    }

    class WidgetListViewFactory implements RemoteViewsService.RemoteViewsFactory {

        SharedPreferences preferences;
        Context context;
        ArrayList<Ingredient> ingredients;

        public WidgetListViewFactory(Context context) {
            this.context = context;
            preferences = context.getSharedPreferences(Constants.INGREDIENTS, Context.MODE_PRIVATE);
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            ingredients = prefReader();
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return (ingredients != null) ? ingredients.size(): 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_item);
            remoteViews.setTextViewText(R.id.widget_item_ingredient, ingredients.get(position).getIngredient());
            remoteViews.setTextViewText(R.id.widget_item_quantity, ingredients.get(position).getQuantity() + " " + ingredients.get(position).getMeasure());
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        private ArrayList<Ingredient> prefReader() {

            int ingredient_size = preferences.getInt(Constants.INGREDIENTS_SIZE, 0);
            ArrayList<Ingredient> ingredients = new ArrayList<>();

            for (int i = 0; i < ingredient_size; i++) {

                String ingredient = preferences.getString(Constants.INGREDIENT + " " + i, "error");
                float quantity = preferences.getFloat(Constants.QUANTITY + " " + i, 1);
                String measure = preferences.getString(Constants.MEASURE + " " + i, "error");
                Log.e("size", ingredient + " **-** " + quantity+ " **-** " + measure);

                Ingredient ingredientObject = new Ingredient();
                ingredientObject.setIngredient(ingredient);
                ingredientObject.setQuantity(quantity);
                ingredientObject.setMeasure(measure);

                ingredients.add(ingredientObject);
            }

            Log.e("size", ingredients.size() + " **-** " + ingredient_size);
            return ingredients;
        }
    }
}