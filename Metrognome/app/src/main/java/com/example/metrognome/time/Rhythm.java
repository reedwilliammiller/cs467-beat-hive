package com.example.metrognome.time;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents an entire rhythm which is a sequence of measures.
 */
public class Rhythm implements Iterable<Measure> {
    public static final int DEFAULT_TEMPO = 60;
    public static final int MAX_BPM = 400;
    public static final int MIN_BPM = 1;

    private static final long MILLIS_PER_BPM = 60000;
    private List<Measure> measures = new ArrayList<>();
    private int tempo;

    public static Rhythm BASIC;
    static {
        BASIC = new Rhythm();
        Measure first = new Measure(BASIC);
        BASIC.addMeasure(first);
    }

    public Rhythm() {
        this(DEFAULT_TEMPO);
    }

    public Rhythm(int tempo) {
        setTempo(tempo);
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public int getTempo() {
        return tempo;
    }

    public long getTotalMillis() {
        return getBeatCount() * getMilliesPerBeat();
    }

    public void addMeasure(Measure measure) {
        measures.add(measure);
    }

    public void addMeasureAt(int index, Measure measure) {
        measures.add(index, measure);
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

    public int indexOf(Measure measure) {
        return measures.indexOf(measure);
    }

    public int indexOf(Beat beat) {
        int count = 0;
        for (Measure measure : this) {
            int index = measure.indexOf(beat);
            if (index != -1) {
                return index + count;
            }
            count += measure.getBeatCount();
        }
        return -1;
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

    public long getMilliesPerBeat() {
        return MILLIS_PER_BPM / getTempo();
    }
}
