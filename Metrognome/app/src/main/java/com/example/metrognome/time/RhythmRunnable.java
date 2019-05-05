package com.example.metrognome.time;

import android.os.Handler;

import com.example.metrognome.audio.SoundPoolWrapper;

public class RhythmRunnable implements Runnable {
    private Rhythm rhythm;
    private Handler handler;
    private SoundPoolWrapper soundPool;

    public RhythmRunnable(Rhythm rhythm, Handler handler, SoundPoolWrapper soundPool) {
        this.rhythm = rhythm;
        this.handler = handler;
        this.soundPool = soundPool;
    }

    @Override
    public void run() {
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
            }
        }
        handler.postDelayed(this, rhythm.getTotalMillis());
    }
}