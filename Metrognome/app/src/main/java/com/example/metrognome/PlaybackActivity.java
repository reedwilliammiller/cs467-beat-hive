package com.example.metrognome;

import android.media.MediaPlayer;
import android.os.AsyncTask;
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
    private static final int MAX_MEDIA_PLAYERS = 10;
    private EditText editText;
    private Button startStopButton;
    private MediaPlayer[] mediaPlayers;
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
        super.onDestroy();
        for (MediaPlayer m : mediaPlayers) {
            m.release();
        }
    }

    private void init() {
        editText = findViewById(R.id.bpm_edit_text);
        startStopButton = findViewById(R.id.start_stop_button);
        initStartStopButton();
        mediaPlayers = new MediaPlayer[MAX_MEDIA_PLAYERS];
        for (int i = 0; i < mediaPlayers.length; i++) {
            mediaPlayers[i] = MediaPlayer.create(this, R.raw.clave1);
        }
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
        new MetronomeTask().execute(mediaPlayers);
    }

    private void stopPlayer() {

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

    private class MetronomeTask extends AsyncTask<MediaPlayer, Void, Void> {
        @Override
        protected Void doInBackground(MediaPlayer... mediaPlayers) {
            long millisBetweenBeats = millisBetweenBeats();
            int idx = 0;
            do {
                mediaPlayers[idx].start();
                idx += 1;
                idx %= MAX_MEDIA_PLAYERS;
                SystemClock.sleep(millisBetweenBeats);
            } while (running);
            return null;
        }
    }
}
