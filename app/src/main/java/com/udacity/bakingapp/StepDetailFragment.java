package com.udacity.bakingapp;

import android.app.Activity;
import android.os.Bundle;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.model.Step;
import com.udacity.bakingapp.player.MediaPlayerImpl;
import com.udacity.bakingapp.viewModel.BakingViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import static com.udacity.bakingapp.R2.id.tv_no_player_step_description;
import static com.udacity.bakingapp.StepListActivity.ARG_RECIPE_ID;
import static com.udacity.bakingapp.StepListActivity.ARG_STEP_ID;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link StepListActivity}
 * in two-pane mode (on tablets) or a {@link StepDetailActivity}
 * on handsets.
 */
public class StepDetailFragment extends Fragment implements Player.EventListener{
     /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    private static final String TAG = StepDetailFragment.class.toString();
    private PlayerView exoPlayerView;
    private MediaPlayerImpl mediaPlayerImpl;
    private CollapsingToolbarLayout appBarLayout;
    private Recipe mItem;
    private Step mStep;
    @Nullable @BindView(R2.id.tv_step_description) TextView mStepDescriptionTextView;
    @Nullable @BindView(R2.id.iv_step_thumbnailUrl) ImageView mStepThumbnailImageView;
    @Nullable @BindView(tv_no_player_step_description) TextView mNoPlayerStepDescriptionTextView;
    private static final String POSITION = "position";


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.step_detail, container, false);
      exoPlayerView = view.findViewById(R.id.ep_video_view);
      mediaPlayerImpl = new MediaPlayerImpl();
      exoPlayerView.setPlayer(mediaPlayerImpl.getPlayerImpl(this.getActivity(), this));
      ButterKnife.bind(this, view);
      return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
        exoPlayerView.getPlayer().release();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(POSITION, exoPlayerView.getPlayer().getCurrentPosition());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
                if(mStepDescriptionTextView !=null){
                    mStepDescriptionTextView.setText(mStep.getDescription());
                }
                if(!mStep.getThumbnailURL().isEmpty()){
                    try {
                        Picasso.get().load(mStep.getThumbnailURL()).into(mStepThumbnailImageView);
                    }catch (Exception e){
                        Log.d(TAG, getString(R.string.error_loading_image));
                    }
                }
                if(!mStep.getVideoURL().isEmpty()) {
                    if (exoPlayerView != null) {
                        try {
                            if (savedInstanceState != null && savedInstanceState.getLong(POSITION, 0L) != 0L) {
                            mediaPlayerImpl.playFrom(mStep.getVideoURL(), savedInstanceState.getLong(POSITION));
                            } else {
                            mediaPlayerImpl.play(mStep.getVideoURL());
                            }
                        }catch (Exception e){
                            Log.d(TAG, getString(R.string.error_loading_video));
                        }
                    }
                }else{
                    exoPlayerView.setVisibility(View.GONE);
                        if(mNoPlayerStepDescriptionTextView !=null) {
                        mNoPlayerStepDescriptionTextView.setVisibility(View.VISIBLE);
                        mStepThumbnailImageView.setVisibility(View.VISIBLE);
                        mNoPlayerStepDescriptionTextView.setText(mStep.getDescription());
                        }
                }
            }
        });
    }
}
