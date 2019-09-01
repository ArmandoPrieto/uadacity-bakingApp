package com.udacity.bakingapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.udacity.bakingapp.model.Ingredient;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.viewModel.BakingViewModel;

import java.util.ArrayList;
import java.util.List;


import static com.udacity.bakingapp.StepListActivity.ARG_RECIPE_ID;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link StepListActivity}
 * in two-pane mode (on tablets) or a {@link StepDetailActivity}
 * on handsets.
 */
public class IngredientDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */

    private CollapsingToolbarLayout appBarLayout;
    private List<Ingredient> mIngredientList = new ArrayList<>();
    private RecyclerView.Adapter mIngredientListAdapter;
    private Recipe mItem;



    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public IngredientDetailFragment() {
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
       return inflater.inflate(R.layout.ingedients_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View ingredientListRecyclerView = view.findViewById(R.id.rv_recipe_ingredients);
        setupIngredientListRecyclerView((RecyclerView) ingredientListRecyclerView, view);
        int argId = Integer.valueOf(this.getArguments().getString(ARG_RECIPE_ID,"0"));
        BakingViewModel model = ViewModelProviders.of(this).get(BakingViewModel.class);
        model.getRecipes().observe(this, recipes -> {
            recipes.stream().filter(recipe -> recipe.getId() == argId)
                    .findFirst()
                    .ifPresent(recipe -> mItem = recipe);
            if(mItem!=null) {
                mIngredientList.clear();
                mItem.getIngredients().forEach(ingredient -> mIngredientList.add(ingredient));
                mIngredientListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setupIngredientListRecyclerView(@NonNull RecyclerView recyclerView, View view) {
        mIngredientListAdapter = new IngredientListRecyclerViewAdapter(view, mIngredientList);
        recyclerView.setAdapter(mIngredientListAdapter);
    }

}
