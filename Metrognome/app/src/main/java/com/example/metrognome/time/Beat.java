package com.example.metrognome.time;

import com.example.metrognome.audio.SoundPoolWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an audible or inaudible series of notes that are subdivided.
 */
public class Beat {
    private Measure measure;
    private int index;
    private List<Integer> soundIds = new ArrayList<>();

    public Beat(Measure measure, int index) {
        this(measure, index, 1);
    }

    public Beat(Measure measure, int index, int numSubdivisions) {
        this.measure = measure;
        this.index = index;
        subdivideBy(numSubdivisions);
    }

    public void subdivideBy(int numSubdivisions) {
        if (numSubdivisions < 1) {
            throw new IllegalArgumentException("Subdivisions must be greater than 1: " + numSubdivisions);
        }
        soundIds = new ArrayList<>();
        for (int i = 0; i < numSubdivisions; i++) {
            addSubdivision();
        }
        setSoundAt(0, SoundPoolWrapper.DEFAULT_SOUND);
    }

    public void addSubdivision() {
        soundIds.add(SoundPoolWrapper.INAUDIBLE);
    }

    public void removeSubdivision() {
        if (soundIds.size() == 1) {
            throw new IllegalStateException("Cannot have no subdivisions.");
        }
        soundIds.remove(soundIds.size() - 1);
    }

    public int getSubdivisions() {
        return soundIds.size();
    }

    public int getSoundAt(int subdivision) {
        return soundIds.get(subdivision);
    }

    public void setSoundAt(int subdivision, int soundPoolIndex) {
        soundIds.set(subdivision, soundPoolIndex);
    }

    public void playSubdivisionAt(int subdivision, SoundPoolWrapper soundPool) {
        soundPool.play(getSoundAt(subdivision));
    }

    public Measure getMeasure() {
        return measure;
    }

    public int getIndex() {
        return this.index;
    }

    public String toString() {
        return "Beat: " + soundIds;
    }
}
