package com.example.metrognome.time;

import android.util.Log;

import com.example.metrognome.audio.SoundPoolWrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents an entire rhythm which is a sequence of measures.
 */
public class Rhythm implements Iterable<Measure> {
    private static final String TAG = Rhythm.class.getSimpleName();
    private List<Measure> measures = new ArrayList<>();
    private String name;
    private int tempo;

    public static Rhythm CLAVE;
    static {
        CLAVE = new Rhythm("Rumba Clave", 120);

        Measure first = new Measure(TimeSignature.COMMON_TIME, CLAVE.tempo);
        first.getBeatAt(0).setSoundAt(0, SoundPoolWrapper.INAUDIBLE);
        first.getBeatAt(3).setSoundAt(0, SoundPoolWrapper.INAUDIBLE);

        Measure second = new Measure(TimeSignature.COMMON_TIME, CLAVE.tempo);
        second.getBeatAt(1).subdivideBy(2);
        second.getBeatAt(1).setSoundAt(0, SoundPoolWrapper.INAUDIBLE);
        second.getBeatAt(1).setSoundAt(1, SoundPoolWrapper.DEFAULT_SOUND);
        second.getBeatAt(2).setSoundAt(0, SoundPoolWrapper.INAUDIBLE);
        second.getBeatAt(3).subdivideBy(2);
        second.getBeatAt(3).setSoundAt(0, SoundPoolWrapper.INAUDIBLE);
        second.getBeatAt(3).setSoundAt(1, SoundPoolWrapper.DEFAULT_SOUND);

        CLAVE.addMeasure(first);
        CLAVE.addMeasure(second);
    }

    public Rhythm(String name, int tempo) {
        this.name = name;
        this.tempo = tempo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTempo(int tempo) {
        for (Measure measure : measures) {
            measure.setTempo(tempo);
        }
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
