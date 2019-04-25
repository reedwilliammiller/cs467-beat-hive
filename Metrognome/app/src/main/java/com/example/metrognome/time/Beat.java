package com.example.metrognome.time;

import com.example.metrognome.audio.SoundPoolWrapper;

import java.util.Arrays;

/**
 * Represents an audible or inaudible series of notes that are subdivided.
 */
public class Beat {
    private int[] soundIds;

    public Beat() {
        this(1);
    }

    public Beat(int numSubdivisions) {
        subdivideBy(numSubdivisions);
    }

    public void subdivideBy(int numSubdivisions) {
        if (numSubdivisions < 1) {
            throw new IllegalArgumentException("Subdivisions must be greater than 1: " + numSubdivisions);
        }
        soundIds = new int[numSubdivisions];
        Arrays.fill(soundIds, SoundPoolWrapper.INAUDIBLE);
        setSoundAt(0, SoundPoolWrapper.DEFAULT_SOUND);
    }

    public int getSubdivisions() {
        return soundIds.length;
    }

    public int getSoundAt(int subdivision) {
        return soundIds[subdivision];
    }

    public void setSoundAt(int subdivision, int soundPoolIndex) {
        soundIds[subdivision] = soundPoolIndex;
    }

    public void playSubdivisionAt(int subdivision, SoundPoolWrapper soundPool) {
        soundPool.play(getSoundAt(subdivision));
    }
}
