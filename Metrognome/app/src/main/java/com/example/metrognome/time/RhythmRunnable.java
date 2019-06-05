package com.example.metrognome.time;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.metrognome.R;
import com.example.metrognome.animation.ScrollingLayoutManager;
import com.example.metrognome.audio.SoundPoolWrapper;

public class RhythmRunnable implements Runnable {
    private Rhythm rhythm;
    private Handler handler;
    private SoundPoolWrapper soundPool;
    private RecyclerView recyclerView;
    private int lastpos = 0;

    public RhythmRunnable(Rhythm rhythm, RecyclerView recyclerView, Handler handler, SoundPoolWrapper soundPool) {
        this.rhythm = rhythm;
        this.handler = handler;
        this.soundPool = soundPool;
        this.recyclerView = recyclerView;
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
        for (int k = 0; k < rhythm.getMeasureCount(); k++) {
            final Measure measure = rhythm.getMeasureAt(k);
            for (int i = 0; i < measure.getBeatCount(); i++) {
                Beat beat = measure.getBeatAt(i);
                for (int j = 0; j < beat.getSubdivisions(); j++) {
                    final int beatIndex = i;
                    final int subdivisionIndex = j;
                    final long subdivisionOffset = measure.getSubdivisionOffsetMillisAt(i, j) + k * measure.getTotalMillis();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            measure.playBeatAtSubdivisionAt(beatIndex, subdivisionIndex, soundPool);
                        }
                    }, subdivisionOffset);
                }

                final LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                handler.postDelayed(new Runnable() {
                @Override
                    public void run() {
                        final ObjectAnimator animator = ObjectAnimator.ofArgb(lm.findViewByPosition(lm.findFirstCompletelyVisibleItemPosition()),
                                "backgroundColor", Color.LTGRAY, Color.WHITE).setDuration(measure.getTotalMillis() / measure.getBeatCount());
                        animator.start();

                        float position = recyclerView
                                .getLayoutManager()
                                .findViewByPosition(((LinearLayoutManager) recyclerView.getLayoutManager())
                                        .findFirstCompletelyVisibleItemPosition()).getX();
                        ((ScrollingLayoutManager) recyclerView.getLayoutManager()).deltaDynamicOffset((int)(rhythm.getTempo() - position));
                        recyclerView.smoothScrollToPosition(Integer.MAX_VALUE);
                    }
                }, i * measure.getTotalMillis() / measure.getBeatCount());
            }
        }
        handler.postDelayed(this, rhythm.getTotalMillis());
    }
}