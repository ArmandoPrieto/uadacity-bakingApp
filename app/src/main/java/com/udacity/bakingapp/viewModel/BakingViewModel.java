package com.udacity.bakingapp.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.utils.data.ApiClient;
import com.udacity.bakingapp.utils.data.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class BakingViewModel extends ViewModel {

    private static final String TAG = BakingViewModel.class.toString();

    private MutableLiveData<List<Recipe>> recipes;
    private ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

    public LiveData<List<Recipe>> getRecipes() {
        if (recipes == null) {
            recipes = new MutableLiveData<>();
            loadRecipes(ApiClient.ENDPOINT);
        }
        return recipes;
    }


    private void loadRecipes(String endpoint){
        Call<List<Recipe>> call = apiInterface.getRecipes(endpoint);
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                Log.d(TAG, "onResponse: "+ response.body());
                recipes.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+ t.getLocalizedMessage());
            }
        });

    }

}

