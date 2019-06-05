package com.example.beathive.time;

/**
 * Represents a measure of time.
 *
 * This class is mostly a helper class for the Rhythm class to keep track of how many beats are
 * in this measure.
 */
public class Measure {
    private int numBeats;

    /**
     * Creates a measure with no beats.
     */
    public Measure() {
        this(0);
    }

    /**
     * Create a measure for the given rhythm with the specified number of beats.
     * @param numBeats the number of beats to add to this measure.
     */
    public Measure(int numBeats) {
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