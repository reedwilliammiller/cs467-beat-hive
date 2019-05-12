package com.example.metrognome.rhythmProcessor;

import com.example.metrognome.audio.SoundPoolWrapper;
import com.example.metrognome.time.Measure;
import com.example.metrognome.time.Rhythm;
import com.example.metrognome.time.TimeSignature;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RhythmJSONConverter {

    public static String toJSON(Rhythm rhythmInput){
        JsonObject rhythm = new JsonObject();

        rhythm.addProperty("name", rhythmInput.getName());
        rhythm.addProperty("tempo", rhythmInput.getTempo());
        JsonArray measures = new JsonArray();
        for (int k = 0; k < rhythmInput.getMeasureCount(); k++) {
            JsonObject measure = new JsonObject();
            measure.addProperty("tempo", rhythmInput.getMeasureAt(k).getTempo());
            measure.addProperty("index", k);

            JsonObject timesignature = new JsonObject();
            timesignature.addProperty("beats", rhythmInput.getMeasureAt(k).getTimeSignature().getBeats());
            timesignature.addProperty("note", rhythmInput.getMeasureAt(k).getTimeSignature().getNote());
            measure.add("timesignature", timesignature);

            JsonArray beats = new JsonArray();
            for (int j = 0; j < rhythmInput.getMeasureAt(k).getBeatCount(); j++) {
                JsonObject beat = new JsonObject();
                beat.addProperty("index", j);
                JsonArray soundIds = new JsonArray();
                for (int i = 0; i < rhythmInput.getMeasureAt(k).getBeatAt(j).getSubdivisions(); i++) {
                    soundIds.add(rhythmInput.getMeasureAt(k).getBeatAt(j).getSoundAt(i));
                }
                beat.add("soundIds", soundIds);
                beats.add(beat);
            }
            measure.add("beats", beats);
            measures.add(measure);
        }
        rhythm.addProperty("total_measures", rhythmInput.getMeasureCount());
        rhythm.add("measures", measures);
        return rhythm.toString();
    }

    public static Rhythm fromJSON(String string){
        JsonParser jsonParser = new JsonParser();
        JsonObject rhythmJSON = jsonParser.parse(string).getAsJsonObject();
        Rhythm rhythm = new Rhythm(rhythmJSON.get("name").getAsString(), rhythmJSON.get("tempo").getAsInt());
        Measure first = new Measure(rhythm, 0, TimeSignature.COMMON_TIME, rhythm.tempo);
        first.getBeatAt(0).setSoundAt(0, SoundPoolWrapper.INAUDIBLE);
        first.getBeatAt(3).setSoundAt(0, SoundPoolWrapper.INAUDIBLE);

        return rhythm;
    }

}
