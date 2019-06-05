package com.example.metrognome.time;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.metrognome.animation.ScrollingLayoutManager;
import com.example.metrognome.audio.SoundPoolWrapper;

public class RhythmRunnable implements Runnable {
    private Rhythm rhythm;
    private Handler handler;
    private SoundPoolWrapper soundPool;
    private RecyclerView recyclerView;

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

                final LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                handler.postDelayed(new Runnable() {
                @Override
                    public void run() {
                        final ObjectAnimator animator = ObjectAnimator.ofArgb(lm.findViewByPosition(lm.findFirstCompletelyVisibleItemPosition()),
                                "backgroundColor", Color.LTGRAY, Color.WHITE).setDuration(rhythm.getMilliesPerBeat());
                        animator.start();

                        float position = recyclerView
                                .getLayoutManager()
                                .findViewByPosition(((LinearLayoutManager) recyclerView.getLayoutManager())
                                        .findFirstCompletelyVisibleItemPosition()).getX();
                        ((ScrollingLayoutManager) recyclerView.getLayoutManager()).deltaDynamicOffset((int)(rhythm.getTempo() - position));
                        recyclerView.smoothScrollToPosition(Integer.MAX_VALUE);
                    }
                }, i * rhythm.getMilliesPerBeat());
            }
        }
        handler.postDelayed(this, rhythm.getTotalMillis());
    }
}