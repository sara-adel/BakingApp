package com.sara.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sara.bakingapp.R;
import com.sara.bakingapp.model.Ingredient;

import java.util.List;

/**
 * Created by sara on 4/14/2018.
 */

public class IngredientsDetailsAdapter extends RecyclerView.Adapter<IngredientsDetailsAdapter.IngredientsViewHolder> {

    List<Ingredient> ingredients;
    Context context;

    public IngredientsDetailsAdapter(List<Ingredient> ingredients, Context context) {
        this.ingredients = ingredients;
        this.context = context;
    }

    @Override
    public IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ingredient, parent, false);
        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsViewHolder holder, int position) {
        holder.mIngredientTitle.setText(ingredients.get(position).getIngredient());
        holder.mIngredientQuantity.append(" : " + String.valueOf(ingredients.get(position).getQuantity()) + " " + ingredients.get(position).getMeasure());

    }

    @Override
    public int getItemCount() {
        return (ingredients != null) ? ingredients.size(): 0;
    }

    class IngredientsViewHolder extends RecyclerView.ViewHolder {

        TextView mIngredientTitle, mIngredientQuantity;

        public IngredientsViewHolder(View itemView) {
            super(itemView);

            mIngredientTitle = itemView.findViewById(R.id.item_ingredient_title);
            mIngredientQuantity = itemView.findViewById(R.id.item_ingredient_quantity);
        }
    }
}
