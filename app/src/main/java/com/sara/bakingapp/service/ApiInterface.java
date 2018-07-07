package com.sara.bakingapp.service;

import com.sara.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by sara on 3/26/2018.
 */

public interface ApiInterface {
    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipe();
}