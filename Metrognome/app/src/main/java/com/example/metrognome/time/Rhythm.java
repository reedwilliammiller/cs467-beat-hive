package com.example.metrognome.time;

import com.example.metrognome.audio.SoundPoolWrapper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Represents an entire rhythm which is a sequence of measures.
 */
public class Rhythm implements Iterable<Beat> {
    private static final String TAG = Rhythm.class.getSimpleName();
    public static final int DEFAULT_TEMPO = 60;
    public static final int MAX_BPM = 400;
    public static final int MIN_BPM = 1;

    private static final long MILLIS_PER_BPM = 60000;
    private Map<Measure, Integer> measures = new HashMap<>();
    private List<Beat> beats = new LinkedList<>();
    private int tempo;

    public static Rhythm BASIC;
    static {
        BASIC = new Rhythm();
        Measure first = new Measure(BASIC, 4);
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

    /**
     * Puts a measure at the end of the list.
     */
    public void addMeasure(final Measure measure) {
        addMeasureAt(getBeatCount(), measure);
    }

    private void addMeasureAt(final int index, final Measure measure) {
        if (index > getBeatCount() || index < 0) {
            throw new IllegalArgumentException(String.format("Invalid index: %d", index));
        }
        measures.put(measure, index);
        for (int i = 0; i < measure.getBeatCount(); i++) {
            beats.add(index + i, new Beat(measure));
        }
    }

    /**
     * Returns the measure that for the specified beat index.
     * @param index
     * @return
     */
    public Measure getMeasureAt(final int index) {
        return getBeatAt(index).getMeasure();
    }

    /**
     * Removes the measure at the specified beat index and all the beats associated with it.
     * @param index index of the measure.
     */
    public void removeMeasureAt(final int index) {
        Measure toRemove = getMeasureAt(index);
        int startIndex = measures.get(toRemove);
        int beatCount = toRemove.getBeatCount();
        for (int i = startIndex + beatCount - 1; i >= startIndex; i--) {
            beats.remove(i);
        }
        measures.remove(toRemove);
    }

    public int indexOf(final Measure measure) {
        return measures.get(measure);
    }

    /**
     * Returns the number for this beat in the measure.
     *
     * @param beat the beat.
     * @return
     */
    public int getBeatIndexInMeasure(final Beat beat) {
        int measureIndex = measures.get(beat.getMeasure());
        int beatIndex = indexOf(beat);
        return beatIndex - measureIndex + 1;
    }

    public int getMeasureCount() {
        return measures.size();
    }

    public int indexOf(final Beat beat) {
        return beats.indexOf(beat);
    }

    public int getBeatCount() {
        return beats.size();
    }

    public Beat getBeatAt(final int beatIndex) {
        return beats.get(beatIndex);
    }

    public long getMilliesPerBeat() {
        return MILLIS_PER_BPM / getTempo();
    }

    public long getBeatOffsetMillies(int index) {
        return index * getMilliesPerBeat();
    }

    public void playBeatSubdivisionAt(int index, int subdivision, SoundPoolWrapper soundPool) {
        getBeatAt(index).playSubdivisionAt(subdivision, soundPool);
    }

    public Iterator<Beat> iterator() {
        return beats.iterator();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Rhythm: \n");
        stringBuilder.append("Tempo: ");
        stringBuilder.append(tempo);
        stringBuilder.append("\n");

        for (Beat beat : this) {
            stringBuilder.append(beat);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
