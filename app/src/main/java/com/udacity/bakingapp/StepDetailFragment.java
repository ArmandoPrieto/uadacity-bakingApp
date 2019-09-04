package com.udacity.bakingapp;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;
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
import com.udacity.bakingapp.player.MediaPlayer;
import com.udacity.bakingapp.player.MediaPlayerImpl;
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
public class StepDetailFragment extends Fragment implements Player.EventListener{
     /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    private PlayerView exoPlayerView;
    private MediaPlayerImpl mediaPlayerImpl;
    private CollapsingToolbarLayout appBarLayout;
    private Recipe mItem;
    private Step mStep;
    private List<Step> mStepList = new ArrayList<>();
    @Nullable @BindView(R2.id.tv_step_description) TextView mStepDescriptionTextView;
    @Nullable @BindView(R2.id.tv_step_videoUrl) TextView mStepVideoUrlTextView;
    @Nullable @BindView(R2.id.tv_step_thumbnailUrl) TextView mStepThumnailTextView;
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
    public void onResume() {
        super.onResume();

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
                    mStepVideoUrlTextView.setText(mStep.getVideoURL());
                    mStepThumnailTextView.setText(mStep.getThumbnailURL());
                }
                if(exoPlayerView!=null) {
                    if (savedInstanceState != null && savedInstanceState.getLong(POSITION, 0L) != 0L) {
                        mediaPlayerImpl.playFrom(mStep.getVideoURL(), savedInstanceState.getLong(POSITION));
                    } else {
                        mediaPlayerImpl.play(mStep.getVideoURL());
                    }
                }
            }
        });

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case Player.STATE_BUFFERING:
                //You can use progress dialog to show user that video is preparing or buffering so please wait
                break;
            case Player.STATE_IDLE:
                //idle state
                break;
            case Player.STATE_READY:
                // dismiss your dialog here because our video is ready to play now
                break;
            case Player.STATE_ENDED:
                // do your processing after ending of video
                break;
        }

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }



}
