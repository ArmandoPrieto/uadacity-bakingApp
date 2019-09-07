package com.udacity.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.utils.data.ApiClient;
import com.udacity.bakingapp.utils.data.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class BakingAppWidgetIntentService extends IntentService {

    private static final String TAG =BakingAppWidgetIntentService.class.toString();
    private static final String ACTION_LOAD_RECIPES = "com.udacity.bakingapp.action.LOAD_RECIPES";
    List<Recipe> recipeList;
    private ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

    public BakingAppWidgetIntentService() {
        super("BakingAppWidgetIntentService");
    }


    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionLoadRecipe(Context context) {
        Intent intent = new Intent(context, BakingAppWidgetIntentService.class);
        intent.setAction(ACTION_LOAD_RECIPES);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_LOAD_RECIPES.equals(action)) {
                handleActionLoadRecipes();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionLoadRecipes() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidgetProvider.class));
        loadRecipesOnWidgets(appWidgetManager,appWidgetIds);
    }

    private void loadRecipesOnWidgets(AppWidgetManager appWidgetManager, int[] appWidgetIds){
        Call<List<Recipe>> call = apiInterface.getRecipes(ApiClient.ENDPOINT);
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                Log.d(TAG, "onResponse: "+ response.body());
                recipeList = response.body();
                BakingAppWidgetProvider.updateAppWidget(BakingAppWidgetIntentService.this, appWidgetManager, recipeList, appWidgetIds);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+ t.getLocalizedMessage());
            }
        });

    }

}
