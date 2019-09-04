package com.udacity.bakingapp.player;

import android.content.Context;
import android.net.Uri;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.udacity.bakingapp.R;

public class MediaPlayerImpl implements MediaPlayer {

    ExoPlayer mExoPlayer;
    Player.EventListener eventListener;
    Context context;

    private void initializePlayer() {
        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        DefaultLoadControl loadControl = new DefaultLoadControl();
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(context);
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                context, renderersFactory, trackSelector, loadControl);
        mExoPlayer.addListener(eventListener);

    }

    @Override
    public void play(String url) {
        String userAgent = Util.getUserAgent(context, context.getString(R.string.app_name));
        MediaSource mediaSource = new ExtractorMediaSource
                .Factory(new DefaultDataSourceFactory(context, userAgent))
                .setExtractorsFactory(new DefaultExtractorsFactory())
                .createMediaSource(Uri.parse(url));
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);
    }
    @Override
    public void playFrom(String url, long from) {
        String userAgent = Util.getUserAgent(context, context.getString(R.string.app_name));
        MediaSource mediaSource = new ExtractorMediaSource
                .Factory(new DefaultDataSourceFactory(context, userAgent))
                .setExtractorsFactory(new DefaultExtractorsFactory())
                .createMediaSource(Uri.parse(url));
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.seekTo(from);
        mExoPlayer.setPlayWhenReady(false);
    }

    @Override
    public ExoPlayer getPlayerImpl(Context context, Player.EventListener eventListener) {
        this.context = context;
        this.eventListener = eventListener;
        initializePlayer();
        return mExoPlayer;
    }


}
