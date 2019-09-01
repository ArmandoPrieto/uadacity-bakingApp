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

import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.model.Step;
import com.udacity.bakingapp.viewModel.BakingViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.udacity.bakingapp.StepListActivity.ARG_RECIPE_ID;
import static com.udacity.bakingapp.StepListActivity.ARG_STEP_ID;

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

    private CollapsingToolbarLayout appBarLayout;
    private Recipe mItem;
    private Step mStep;
    private List<Step> mStepList = new ArrayList<>();
    @BindView(R2.id.tv_step_description) TextView mStepDescriptionTextView;
    @BindView(R2.id.tv_step_videoUrl) TextView mStepVideoUrlTextView;
    @BindView(R2.id.tv_step_thumbnailUrl) TextView mStepThumnailTextView;


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
      View view = inflater.inflate(R.layout.step_detail, container, false);
      ButterKnife.bind(this, view);
      return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BakingViewModel model = ViewModelProviders.of(this).get(BakingViewModel.class);
        int argId = Integer.valueOf(this.getArguments().getString(ARG_RECIPE_ID,"0"));
        int stepId = this.getArguments().getInt(ARG_STEP_ID);
        model.getRecipes().observe(this, recipes -> {
            recipes.stream()
                    .filter(recipe -> recipe.getId() == argId)
                    .findFirst()
                    .ifPresent(recipe -> {
                        mItem = recipe;
                        mItem.getSteps().stream()
                                .filter(step -> step.getId() == stepId)
                                .findFirst()
                                .ifPresent(step -> mStep = step);
                    });
            if(mStep!=null){
                mStepDescriptionTextView.setText(mStep.getDescription());
                mStepVideoUrlTextView.setText(mStep.getVideoURL());
                mStepThumnailTextView.setText(mStep.getThumbnailURL());
            }
        });
    }

}
