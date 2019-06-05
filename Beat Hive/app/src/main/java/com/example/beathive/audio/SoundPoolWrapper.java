package com.example.beathive.audio;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.example.beathive.R;

/**
 * Wrapper class around a {@link SoundPool}.
 */
public class SoundPoolWrapper {
    public static final int INAUDIBLE = -1;
    public static final int DEFAULT_SOUND = 0;
    public static final int NUM_SOUNDS = 4;

    private static final int MAX_SOUND_POOL_STREAMS = 20;
    private static final float DEFAULT_VOLUME = 1f;
    private static final int DEFAULT_PRIORITY = 1;
    private static final int DEFAULT_LOOP = 0;
    private static final float DEFAULT_RATE = 1f;
    private SoundPool soundPool;
    private int[] soundIds = new int[NUM_SOUNDS];

    private static SoundPoolWrapper INSTANCE;

    public static SoundPoolWrapper get(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SoundPoolWrapper(context);
        }
        return INSTANCE;
    }

    private SoundPoolWrapper(Context context) {
        init(context);
    }

    @SuppressWarnings("deprecation")
    private void init(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build();
            soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(MAX_SOUND_POOL_STREAMS).build();
        } else {
            soundPool = new SoundPool(MAX_SOUND_POOL_STREAMS, AudioManager.STREAM_ALARM, 0);
        }
        soundIds[0] = soundPool.load(context, R.raw.clave, DEFAULT_PRIORITY);
        soundIds[1] = soundPool.load(context, R.raw.snare, DEFAULT_PRIORITY);
        soundIds[2] = soundPool.load(context, R.raw.hihat, DEFAULT_PRIORITY);
        soundIds[3] = soundPool.load(context, R.raw.kick, DEFAULT_PRIORITY);
    }

    public int getSoundIdAt(int index) {
        if (index == -1) {
            return INAUDIBLE;
        }
        return soundIds[index];
    }

    public void play(int index) {
        int soundId = getSoundIdAt(index);
        if (soundId != INAUDIBLE) {
            soundPool.play(soundId, DEFAULT_VOLUME, DEFAULT_VOLUME, DEFAULT_PRIORITY, DEFAULT_LOOP, DEFAULT_RATE);
        }
    }
}
