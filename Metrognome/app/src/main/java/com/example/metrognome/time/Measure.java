package com.example.metrognome.time;

import android.support.annotation.NonNull;

import com.example.metrognome.audio.SoundPoolWrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a measure of time.
 */
public class Measure implements Iterable<Beat> {
    private static final long MILLIS_PER_BPM = 60000;
    private static final int DEFAULT_TEMPO = 60;

    private TimeSignature timeSignature;
    private int tempo;
    private List<Beat> beats = new ArrayList<>();

    /**
     * Creates a simple measure of 4/4 time at 60 bpm.
     */
    public Measure() {
        this(TimeSignature.COMMON_TIME, DEFAULT_TEMPO);
    }

    public static Measure CLAVE = new Measure();
    static {
        CLAVE.getBeatAt(0).subdivideBy(4);
        CLAVE.getBeatAt(1).subdivideBy(4);
        CLAVE.getBeatAt(2).subdivideBy(4);
        CLAVE.getBeatAt(3).subdivideBy(4);

        CLAVE.getBeatAt(0).setSoundAt(3, SoundPoolWrapper.DEFAULT_SOUND);
        CLAVE.getBeatAt(1).setSoundAt(0, SoundPoolWrapper.INAUDIBLE);
        CLAVE.getBeatAt(1).setSoundAt(3, SoundPoolWrapper.DEFAULT_SOUND);
        CLAVE.getBeatAt(2).setSoundAt(0, SoundPoolWrapper.INAUDIBLE);
        CLAVE.getBeatAt(2).setSoundAt(2, SoundPoolWrapper.DEFAULT_SOUND);
    }

    /**
     * Create a measure with the given {@link TimeSignature} and tempo in BPM.
     * @param timeSignature the time signature (i.e. 4/4)
     * @param tempo the tempo in BPM.
     */
    public Measure(TimeSignature timeSignature, int tempo) {
        setTimeSignature(timeSignature);
        setTempo(tempo);
    }

    /**
     * Sets the time signature for the measure and gives default beats.
     * @param timeSignature the time signature.
     */
    public void setTimeSignature(TimeSignature timeSignature) {
        this.timeSignature = timeSignature;
        for (int i = 0; i < timeSignature.getBeats(); i++) {
            beats.add(new Beat());
        }
    }

    /**
     * Sets the tempo for this measure and updates the offsets for each beat.
     * @param tempo the tempo in bpm.
     */
    public void setTempo(int tempo) {
        if (tempo < 1 || tempo >= 600) {
            throw new IllegalArgumentException("Tempo must be between 1 and 600: " + tempo);
        }
        this.tempo = tempo;
    }

    public long getBeatOffsetMillisAt(int index) {
        return index * getMillisPerBeat();
    }

    public long getSubdivisionOffsetMillisAt(int index, int subdivision) {
        return getBeatOffsetMillisAt(index) + (subdivision * getMillisPerBeat()) / getBeatAt(index).getSubdivisions();
    }


    public int getBeatCount() {
        return beats.size();
    }

    public long getTotalMillis() {
        return getBeatCount() * getMillisPerBeat();
    }

    private long getMillisPerBeat() {
        return MILLIS_PER_BPM / tempo;
    }

    public int getTempo() {
        return tempo;
    }

    public TimeSignature getTimeSignature() {
        return timeSignature;
    }

    public Beat getBeatAt(int index) {
        return beats.get(index);
    }

    public void subdivideBeatAt(int index, int subdivisions) {
        getBeatAt(index).subdivideBy(subdivisions);
    }

    public void playBeatAtSubdivisionAt(int index, int subdivision, SoundPoolWrapper soundPool) {
        getBeatAt(index).playSubdivisionAt(subdivision, soundPool);
    }

    @NonNull
    @Override
    public Iterator<Beat> iterator() {
        return beats.iterator();
    }
}
