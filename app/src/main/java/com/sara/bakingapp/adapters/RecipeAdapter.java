package com.sara.bakingapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sara.bakingapp.R;
import com.sara.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sara on 3/26/2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    ArrayList<Recipe> recipeList;
    Context mContext;


    public RecipeAdapter(ArrayList<Recipe> recipeList, Context mContext) {
        this.recipeList = recipeList;
        this.mContext = mContext;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.recipeTitle.setText(recipeList.get(position).getName());

        String imageUrl = recipeList.get(position).getImage();
        if (imageUrl != null) {
            Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
            Picasso.with(mContext)
                    .load(builtUri)
                    .placeholder(R.drawable.logo)
                    .into(holder.recipeImage);
        }

    }

    @Override
    public int getItemCount() {
        return (recipeList != null) ? recipeList.size(): 0;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        private TextView recipeTitle;
        private ImageView recipeImage;

        public RecipeViewHolder(View itemView) {
            super(itemView);

            recipeTitle = itemView.findViewById(R.id.recipe_title);
            recipeImage = itemView.findViewById(R.id.recipe_image);

        }

    }
}
