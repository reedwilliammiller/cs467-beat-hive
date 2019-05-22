package com.example.metrognome.time;

/**
 * Represents a measure of time.
 *
 * This class is mostly a helper class for the Rhythm class to keep track of how many beats are
 * in this measure.
 *
 * The {@link Rhythm} class keeps track of which beat is the first beat for this measure.
 */
public class Measure {
    private Rhythm rhythm;
    private int numBeats;

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
        this.numBeats = numBeats;
    }

    /**
     * Returns the number of beats in this measure.
     * @return the number of beats.
     */
    public int getBeatCount() {
        return numBeats;
    }

    public void addBeat() {
        this.numBeats++;
    }

    public void removeBeat() {
        if (numBeats == 0) {
            throw new IllegalStateException("Cannot have negative beats.");
        }
        this.numBeats--;
    }

    /**
     * Returns the rhythm this measure belongs to.
     * @return the rhythm.
     */
    public Rhythm getRhythm() {
        return rhythm;
    }

    /**
     * Returns a string representation of this measure.
     * @return this measure as a string.
     */
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Measure: \n");
        stringBuilder.append("Beats: " + getBeatCount() + "\n");
        return stringBuilder.toString();
    }
}