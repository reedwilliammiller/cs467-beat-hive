package com.example.metrognome;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.metrognome.audio.SoundPoolWrapper;
import com.example.metrognome.editor.MeasureAdapter;
import com.example.metrognome.time.Measure;
import com.example.metrognome.time.Rhythm;
import com.example.metrognome.time.RhythmRunnable;

/**
 * An activity for handling playback of a metronome.
 */
public class PlaybackActivity extends AppCompatActivity {
    private TextView titleTextView;
    private NumberPicker numberPicker;
    private ToggleButton playPauseButton;
    private SoundPoolWrapper soundPool;
    private Handler handler;
    private Rhythm rhythm;
    private RhythmRunnable rhythmRunnable;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);
        init();
    }

    private void init() {
        rhythm = Rhythm.RUMBA_CLAVE;

        titleTextView = findViewById(R.id.text_view_title);
        titleTextView.setText(rhythm.getName());

        recyclerView = findViewById(R.id.recycler_view_measure);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new MeasureAdapter(this, rhythm));

        numberPicker = findViewById(R.id.number_picker_tempo);
        numberPicker.setMinValue(Measure.MIN_BPM);
        numberPicker.setMaxValue(Measure.MAX_BPM);
        numberPicker.setValue(rhythm.getTempo());
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                rhythm.setTempo(newVal);
            }
        });

        playPauseButton = findViewById(R.id.button_play_pause);
        playPauseButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startPlayer();
                } else {
                    stopPlayer();
                }
            }
        });

        soundPool = new SoundPoolWrapper(this);
        handler = new Handler();
        rhythmRunnable = new RhythmRunnable(rhythm, handler, soundPool);
    }

    private void startPlayer() {
        handler.post(rhythmRunnable);
    }

    private void stopPlayer() {
        handler.removeCallbacksAndMessages(null);
    }
}