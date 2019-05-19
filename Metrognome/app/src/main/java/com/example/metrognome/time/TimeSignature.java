package com.example.metrognome.time;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a time signature such as 3/4 or 5/8 time.
 */
public class TimeSignature {
    public static final List<Integer> TOP_SIGNATURES = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
    public static final List<Integer> BOTTOM_SIGNATURES = Arrays.asList(1, 2, 4);

    private final int topSignature;
    private final int bottomSignature;

    public static TimeSignature COMMON_TIME = new TimeSignature(4, 4);

    public TimeSignature(int topSignature, int bottomSignature) {
        if (bottomSignature < 1 || topSignature < 1) {
            throw new IllegalArgumentException("Must be positive integers: " + topSignature + "/" + bottomSignature);
        }
        if (bottomSignature % 2 != 0 || bottomSignature >= 16) {
            throw new IllegalArgumentException("Note must be even and less than 16: " + bottomSignature);
        }
        this.topSignature = topSignature;
        this.bottomSignature = bottomSignature;
    }

    public int getTopSignature() {
        return topSignature;
    }

    public int getBottomSignature() {
        return bottomSignature;
    }

    public String toString() {
        return "TimeSignature: " + getTopSignature() + "/" + getBottomSignature();
    }
}
