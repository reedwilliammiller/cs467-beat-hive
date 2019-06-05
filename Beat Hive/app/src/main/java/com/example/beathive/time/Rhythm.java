package com.example.beathive.time;

import android.util.Log;

import com.example.beathive.audio.SoundPoolWrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents an entire rhythm which is a sequence of measures.
 */
public class Rhythm implements Iterable<Beat> {
    private static final String TAG = Rhythm.class.getSimpleName();
    public static final int DEFAULT_TEMPO = 60;
    public static final int MAX_BPM = 400;
    public static final int MIN_BPM = 1;

    private static final long MILLIS_PER_BPM = 60000;
    private List<Measure> measures = new LinkedList<>();
    private List<Beat> beats = new LinkedList<>();
    private int tempo;

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
        addMeasureAt(measures.size(), measure);
    }

    public List<Measure> getMeasures() {
        return measures;
    }

    public void addMeasureAt(final int measureIndex, final Measure measure) {
        if (measureIndex > measures.size() || measureIndex < 0) {
            throw new IllegalArgumentException(String.format("Invalid index: %d", measureIndex));
        }
        measures.add(measureIndex, measure);
        for (int i = 0; i < measure.getBeatCount(); i++) {
            beats.add(getBeatsBeforeMeasure(measure), new Beat());
        }
    }

    public void removeMeasureAt(final int measureIndex) {
        if (measureIndex >= measures.size() || measureIndex < 0) {
            throw new IllegalArgumentException(String.format("Invalid index: %d", measureIndex));
        }
        Measure toRemove = measures.get(measureIndex);
        int firstBeatIndex = getBeatsBeforeMeasure(toRemove);
        int lastBeatIndex = firstBeatIndex + toRemove.getBeatCount() - 1;
        for (int i = lastBeatIndex; i >= firstBeatIndex; i--) {
            beats.remove(i);
        }
        measures.remove(toRemove);
    }

    public void removeBeatAt(final int beatIndex) {
        final Measure measureForBeat = getMeasureForBeatAt(beatIndex);
        if (measureForBeat.getBeatCount() == 1) {
            measures.remove(measureForBeat);
        }
        measureForBeat.removeBeat();
        beats.remove(beatIndex);
    }

    public void addBeatAt(final int beatIndex) {
        final Measure measureForBeat = getMeasureForBeatAt(beatIndex);
        measureForBeat.addBeat();
        beats.add(beatIndex, new Beat());
    }

    public Measure getMeasureAt(final int index) {
        return measures.get(index);
    }

    public List<Integer> getIndicesForFirstBeatsOfEachMeasure() {
        List<Integer> indices = new ArrayList<>();
        int beatIndex = 0;
        for (Measure measure : measures) {
            indices.add(beatIndex);
            beatIndex += measure.getBeatCount();
        }
        return indices;
    }

    public int getIndexOfFirstBeatOfNextMeasure(int index) {
        List<Integer> firstBeatIndices = getIndicesForFirstBeatsOfEachMeasure();
        for (int firstBeatIndex : firstBeatIndices) {
            if (firstBeatIndex > index) {
                return firstBeatIndex;
            }
        }
        return getBeatCount();
    }

    /**
     * Returns the measure for the specified beat index.
     * @param beatIndex the beat index
     * @return
     */
    public Measure getMeasureForBeatAt(final int beatIndex) {
        if (measures.size() == 0) {
            return null;
        }
        int currentBeatIndex = 0;
        final Iterator<Measure> itr = measures.iterator();
        Measure currentMeasure = itr.next();
        while (currentBeatIndex + currentMeasure.getBeatCount() <= beatIndex && itr.hasNext()) {
            currentBeatIndex += currentMeasure.getBeatCount();
            currentMeasure = itr.next();
        }
        return currentMeasure;
    }

    public int getBeatsBeforeMeasure(final Measure measure) {
        int beatCount = 0;
        for (Measure thisMeasure : measures) {
            if (thisMeasure.equals(measure)) {
                break;
            }
            beatCount += thisMeasure.getBeatCount();
        }
        return beatCount;
    }

    /**
     * Returns the index of the measure.
     * @param measure
     * @return
     */
    public int indexOf(final Measure measure) {
        return measures.indexOf(measure);
    }

    /**
     * Returns the index of the beat.
     * @param beat
     * @return
     */
    public int indexOf(final Beat beat) {
        return beats.indexOf(beat);
    }

    public int getMeasureNumberInRhythm(final Measure measure) {
        return indexOf(measure) + 1;
    }

    /**
     * Returns the number for this beat in the measure.
     * @param beat the beat.
     * @return
     */
    public int getBeatNumberInMeasure(final Beat beat) {
        final int beatIndex = indexOf(beat);
        final int beatsBeforeMeasure = getBeatsBeforeMeasure(getMeasureForBeatAt(beatIndex));
        Log.d(TAG, "Measures before beat: " +  beatsBeforeMeasure);
        return beatIndex - beatsBeforeMeasure + 1;
    }

    public int getMeasureCount() {
        return measures.size();
    }

    public int getBeatCount() {
        return beats.size();
    }

    public void setBeatAt(final int beatIndex, final Beat beat) {
        beats.set(beatIndex, beat);
    }

    public Beat getBeatAt(final int beatIndex) {
        return beats.get(beatIndex);
    }

    public long getMilliesPerBeat() {
        return MILLIS_PER_BPM / getTempo();
    }

    public long getBeatOffsetMillies(final int index) {
        return index * getMilliesPerBeat();
    }

    /**
     * Gets the offset in ms for the specified subdivision.
     * @param beat the beat to get.
     * @param subdivision the subdivision to get.
     * @return the offset in ms for this subdivision.
     */
    public long getSubdivisionOffsetForBeat(final Beat beat, final int subdivision) {
        return getMilliesPerBeat() / beat.getSubdivisions() * subdivision;
    }

    public void playBeatSubdivisionAt(final int index, final int subdivision, final SoundPoolWrapper soundPool) {
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
