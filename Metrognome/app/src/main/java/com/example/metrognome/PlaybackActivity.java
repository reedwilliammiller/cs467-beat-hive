package com.example.metrognome;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * An activity for handling playback of a metronome.
 */
public class PlaybackActivity extends AppCompatActivity {
    private static final float VOLUME = 1f;
    private static final int MAX_SOUND_POOL_STREAMS = 20;
    private static final long MILLIS_PER_BPM = 60000;
    private EditText editText;
    private Button startStopButton;
    private SoundPool soundPool;
    private int claveId;
    private Handler handler;
    private MetronomeRunnable metronomeRunnable;
    private int beatsPerMinute;
    private boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);
        init();
    }

    @Override
    protected void onDestroy() {
        soundPool.release();
        soundPool = null;
        super.onDestroy();
    }

    private void init() {
        initEditText();
        initStartStopButton();
        initSoundPool();
        initHandler();
    }

    private void initEditText() {
        editText = findViewById(R.id.bpm_edit_text);
    }

    private void initStartStopButton() {
        startStopButton = findViewById(R.id.start_stop_button);
        startStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = setBeatsPerMinute();
                if (valid) {
                    if (running) {
                        stopPlayer();
                        startStopButton.setText(R.string.start);
                    } else {
                        startPlayer();
                        startStopButton.setText(R.string.stop);
                    }
                    running = !running;
                }
            }
        });
    }
    
    private void initSoundPool() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build();
            soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(MAX_SOUND_POOL_STREAMS).build();
        } else {
            soundPool = new SoundPool(MAX_SOUND_POOL_STREAMS, AudioManager.STREAM_ALARM, 0);
        }
        claveId = soundPool.load(this, R.raw.clave1, 1);
    }

    private void initHandler() {
        handler = new Handler();
        metronomeRunnable = new MetronomeRunnable(handler, soundPool, claveId);
    }

    private void startPlayer() {
        metronomeRunnable.setInterval(millisBetweenBeats());
        handler.post(metronomeRunnable);
    }

    private void stopPlayer() {
        handler.removeCallbacksAndMessages(null);
    }

    private long millisBetweenBeats() {
        return MILLIS_PER_BPM / beatsPerMinute;
    }

    private boolean setBeatsPerMinute() {
        try {
            int bpm = Integer.parseInt(editText.getText().toString());
            if (bpm >= 1 && bpm <= 600) {
                beatsPerMinute = bpm;
                return true;
            } else {
                Toast.makeText(this, "Invalid BPM: Integer must be between 1 and 600.", Toast.LENGTH_SHORT).show();

            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid BPM: Not an integer.", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /**
     * A runnable object that plays a sound at a specified interval. It invokes the handler and posts
     * itself to run at the interval.
     *
     * It is expected to set the interval prior to calling run.
     */
    public class MetronomeRunnable implements Runnable {
        private Handler handler;
        private SoundPool soundPool;
        private int soundId;
        private long intervalInMillis = Long.MAX_VALUE;

        /**
         * Creates a new MetronomeRunnable
         * @param handler the handler to post itself to,
         * @param soundPool the SoundPool object to play,
         * @param soundId the id of the audio to play.
         */
        public MetronomeRunnable(Handler handler, SoundPool soundPool, int soundId) {
            this.handler = handler;
            this.soundPool = soundPool;
            this.soundId = soundId;
        }

        /**
         * Sets the interval to repeat this sound at.
         * @param interval the interval in milliseconds.
         */
        public void setInterval(long interval) {
            intervalInMillis = interval;
        }

        @Override
        public void run() {
            soundPool.play(soundId, VOLUME, VOLUME, 0, 0, 1f);
            handler.postDelayed(this, intervalInMillis);
        }
    }
}
