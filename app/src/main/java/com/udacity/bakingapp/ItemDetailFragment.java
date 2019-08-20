package com.udacity.bakingapp;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.bakingapp.dummy.DummyContent;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.viewModel.BakingViewModel;

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
    private TextView mNameTextView;
    private CollapsingToolbarLayout appBarLayout;
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
        mNameTextView = rootView.findViewById(R.id.item_detail);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            int argId = Integer.valueOf(getArguments().getString(ARG_ITEM_ID));
            BakingViewModel model = ViewModelProviders.of(this).get(BakingViewModel.class);
            model.getRecipes().observe(this, recipes -> {
                recipes.stream().filter(recipe -> recipe.getId() == argId)
                        .findFirst()
                        .ifPresent(recipe -> mItem = recipe);
                if (appBarLayout != null) {
                    appBarLayout.setTitle(mItem.getName());
                }
                mNameTextView.setText(mItem.getName());
            });
        }
    }
}
