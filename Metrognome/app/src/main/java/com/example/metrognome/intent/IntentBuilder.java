package com.example.metrognome.intent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class IntentBuilder {
    public static final String KEY_ID = "ID";
    public static final String KEY_WITH_PLAYBACK = "WITH_PLAYBACK";
    private Context context;
    private Class<? extends Activity> targetActivity;
    private Integer id;
    private Boolean withPlayback;

    private IntentBuilder(Context context, Class<? extends Activity> targetActivity) {
        this.context = context;
        this.targetActivity = targetActivity;
    }

    public static IntentBuilder getBuilder(Context context, Class<? extends Activity> targetActivity) {
        return new IntentBuilder(context, targetActivity);
    }

    public IntentBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public IntentBuilder withPlayback(boolean playback) {
        this.withPlayback = playback;
        return this;
    }

    public Intent toIntent() {
        Intent intent = new Intent(context, targetActivity);
        if (id != null) {
            intent.putExtra(KEY_ID, id);
        }
        if (withPlayback != null) {
            intent.putExtra(KEY_WITH_PLAYBACK, withPlayback);
        }
        return intent;
    }

}
