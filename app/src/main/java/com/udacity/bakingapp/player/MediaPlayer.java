package com.udacity.bakingapp.player;

import android.content.Context;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;

public interface MediaPlayer {

    public void play(String url);
    public void playFrom(String url, long from);
    public ExoPlayer getPlayerImpl(Context context, Player.EventListener eventListener);
}
