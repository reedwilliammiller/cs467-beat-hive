package com.example.metrognome;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.metrognome.audio.SoundPoolWrapper;
import com.example.metrognome.time.Measure;
import com.example.metrognome.time.MeasureRunnable;

/**
 * An activity for handling playback of a metronome.
 */
public class PlaybackActivity extends AppCompatActivity {
    private EditText editText;
    private Button startStopButton;
    private SoundPoolWrapper soundPool;
    private Handler handler;
    private boolean running;
    private Measure measure = Measure.CLAVE;
    private MeasureRunnable measureRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        editText = findViewById(R.id.bpm_edit_text);
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

        soundPool = new SoundPoolWrapper(this);
        handler = new Handler();
        measureRunnable = new MeasureRunnable(measure, handler, soundPool);
    }

    private void startPlayer() {
        handler.post(measureRunnable);
    }

    private void stopPlayer() {
        handler.removeCallbacksAndMessages(null);
    }

    private boolean setBeatsPerMinute() {
        try {
            int bpm = Integer.parseInt(editText.getText().toString());
            if (bpm >= 1 && bpm <= 600) {
                measure.setTempo(bpm);
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
