package com.example.metrognome.time;

/**
 * Represents a time signature such as 3/4 or 5/8 time.
 */
public class TimeSignature {
    private final int beats;
    private final int note;

    public static TimeSignature COMMON_TIME = new TimeSignature(4, 4);

    public TimeSignature(int beats, int note) {
        if (note < 1 || beats < 1) {
            throw new IllegalArgumentException("Must be positive integers: " + beats + "/" + note);
        }
        if (note % 2 != 0 || note >= 16) {
            throw new IllegalArgumentException("Note must be even and less than 16: " + note);
        }
        this.beats = beats;
        this.note = note;
    }

    public int getBeats() {
        return beats;
    }

    public int getNote() {
        return note;
    }
}
