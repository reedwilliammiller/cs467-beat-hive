package com.example.metrognome.time;

import com.example.metrognome.audio.SoundPoolWrapper;

/**
 * Represents an audible or inaudible series of notes that are subdivided.
 */
public class Beat {
    private static final int MIN_SUBDIVISIONS = 1;
    private static final int MAX_SUBDIVISIONS = 4;
    private Measure measure;
    private int[] soundIds = new int[MAX_SUBDIVISIONS];
    private int numSubdivisions;

    /**
     * Constructs a beat with 1 subdivision.
     * @param measure the measure this beat belongs to.
     */
    public Beat(Measure measure) {
        this(measure, MIN_SUBDIVISIONS);
    }

    /**
     * Constructs a beat that has up to 4 subdivisions.
     * @param measure the measure this beat belongs to.
     * @param numSubdivisions the number of subdivision, must be between 1 and 4.
     */
    public Beat(Measure measure, int numSubdivisions) {
        this.measure = measure;
        subdivideBy(numSubdivisions);
    }

    /**
     * Subdivides this beat.
     * @param numSubdivisions the number of subdivisions to subdivide be.
     */
    public void subdivideBy(int numSubdivisions) {
        if (numSubdivisions < MIN_SUBDIVISIONS || numSubdivisions > MAX_SUBDIVISIONS) {
            throw new IllegalArgumentException("Subdivisions must be between 1 and 4: " + numSubdivisions);
        }
        this.numSubdivisions = numSubdivisions;
        for (int i = 1; i < numSubdivisions; i++) {
            setSoundAt(i, SoundPoolWrapper.INAUDIBLE);
        }
        setSoundAt(0, SoundPoolWrapper.DEFAULT_SOUND);
    }

    /**
     * Adds a subdivision to the end of this beat.
     */
    public void addSubdivision() {
        if (getSubdivisions() == MAX_SUBDIVISIONS) {
            throw new IllegalStateException("Cannot have more than 4 subdivisions.");
        }
        this.numSubdivisions++;
    }

    /**
     * Removes the last subdivision.
     */
    public void removeSubdivision() {
        if (getSubdivisions() == MIN_SUBDIVISIONS) {
            throw new IllegalStateException("Cannot have 0 subdivisions.");
        }
        soundIds[numSubdivisions - 1] = SoundPoolWrapper.INAUDIBLE;
        numSubdivisions--;
    }

    /**
     * Returns the number of subdivisions in this beat.
     * @return the number of subdivisions.
     */
    public int getSubdivisions() {
        return numSubdivisions;
    }

    /**
     * Returns the sound id at the specified subdivision.
     * @param subdivision the subdivision to get.
     * @return the sound id.
     */
    public int getSoundAt(int subdivision) {
        return soundIds[subdivision];
    }

    /**
     * Sets the sound id at the specified subdivision.
     * @param subdivision the subdivision to set.
     * @param soundPoolId the sound id.
     */
    public void setSoundAt(int subdivision, int soundPoolId) {
        soundIds[subdivision] = soundPoolId;
    }

    /**
     * Plays the sound at the specified subdivision.
     * @param subdivision the subdivision to play.
     * @param soundPool the sound pool to play with.
     */
    public void playSubdivisionAt(int subdivision, SoundPoolWrapper soundPool) {
        soundPool.play(getSoundAt(subdivision));
    }

    /**
     * Gets the measure that this beat belongs to.
     * @return the measure for this beat.
     */
    public Measure getMeasure() {
        return measure;
    }

    /**
     * Gets the offset in ms for the specified subdivision.
     * @param subdivision the subdivision to get.
     * @return the offset in ms for this subdivision.
     */
    public long getSubdivisionOffset(int subdivision) {
        return measure.getRhythm().getMilliesPerBeat() / getSubdivisions() * subdivision;
    }

    /**
     * Returns a string representation of this beat.
     * @return this beat as a string.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Beat:\n");
        for (int i = 0; i < numSubdivisions; i++) {
            if (i == 0) {
                sb.append("[");
            }
            sb.append(soundIds[i]);
            if (i != numSubdivisions - 1) {
                sb.append(",");
            } else {
                sb.append("]\n");
            }
        }
        return sb.toString();
    }
}
