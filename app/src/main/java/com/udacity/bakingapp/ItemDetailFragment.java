package com.udacity.bakingapp;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

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
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    private Recipe mItem;
    private TextView mRecipeNameTextView;
    private TextView mRecipeServingsTextView;
    private CollapsingToolbarLayout appBarLayout;
    private List<Ingredient> mIngredientList = new ArrayList<>();
    private RecyclerView.Adapter mIngredientListAdapter;
    private List<Step> mStepList = new ArrayList<>();
    private RecyclerView.Adapter mStepListAdapter;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = this.getActivity();
        appBarLayout = activity.findViewById(R.id.toolbar_layout);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        mRecipeServingsTextView = rootView.findViewById(R.id.tv_recipe_servings);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View ingredientListRecyclerView = view.findViewById(R.id.rv_recipe_ingredients);
        setupIngredientListRecyclerView((RecyclerView) ingredientListRecyclerView, view);
        View stepListRecyclerView = view.findViewById(R.id.rv_recipe_steps);
        setupStepListRecyclerView((RecyclerView) stepListRecyclerView, view);

        if (getArguments()!= null && getArguments().containsKey(ARG_ITEM_ID)) {
            int argId = Integer.valueOf(getArguments().getString(ARG_ITEM_ID));
            BakingViewModel model = ViewModelProviders.of(this).get(BakingViewModel.class);
            model.getRecipes().observe(this, recipes -> {
                recipes.stream().filter(recipe -> recipe.getId() == argId)
                        .findFirst()
                        .ifPresent(recipe -> mItem = recipe);
                mIngredientList.clear();
                mItem.getIngredients().forEach(ingredient -> mIngredientList.add(ingredient));
                mIngredientListAdapter.notifyDataSetChanged();
                mStepList.clear();
                mItem.getSteps().forEach(step -> mStepList.add(step));
                mStepListAdapter.notifyDataSetChanged();
                if (appBarLayout != null) {
                    appBarLayout.setTitle(mItem.getName());
                }

                mRecipeServingsTextView.setText(String.valueOf(mItem.getServings()));
            });
        }
    }

    private void setupIngredientListRecyclerView(@NonNull RecyclerView recyclerView, View parent) {
        mIngredientListAdapter = new IngredientListRecyclerViewAdapter(parent, mIngredientList);
        recyclerView.setAdapter(mIngredientListAdapter);
    }

    private void setupStepListRecyclerView(@NonNull RecyclerView recyclerView, View parent) {
        mStepListAdapter = new StepListRecyclerViewAdapter(parent, mStepList);
        recyclerView.setAdapter(mStepListAdapter);
    }

    public static class StepListRecyclerViewAdapter
            extends RecyclerView.Adapter<ItemDetailFragment.StepListRecyclerViewAdapter.ViewHolder> {

        private final View mParentView;
        private final List<Step> mStepValues;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };

        StepListRecyclerViewAdapter(View parent, List<Step> step) {
            mStepValues = step;
            mParentView = parent;

        }

        @Override
        public ItemDetailFragment.StepListRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_steps_list_content, parent, false);
            return new ItemDetailFragment.StepListRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ItemDetailFragment.StepListRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mStepShortDescriptionTextView.setText(mStepValues.get(position).getShortDescription());
            holder.mStepDescriptionTextView.setText(mStepValues.get(position).getShortDescription());
            holder.mStepVideoUrlTextView.setText(mStepValues.get(position).getVideoURL());
            holder.mStepThumbnailUrlTextView.setText(mStepValues.get(position).getThumbnailURL());
            //holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mStepValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R2.id.tv_step_short_description) TextView mStepShortDescriptionTextView;
            @BindView(R2.id.tv_step_description) TextView mStepDescriptionTextView;
            @BindView(R2.id.tv_step_videoUrl) TextView mStepVideoUrlTextView;
            @BindView(R2.id.tv_step_thumbnailUrl) TextView mStepThumbnailUrlTextView;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }

    public static class IngredientListRecyclerViewAdapter
            extends RecyclerView.Adapter<ItemDetailFragment.IngredientListRecyclerViewAdapter.ViewHolder> {

        private final View mParentView;
        private final List<Ingredient> mIngredientValues;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };

        IngredientListRecyclerViewAdapter(View parent, List<Ingredient> ingredients) {
            mIngredientValues = ingredients;
            mParentView = parent;
        }

        @Override
        public ItemDetailFragment.IngredientListRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_ingredients_list_content, parent, false);
            return new ItemDetailFragment.IngredientListRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ItemDetailFragment.IngredientListRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mIngredientNameTextView.setText(String.valueOf(mIngredientValues.get(position).getIngredient()));
            holder.mIngredientQuantityTextView.setText(String.valueOf(mIngredientValues.get(position).getQuantity()));
            holder.mIngredientMeasureTextView.setText(mIngredientValues.get(position).getMeasure());
            //holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mIngredientValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R2.id.tv_ingredient_name) TextView mIngredientNameTextView;
            @BindView(R2.id.tv_ingredient_quantity) TextView mIngredientQuantityTextView;
            @BindView(R2.id.tv_ingredient_measure) TextView mIngredientMeasureTextView;


            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}
