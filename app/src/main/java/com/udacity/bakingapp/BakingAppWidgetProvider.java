package com.udacity.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.bakingapp.model.Ingredient;
import com.udacity.bakingapp.model.Recipe;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, List<Recipe> recipeList,
                                int[] appWidgetIds) {
         // Construct the RemoteViews object
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget_provider);
            views.setOnClickPendingIntent(R.id.bt_show_next_recipe,
                    getPendingSelfIntent(context, AppWidgetManager.ACTION_APPWIDGET_UPDATE, appWidgetId));
            for(Recipe recipe : recipeList){
                RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget_recipe);
                view.setTextViewText(R.id.tv_recipe_name, recipe.getName());
                view.setTextViewText(R.id.tv_recipe_ingredients_list,
                        recipe.getIngredients().stream()
                                    .map(Ingredient::getIngredient)
                                    .collect(Collectors.joining(", ")));
                views.addView(R.id.view_flipper,view);
            }
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        BakingAppWidgetIntentService.startActionLoadRecipe(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction())){
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget_provider);
            views.showNext(R.id.view_flipper);
            Toast.makeText(context, "Button1", Toast.LENGTH_SHORT).show();
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,0);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }


    }

    protected static PendingIntent getPendingSelfIntent(Context context, String action, int appWidgetId) {
        Intent intent = new Intent(context, BakingAppWidgetProvider.class);
        intent.setAction(action);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}

