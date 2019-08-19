package com.udacity.bakingapp.utils;

import com.udacity.bakingapp.model.Recipe;
import com.udacity.popularMovies.model.Movie;
import com.udacity.popularMovies.model.Review;
import com.udacity.popularMovies.model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class JsonUtils {

    private static final String RESULTS = "recipes";

    public static void parseRecipeListJson(String json, List<Recipe> movieList) {
         try {
            JSONObject jo = new JSONObject(json);
            JSONArray results = jo.getJSONArray(RESULTS);
            for(int i = 0; i< results.length(); i++){
                JSONObject recipeJsonObject = results.getJSONObject(i);
                Recipe recipe = new Recipe();
                recipe.setId(recipeJsonObject.getInt(Movie.ID));
                recipe.setTitle(recipeJsonObject.getString(Movie.TITLE));
                recipe.setOverview(recipeJsonObject.getString(Movie.OVERVIEW));
                recipe.setPosterPath(recipeJsonObject.getString(Movie.POSTER_PATH));
                recipe.setReleaseDate(recipeJsonObject.getString(Movie.RELEASE_DATE));
                recipe.setVoteAverage((float) recipeJsonObject.getDouble(Movie.VOTE_AVERAGE));
                movieList.add(recipe);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            movieList = null;
        }
    }




}
