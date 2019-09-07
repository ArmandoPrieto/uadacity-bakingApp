package com.udacity.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import static com.udacity.bakingapp.StepListActivity.ARG_RECIPE_TITLE;
import static com.udacity.bakingapp.StepListActivity.ARG_STEP_ID;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link StepListActivity}.
 */
public class StepDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_step_detail);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            Toolbar toolbar = findViewById(R.id.toolbar);
            if(this.getIntent().hasExtra(ARG_RECIPE_TITLE)) {
                toolbar.setTitle(this.getIntent().getStringExtra(ARG_RECIPE_TITLE));
            }else{
                toolbar.setTitle(this.getTitle());
            }
            setSupportActionBar(toolbar);
        }


        if (savedInstanceState == null) {
            if(this.getIntent().hasExtra(ARG_STEP_ID)) {
                Bundle arguments = new Bundle();
                arguments.putString(StepListActivity.ARG_RECIPE_ID,
                        getIntent().getStringExtra(StepListActivity.ARG_RECIPE_ID));
                arguments.putInt(ARG_STEP_ID,
                        getIntent().getIntExtra(ARG_STEP_ID,0));
                StepDetailFragment fragment = new StepDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.item_detail_container, fragment)
                        .commit();
            }else{
                Bundle arguments = new Bundle();
                arguments.putString(StepListActivity.ARG_RECIPE_ID,
                        getIntent().getStringExtra(StepListActivity.ARG_RECIPE_ID));
                IngredientDetailFragment fragment = new IngredientDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.item_detail_container, fragment)
                        .commit();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, StepListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
