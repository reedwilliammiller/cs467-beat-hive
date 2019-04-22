package com.example.metrognome;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlaybackActivity extends AppCompatActivity {
    private static final float VOLUME = 1f;
    private EditText editText;
    private Button startStopButton;
    private SoundPool soundPool;
    private int claveId;
    private Handler handler;
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
        editText = findViewById(R.id.bpm_edit_text);
        startStopButton = findViewById(R.id.start_stop_button);
        initStartStopButton();
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                soundPool.play(1, VOLUME, VOLUME, 0, 0, 1f);
                return true;
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(20).build();
        } else {
            soundPool = new SoundPool(20, AudioManager.STREAM_ALARM, 0);
        }
        claveId = soundPool.load(this, R.raw.clave1, 1);
    }

    private void initStartStopButton() {
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

    private void startPlayer() {
        long now = SystemClock.uptimeMillis();
        long millisBetweenBeats = millisBetweenBeats();
        for (int i = 0; i < 1000; i++) {
            handler.sendMessageAtTime(handler.obtainMessage(), now + i * millisBetweenBeats);
        }
    }

    private void stopPlayer() {
        handler.removeCallbacksAndMessages(null);
    }

    private long millisBetweenBeats() {
        return 60 * 1000 / beatsPerMinute;
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
}
