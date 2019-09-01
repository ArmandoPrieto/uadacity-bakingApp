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
public class StepListActivity extends AppCompatActivity {

    private static final String TAG = StepListActivity.class.toString();
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private List<Recipe> recipeList = new ArrayList<>();

    RecyclerView.Adapter mAdapter;

    public static final String ARG_ITEM_ID = "item_id";
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
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        mRecipeServingsTextView = findViewById(R.id.tv_recipe_servings);

        View stepListRecyclerView = findViewById(R.id.rv_recipe_steps);
        setupStepListRecyclerView((RecyclerView) stepListRecyclerView);

        if (this.getIntent().hasExtra(ARG_ITEM_ID)) {
            int argId = Integer.valueOf(this.getIntent().getStringExtra(ARG_ITEM_ID));
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
            arguments.putInt(StepDetailFragment.ARG_LAYOUT_ID, R.layout.ingedients_detail);
            arguments.putString(ARG_ITEM_ID,this.getIntent().getStringExtra(ARG_ITEM_ID));
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();

        }else{
            //Open in activity


        }

    }

    public static class StepListRecyclerViewAdapter
            extends RecyclerView.Adapter<StepListActivity.StepListRecyclerViewAdapter.ViewHolder> {

        private final StepListActivity mParentView;
        private final List<Step> mStepValues;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };

        StepListRecyclerViewAdapter(StepListActivity parent, List<Step> step) {
            mStepValues = step;
            mParentView = parent;

        }

        @Override
        public StepListActivity.StepListRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_steps_list_content, parent, false);
            return new StepListActivity.StepListRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final StepListActivity.StepListRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mStepShortDescriptionTextView.setText(mStepValues.get(position).getShortDescription());
        //    holder.mStepDescriptionTextView.setText(mStepValues.get(position).getShortDescription());
        //    holder.mStepVideoUrlTextView.setText(mStepValues.get(position).getVideoURL());
        //    holder.mStepThumbnailUrlTextView.setText(mStepValues.get(position).getThumbnailURL());
            //holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mStepValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R2.id.tv_step_short_description) TextView mStepShortDescriptionTextView;
         //   @BindView(R2.id.tv_step_description) TextView mStepDescriptionTextView;
         //   @BindView(R2.id.tv_step_videoUrl) TextView mStepVideoUrlTextView;
         //   @BindView(R2.id.tv_step_thumbnailUrl) TextView mStepThumbnailUrlTextView;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }



}
