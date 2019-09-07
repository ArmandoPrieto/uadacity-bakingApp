package com.udacity.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.squareup.picasso.Picasso;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.viewModel.BakingViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListActivity extends AppCompatActivity {

    private static final String TAG = RecipeListActivity.class.toString();
    private List<Recipe> recipeList = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View recyclerView = findViewById(R.id.item_list);
        setupRecyclerView((RecyclerView) recyclerView);
        BakingViewModel model = ViewModelProviders.of(this).get(BakingViewModel.class);
        model.getRecipes().observe(this, recipes -> {
            recipeList.clear();
            recipeList.addAll(recipes);
            mAdapter.notifyDataSetChanged();
        });
    }



    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            int numberOfColumns = calculateNoOfColumns(320);
            mLayoutManager = new GridLayoutManager(this, numberOfColumns);
            recyclerView.setLayoutManager(mLayoutManager);
        }

        mAdapter = new RecipeListActivity.RecipeListRecyclerViewAdapter(this, recipeList);
        recyclerView.setAdapter(mAdapter);
    }

    private int calculateNoOfColumns(float columnWidthDp) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
    }

    public static class RecipeListRecyclerViewAdapter
            extends RecyclerView.Adapter<RecipeListActivity.RecipeListRecyclerViewAdapter.ViewHolder> {

        private final RecipeListActivity mParentActivity;
        private final List<Recipe> mValues;
        private final View.OnClickListener mOnClickListener = view -> {
            Recipe item = (Recipe) view.getTag();
            Context context = view.getContext();
            Intent intent = new Intent(context, StepListActivity.class);
            intent.putExtra(StepListActivity.ARG_RECIPE_ID, String.valueOf(item.getId()));
            intent.putExtra(StepListActivity.ARG_RECIPE_TITLE, item.getName());
            context.startActivity(intent);

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
            if(!mValues.get(position).getImage().isEmpty()){
                try {
                    Picasso.get().load(mValues.get(position).getImage()).into(holder.mImageView);
                }catch (Exception e){
                    Log.d(TAG, "Error loading image");
                    holder.mImageView.setImageResource(R.drawable.baking_image);
                }
            }
            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R2.id.id_text) TextView mIdView;
            @BindView(R2.id.content) TextView mContentView;
            @BindView(R2.id.iv_baking_image) ImageView mImageView;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}