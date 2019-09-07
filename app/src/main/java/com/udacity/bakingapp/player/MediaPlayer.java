package com.udacity.bakingapp.player;

import android.content.Context;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;

public interface MediaPlayer {

    void play(String url);
    void playFrom(String url, long from);
    ExoPlayer getPlayerImpl(Context context, Player.EventListener eventListener);
}
