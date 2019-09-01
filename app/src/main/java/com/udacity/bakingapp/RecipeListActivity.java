package com.udacity.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.viewModel.BakingViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListActivity extends AppCompatActivity {

    private List<Recipe> recipeList = new ArrayList<>();

    RecyclerView.Adapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        View recyclerView = findViewById(R.id.item_list);
        setupRecyclerView((RecyclerView) recyclerView);

        BakingViewModel model = ViewModelProviders.of(this).get(BakingViewModel.class);
        model.getRecipes().observe(this, recipes -> {
            recipeList.clear();
            recipes.forEach(recipe -> recipeList.add(recipe));
            mAdapter.notifyDataSetChanged();
        });
    }



    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mAdapter = new RecipeListActivity.RecipeListRecyclerViewAdapter(this, recipeList);
        recyclerView.setAdapter(mAdapter);
    }

    public static class RecipeListRecyclerViewAdapter
            extends RecyclerView.Adapter<RecipeListActivity.RecipeListRecyclerViewAdapter.ViewHolder> {

        private final RecipeListActivity mParentActivity;
        private final List<Recipe> mValues;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recipe item = (Recipe) view.getTag();
                    Context context = view.getContext();
                    Intent intent = new Intent(context, StepListActivity.class);
                    intent.putExtra(StepDetailFragment.ARG_ITEM_ID, String.valueOf(item.getId()));
                    context.startActivity(intent);

            }
        };

        RecipeListRecyclerViewAdapter(RecipeListActivity parent,
                                      List<Recipe> recipes) {
            mValues = recipes;
            mParentActivity = parent;

        }

        @Override
        public RecipeListActivity.RecipeListRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new RecipeListActivity.RecipeListRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RecipeListActivity.RecipeListRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mIdView.setText(String.valueOf(mValues.get(position).getId()));
            holder.mContentView.setText(mValues.get(position).getName());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R2.id.id_text)
            TextView mIdView;
            @BindView(R2.id.content) TextView mContentView;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}