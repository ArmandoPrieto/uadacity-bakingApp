package com.udacity.bakingapp.utils.data;

import com.udacity.bakingapp.model.Recipe;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("{endpoint}")
    Call<List<Recipe>> getRecipes(@Path("endpoint") String endpoint);
}
