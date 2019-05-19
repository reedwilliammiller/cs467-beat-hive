package com.example.metrognome.time;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents an entire rhythm which is a sequence of measures.
 */
public class Rhythm implements Iterable<Measure> {
    public static final int DEFAULT_TEMPO = 60;
    private List<Measure> measures = new ArrayList<>();
    private int tempo;

    public static Rhythm BASIC;
    static {
        BASIC = new Rhythm();
        Measure first = new Measure(BASIC, 0, TimeSignature.COMMON_TIME);
        BASIC.addMeasure(first);
    }

    public Rhythm() {
        this(DEFAULT_TEMPO);
    }

    public Rhythm(int tempo) {
        this.tempo = tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public int getTempo() {
        return tempo;
    }

    public long getTotalMillis() {
        long total = 0;
        for (Measure m : measures) {
            total += m.getTotalMillis();
        }
        return total;
    }

    public void addMeasure(Measure measure) {
        measures.add(measure);
    }

    public Measure getMeasureAt(int index) {
        return measures.get(index);
    }

    public void removeMeasureAt(int index) {
        measures.remove(index);
    }

    public int getMeasureCount() {
        return measures.size();
    }

    public int getBeatCount() {
        int count = 0;
        for (Measure measure : measures) {
            count += measure.getBeatCount();
        }
        return count;
    }

    public Beat getBeatAt(final int beatIndex) {
        int measureIndex = 0;
        int currentBeatIndex = 0;
        int beatCount = 0;
        while (beatCount < beatIndex) {
            Measure currentMeasure = getMeasureAt(measureIndex);
            currentBeatIndex++;
            if (currentBeatIndex == currentMeasure.getBeatCount()) {
                currentBeatIndex = 0;
                measureIndex++;
            }
            beatCount++;
        }
        return measures.get(measureIndex).getBeatAt(currentBeatIndex);
    }

    public boolean isFirstBeatOfMeasureAt(final int beatIndex) {
        int currentBeatIndex = 0;
        for (Measure measure : measures) {
            if (currentBeatIndex == beatIndex) {
                return true;
            }
            currentBeatIndex += measure.getBeatCount();
        }
        return false;
    }

    public int getMeasureIndexOfBeatAt(final int beatIndex) {
        int currentBeatIndex = 0;
        for (int i = 0; i < measures.size(); i++) {
            if (currentBeatIndex >= beatIndex) {
                return i;
            }
            currentBeatIndex += measures.get(i).getBeatCount();
        }
        return measures.size() - 1;
    }

    public Iterator<Measure> iterator() {
        return measures.iterator();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Rhythm: \n");
        stringBuilder.append("Tempo: ");
        stringBuilder.append(tempo);
        stringBuilder.append("\n");
        for (Measure measure : this) {
            stringBuilder.append(measure);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
