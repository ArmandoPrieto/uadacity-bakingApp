package com.udacity.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.bakingapp.model.Ingredient;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.model.Step;
import com.udacity.bakingapp.viewModel.BakingViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StepListActivity extends AppCompatActivity implements StepListRecyclerViewAdapter.ViewHolder.OnRecipeStepListener{

    private static final String TAG = StepListActivity.class.toString();
    private boolean mTwoPane;
    private List<Recipe> recipeList = new ArrayList<>();
    RecyclerView.Adapter mAdapter;
    public static final String ARG_RECIPE_ID = "recipe_id";
    public static final String ARG_STEP_ID = "step_id";
    public static final String ARG_RECIPE_TITLE = "recipe_title";
    private Recipe mItem;
    private TextView mRecipeNameTextView;
    private TextView mRecipeServingsTextView;
    private CollapsingToolbarLayout appBarLayout;
    private List<Step> mStepList = new ArrayList<>();
    private RecyclerView.Adapter mStepListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(this.getIntent().hasExtra(ARG_RECIPE_TITLE)) {
            toolbar.setTitle(this.getIntent().getStringExtra(ARG_RECIPE_TITLE));
        }else{
            toolbar.setTitle(this.getTitle());
        }
        setSupportActionBar(toolbar);

        if(findViewById(R.id.item_detail_container) != null) {
              mTwoPane = true;
        }
        mRecipeServingsTextView = findViewById(R.id.tv_recipe_servings);
        View stepListRecyclerView = findViewById(R.id.rv_recipe_steps);
        setupStepListRecyclerView((RecyclerView) stepListRecyclerView);
        if (this.getIntent().hasExtra(ARG_RECIPE_ID)) {
            int argId = Integer.valueOf(this.getIntent().getStringExtra(ARG_RECIPE_ID));
            BakingViewModel model = ViewModelProviders.of(this).get(BakingViewModel.class);
            model.getRecipes().observe(this, recipes -> {
                recipes.stream().filter(recipe -> recipe.getId() == argId)
                        .findFirst()
                        .ifPresent(recipe -> mItem = recipe);
                mStepList.clear();
                mItem.getSteps().forEach(step -> mStepList.add(step));
                mStepListAdapter.notifyDataSetChanged();
                if (appBarLayout != null) {
                    appBarLayout.setTitle(mItem.getName());
                }
                mRecipeServingsTextView.setText(String.valueOf(mItem.getServings()));
            });
        }
        if (findViewById(R.id.item_detail_container) != null) {
            Bundle arguments = new Bundle();
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
            mTwoPane = true;
        }
    }

    private void setupStepListRecyclerView(@NonNull RecyclerView recyclerView) {
        mStepListAdapter = new StepListRecyclerViewAdapter(this, mStepList);
        recyclerView.setAdapter(mStepListAdapter);
    }

    public void showIngredients(View view) {

        if(mTwoPane){
            Bundle arguments = new Bundle();
            arguments.putString(ARG_RECIPE_ID,this.getIntent().getStringExtra(ARG_RECIPE_ID));
            IngredientDetailFragment fragment = new IngredientDetailFragment();
            fragment.setArguments(arguments);
            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        }else{
            //Open in activity
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra(ARG_RECIPE_ID,this.getIntent().getStringExtra(ARG_RECIPE_ID));
            intent.putExtra(ARG_RECIPE_TITLE,this.getIntent().getStringExtra(ARG_RECIPE_TITLE));
            startActivity(intent);
        }
    }

    @Override
    public void onRecipeStepClick(int stepId) {
        if(mTwoPane){
            Bundle arguments = new Bundle();
            arguments.putString(ARG_RECIPE_ID,this.getIntent().getStringExtra(ARG_RECIPE_ID));
            arguments.putInt(ARG_STEP_ID, stepId);
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        }else{
            //Open in activity
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra(ARG_RECIPE_ID,this.getIntent().getStringExtra(ARG_RECIPE_ID));
            intent.putExtra(ARG_STEP_ID, stepId);
            intent.putExtra(ARG_RECIPE_TITLE,this.getIntent().getStringExtra(ARG_RECIPE_TITLE));
            startActivity(intent);
        }
    }
}
