package com.example.metrognome.rhythmProcessor;

import android.util.Log;

import com.example.metrognome.time.Beat;
import com.example.metrognome.time.Measure;
import com.example.metrognome.time.Rhythm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Format of JSON:
 *
 * RHYTHM:
 * {
 *     "tempo": INTEGER,
 *     "measures": ARRAY of MEASURE
 *     "beats": ARRAY of BEAT
 * }
 *
 * MEASURE:
 * {
 *     "beat_count": INTEGER
 * }
 *
 * BEAT:
 * {
 *     "sound_ids": ARRAY of INTEGER
 * }
 */
public class RhythmJSONConverter {
    private static final String TAG = RhythmJSONConverter.class.getSimpleName();

    private static final String KEY_TEMPO = "tempo";
    private static final String KEY_MEASURES = "measures";
    private static final String KEY_BEATS = "beats";
    private static final String KEY_BEAT_COUNT = "beat_count";
    private static final String KEY_SOUND_IDS = "sound_ids";

    public static String toJSON(Rhythm rhythm){
        try {
            return rhythmToJSON(rhythm).toString();
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing Rhythm.", e);
            return null;
        }
    }

    public static Rhythm fromJSON(String string){
        try {
            return jsonToRhythm(new JSONObject(string));
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON.", e);
            return null;
        }
    }

    /**
     * Converts a Rhythm into a JSONObject.
     * @param rhythm
     * @return the
     * @throws JSONException
     */
    private static JSONObject rhythmToJSON(Rhythm rhythm) throws JSONException {
        Log.d(TAG, rhythm.toString());
        JSONObject object = new JSONObject();
        object.put(KEY_TEMPO, rhythm.getTempo());
        JSONArray beatArray = new JSONArray();
        for (int i = 0; i < rhythm.getBeatCount(); i++) {
            Beat beat = rhythm.getBeatAt(i);
            beatArray.put(beatToJSON(beat));
        }
        object.put(KEY_BEATS, beatArray);
        JSONArray measureArray = new JSONArray();
        for (Measure measure : rhythm.getMeasures()) {
            measureArray.put(measureToJSON(measure));
        }
        object.put(KEY_MEASURES, measureArray);
        return object;
    }

    private static Rhythm jsonToRhythm(JSONObject object) throws JSONException {
        Log.d(TAG, object.toString());
        Rhythm rhythm = new Rhythm();
        rhythm.setTempo(object.getInt(KEY_TEMPO));
        JSONArray measuresArray = object.getJSONArray(KEY_MEASURES);
        for (int i = 0; i < measuresArray.length(); i++) {
            JSONObject measureObject = measuresArray.getJSONObject(i);
            rhythm.addMeasure(jsonToMeasure(measureObject));
        }
        JSONArray beatArray = object.getJSONArray(KEY_BEATS);
        for (int i = 0; i < beatArray.length(); i++) {
            JSONObject beatObject = beatArray.getJSONObject(i);
            rhythm.setBeatAt(i, jsonToBeat(beatObject));
        }
        return rhythm;
    }

    private static JSONObject measureToJSON(Measure measure) throws JSONException {
        Log.d(TAG, measure.toString());
        JSONObject object = new JSONObject();
        object.put(KEY_BEAT_COUNT,measure.getBeatCount());
        return object;
    }

    private static Measure jsonToMeasure(JSONObject object) throws JSONException {
        Log.d(TAG, object.toString());
        return new Measure(object.getInt(KEY_BEAT_COUNT));
    }

    private static JSONObject beatToJSON(Beat beat) throws JSONException {
        Log.d(TAG, beat.toString());
        JSONObject object = new JSONObject();
        JSONArray soundsArray = new JSONArray();
        for (int i = 0; i < beat.getSubdivisions(); i++) {
            soundsArray.put(beat.getSoundAt(i));
        }
        object.put(KEY_SOUND_IDS, soundsArray);
        return object;
    }

    private static Beat jsonToBeat(JSONObject object) throws JSONException {
        Log.d(TAG, object.toString());
        Beat beat = new Beat();
        JSONArray soundsArray = object.getJSONArray(KEY_SOUND_IDS);
        beat.subdivideBy(soundsArray.length());
        for (int i = 0; i < soundsArray.length(); i++) {
            beat.setSoundAt(i, soundsArray.getInt(i));
        }
        return beat;
    }
}
