package com.example.metrognome.time;

import android.os.Handler;
import android.os.SystemClock;

import com.example.metrognome.audio.SoundPoolWrapper;

public class MeasureRunnable implements Runnable {
    private Measure measure;
    private Handler handler;
    private SoundPoolWrapper soundPool;

    public MeasureRunnable(Measure measure, Handler handler, SoundPoolWrapper soundPool) {
        this.measure = measure;
        this.handler = handler;
        this.soundPool = soundPool;
    }

    @Override
    public void run() {
        for (int i = 0; i < measure.getBeatCount(); i++) {
            Beat beat = measure.getBeatAt(i);
            for (int j = 0; j < beat.getSubdivisions(); j++) {
                final int beatIndex = i;
                final int subdivisionIndex = j;
                final long subdivisionOffset = measure.getSubdivisionOffsetMillisAt(i, j);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        measure.playBeatAtSubdivisionAt(beatIndex, subdivisionIndex, soundPool);
                    }
                }, subdivisionOffset);
            }
        }
        handler.postDelayed(this, measure.getTotalMillis());
    }
}