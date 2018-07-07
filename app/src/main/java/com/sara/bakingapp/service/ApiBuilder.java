package com.sara.bakingapp.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.sara.bakingapp.Constants.BASE_URL;

/**
 * Created by sara on 3/26/2018.
 */

public class ApiBuilder {

    private static Retrofit retrofit = null;

    public static Retrofit Retrieve() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient.Builder client = new OkHttpClient.Builder();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .callFactory(client.build())
                    .build();
        }
        return retrofit;
    }
}