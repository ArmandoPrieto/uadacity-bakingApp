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
 * This fragment is either contained in a {@link StepListActivity}
 * in two-pane mode (on tablets) or a {@link StepDetailActivity}
 * on handsets.
 */
public class StepDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_LAYOUT_ID = "layout_id";
    private CollapsingToolbarLayout appBarLayout;
    private List<Ingredient> mIngredientList = new ArrayList<>();
    private RecyclerView.Adapter mIngredientListAdapter;
    private Recipe mItem;



    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StepDetailFragment() {
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
        View rootView = null;
        int layout = this.getArguments().getInt(ARG_LAYOUT_ID, R.layout.ingedients_detail);
        switch (layout){
            case R.layout.step_detail:
                rootView = inflater.inflate(R.layout.step_detail, container, false);
                break;
            case R.layout.ingedients_detail:
                rootView = inflater.inflate(R.layout.ingedients_detail, container, false);
                break;
        }
       return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View ingredientListRecyclerView;
        int layout = this.getArguments().getInt(ARG_LAYOUT_ID, R.layout.ingedients_detail);
        switch (layout){
            case R.layout.step_detail:
                break;
            case R.layout.ingedients_detail:
                ingredientListRecyclerView = view.findViewById(R.id.rv_recipe_ingredients);
                setupIngredientListRecyclerView((RecyclerView) ingredientListRecyclerView, view);
                int argId = Integer.valueOf(this.getArguments().getString(ARG_ITEM_ID,"0"));
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
                break;
         }





    }

    private void setupIngredientListRecyclerView(@NonNull RecyclerView recyclerView, View view) {
        mIngredientListAdapter = new IngredientListRecyclerViewAdapter(view, mIngredientList);
        recyclerView.setAdapter(mIngredientListAdapter);
    }

}
