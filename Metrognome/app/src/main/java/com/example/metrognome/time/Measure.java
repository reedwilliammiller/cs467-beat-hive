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
    private Rhythm rhythm;
    private List<Beat> beats = new ArrayList<>();

    /**
     * Creates a measure with no beats.
     * @param rhythm the rhythm this measure belongs to
     */
    public Measure(Rhythm rhythm) {
        this(rhythm, 0);
    }

    /**
     * Create a measure for the given rhythm with the specified number of beats.
     * @param rhythm the rhythm that this measure belongs to
     * @param numBeats the number of beats to add to this measure.
     */
    public Measure(Rhythm rhythm, int numBeats) {
        this.rhythm = rhythm;
        for (int i = 0; i < numBeats; i++) {
            addBeat(new Beat(this));
        }
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
        return rhythm.getMilliesPerBeat();
    }

    public int getTempo() {
        return rhythm.getTempo();
    }

    public Beat getBeatAt(int index) {
        return beats.get(index);
    }

    public void setBeatAt(int index, Beat beat) {
        beats.set(index, beat);
    }

    public void addBeat(Beat beat) {
        beats.add(beat);
    }

    public void addBeat(int index, Beat beat) {
        beats.add(index, beat);
    }

    public void removeBeat(int index) {
        beats.remove(index);
    }

    public void subdivideBeatAt(int index, int subdivisions) {
        getBeatAt(index).subdivideBy(subdivisions);
    }

    public void playBeatAtSubdivisionAt(int index, int subdivision, SoundPoolWrapper soundPool) {
        getBeatAt(index).playSubdivisionAt(subdivision, soundPool);
    }

    public Rhythm getRhythm() {
        return rhythm;
    }

    public int indexOf(Beat beat) {
        return beats.indexOf(beat);
    }

    @NonNull
    @Override
    public Iterator<Beat> iterator() {
        return beats.iterator();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Measure: \n");
        stringBuilder.append("Beats: " + getBeatCount() + "\n");
        for (int i = 0; i < beats.size(); i++) {
            stringBuilder.append(beats.get(i));
            if (i != beats.size() - 1) {
                stringBuilder.append(",");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}