package com.example.metrognome.time;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;

import com.example.metrognome.audio.SoundPoolWrapper;

public class RhythmRunnable implements Runnable {
    private Rhythm rhythm;
    private Handler handler;
    private SoundPoolWrapper soundPool;
    private RecyclerView recyclerView;
    private int iterations;

    public RhythmRunnable(Rhythm rhythm, RecyclerView recyclerView, Handler handler, SoundPoolWrapper soundPool) {
        this.rhythm = rhythm;
        this.handler = handler;
        this.soundPool = soundPool;
        this.recyclerView = recyclerView;
        this.iterations = 1;
    }

    @Override
    public void run() {
        for (int i = 0; i < rhythm.getBeatCount(); i++) {
            Beat beat = rhythm.getBeatAt(i);
            long beatOffset = rhythm.getBeatOffsetMillies(i);
            for (int j = 0; j < beat.getSubdivisions(); j++) {
                final int beatIndex = i;
                final int subdivisionIndex = j;
                long subdivisionOffset = rhythm.getSubdivisionOffsetForBeat(beat, j);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rhythm.playBeatSubdivisionAt(beatIndex, subdivisionIndex, soundPool);
                    }
                }, subdivisionOffset + beatOffset);
            }
        }
        handler.postDelayed(this, rhythm.getTotalMillis());
    }
}