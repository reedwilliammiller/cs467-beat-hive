package com.example.metrognome.rhythmProcessor;

import android.util.Log;

import com.example.metrognome.time.Beat;
import com.example.metrognome.time.Measure;
import com.example.metrognome.time.Rhythm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Format of JSON:
 *
 * RHYTHM:
 * {
 *     "tempo": INTEGER,
 *     "measures": ARRAY of MEASURE
 * }
 *
 * MEASURE:
 * {
 *     "beats": ARRAY of BEAT
 * }
 *
 * BEAT:
 * {
 *     "sound_ids": ARRAY of INTEGER
 * }
 */
public class RhythmJSONConverter {
    private static final String TAG = RhythmJSONConverter.class.getSimpleName();

    private static final String KEY_SOUND_IDS = "sound_ids";
    private static final String KEY_BEATS = "beats";
    private static final String KEY_TEMPO = "tempo";
    private static final String KEY_MEASURES = "measures";

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
        JSONArray measuresArray = new JSONArray();
        for (Measure measure : rhythm) {
            measuresArray.put(measureToJSON(measure));
        }
        object.put(KEY_MEASURES, measuresArray);
        return object;
    }

    private static Rhythm jsonToRhythm(JSONObject object) throws JSONException {
        Log.d(TAG, object.toString());
        Rhythm rhythm = new Rhythm();
        rhythm.setTempo(object.getInt(KEY_TEMPO));
        JSONArray measuresArray = object.getJSONArray(KEY_MEASURES);
        for (int i = 0; i < measuresArray.length(); i++) {
            JSONObject measureObject = measuresArray.getJSONObject(i);
            rhythm.addMeasure(jsonToMeasure(measureObject, rhythm));
        }
        return rhythm;
    }

    private static JSONObject measureToJSON(Measure measure) throws JSONException {
        Log.d(TAG, measure.toString());
        JSONObject object = new JSONObject();
        JSONArray beatsArray = new JSONArray();
        for (Beat beat : measure) {
            beatsArray.put(beatToJSON(beat));
        }
        object.put(KEY_BEATS, beatsArray);
        return object;
    }

    private static Measure jsonToMeasure(JSONObject object, Rhythm rhythm) throws JSONException {
        Log.d(TAG, object.toString());
        Measure measure = new Measure(rhythm);
        JSONArray beatsArray = object.getJSONArray(KEY_BEATS);
        for (int i = 0; i < beatsArray.length(); i++) {
            JSONObject beatObject = beatsArray.getJSONObject(i);
            measure.addBeat(jsonToBeat(beatObject, measure));
        }
        return measure;
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

    private static Beat jsonToBeat(JSONObject object, Measure measure) throws JSONException {
        Log.d(TAG, object.toString());
        Beat beat = new Beat(measure);
        JSONArray soundsArray = object.getJSONArray(KEY_SOUND_IDS);
        beat.subdivideBy(soundsArray.length());
        for (int i = 0; i < soundsArray.length(); i++) {
            beat.setSoundAt(i, soundsArray.getInt(i));
        }
        return beat;
    }
}
