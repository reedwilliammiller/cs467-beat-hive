package com.example.metrognome.intent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.TreeMap;

public class IntentBuilder {
    public static final String KEY_ID = "ID";
    public static final String KEY_TITLE = "TITLE";
    public static final String KEY_RHYTHM_STRING = "RHYTHM_STRING";
    public static final String KEY_WITH_PLAYBACK = "WITH_PLAYBACK";
    private WeakReference<Context> context;
    private Class<? extends Activity> targetActivity;
    private Map<String, Object> valueMap = new TreeMap<>();

    private IntentBuilder(Context context, Class<? extends Activity> targetActivity) {
        this.context = new WeakReference<>(context);
        this.targetActivity = targetActivity;
    }

    public static IntentBuilder getBuilder(Context context, Class<? extends Activity> targetActivity) {
        return new IntentBuilder(context, targetActivity);
    }

    public IntentBuilder withId(int id) {
        valueMap.put(KEY_ID, id);
        return this;
    }

    public IntentBuilder withTitle(String title) {
        valueMap.put(KEY_TITLE, title);
        return this;
    }

    public IntentBuilder withRhythm(String rhythmString) {
        valueMap.put(KEY_RHYTHM_STRING, rhythmString);
        return this;
    }

    public IntentBuilder withPlayback(boolean playback) {
        valueMap.put(KEY_WITH_PLAYBACK, playback);
        return this;
    }

    public Intent toIntent() {
        Intent intent = new Intent(context.get(), targetActivity);
        for (Map.Entry<String, Object> entry: valueMap.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Integer) {
                intent.putExtra(entry.getKey(), (Integer) value);
            } else if (value instanceof String) {
                intent.putExtra(entry.getKey(), (String) value);
            } else if (value instanceof Boolean) {
                intent.putExtra(entry.getKey(), (Boolean) value);
            } else if (value instanceof Double) {
                intent.putExtra(entry.getKey(), (Double) value);
            }
        }
        return intent;
    }

}
