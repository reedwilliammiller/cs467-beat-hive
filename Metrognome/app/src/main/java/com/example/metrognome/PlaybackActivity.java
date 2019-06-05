package com.example.metrognome;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.metrognome.animation.ScrollingLayoutManager;
import com.example.metrognome.audio.SoundPoolWrapper;
import com.example.metrognome.editor.BeatAdapter;
import com.example.metrognome.intent.IntentBuilder;
import com.example.metrognome.rhythmDB.RhythmObjectViewModel;
import com.example.metrognome.rhythmDB.RhythmObjectViewModelFactory;
import com.example.metrognome.rhythmProcessor.RhythmJSONConverter;
import com.example.metrognome.time.Rhythm;
import com.example.metrognome.time.RhythmRunnable;
import com.example.metrognome.animation.TouchlessRecyclerView;

import static com.example.metrognome.intent.IntentBuilder.KEY_ID;
import static com.example.metrognome.intent.IntentBuilder.KEY_RHYTHM_STRING;
import static com.example.metrognome.intent.IntentBuilder.KEY_TITLE;
import static com.example.metrognome.intent.IntentBuilder.KEY_WITH_PLAYBACK;

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
    private TouchlessRecyclerView recyclerView;
    private ScrollingLayoutManager scroller;
    private RhythmObjectViewModel mRhythmObjectViewModel;
    private Button editButton;
    private boolean playing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        final int ID = intent.getIntExtra(KEY_ID, 0);
        final String title = intent.getStringExtra(KEY_TITLE);
        final String rhythmString = intent.getStringExtra(KEY_RHYTHM_STRING);
        final boolean withPlayback = intent.getBooleanExtra(KEY_WITH_PLAYBACK, false);

        // If Rhythm exists in database, update last opened time
        if(ID != 0){
            RhythmObjectViewModelFactory factory = new RhythmObjectViewModelFactory(this.getApplication(), ID);
            mRhythmObjectViewModel = ViewModelProviders.of(this, factory).get(RhythmObjectViewModel.class);
            mRhythmObjectViewModel.setLastOpened();
        }

        //Convert Rhythm from json string passed through intent
        rhythm = RhythmJSONConverter.fromJSON(rhythmString);


        titleTextView = findViewById(R.id.text_view_title);
        titleTextView.setText(title);
        playing = false;

        recyclerView = findViewById(R.id.recycler_view_playable_measure);

        recyclerView.setAdapter(new BeatAdapter(this, getFragmentManager(), rhythm, false));
        scroller = new ScrollingLayoutManager(this);
        recyclerView.setLayoutManager(scroller);

        numberPicker = findViewById(R.id.number_picker_tempo);
        numberPicker.setMinValue(Rhythm.MIN_BPM);
        numberPicker.setMaxValue(Rhythm.MAX_BPM);
        numberPicker.setValue(rhythm.getTempo());
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                rhythm.setTempo(newVal);
                if (playing == true)
                    recyclerView.smoothScrollToPosition(Integer.MAX_VALUE);
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

        editButton = findViewById(R.id.button_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = view.getContext();
                Intent intent = IntentBuilder.getBuilder(context, EditorActivity.class)
                        .withId(ID)
                        .withTitle(title)
                        .withRhythm(rhythmString)
                        .newEdited(true)
                        .toIntent();
                context.startActivity(intent);
                finish();
            }
        });

        soundPool = SoundPoolWrapper.get(this);
        handler = new Handler();
        rhythmRunnable = new RhythmRunnable(rhythm, recyclerView, handler, soundPool);

        if (withPlayback) {
            playPauseButton.setChecked(true);
        }
    }

    private void startPlayer() {
        playing = true;
        handler.post(rhythmRunnable);
        recyclerView.smoothScrollToPosition(Integer.MAX_VALUE);
    }

    private void stopPlayer() {
        playing = false;
        handler.removeCallbacksAndMessages(null);
        recyclerView.scrollToPosition(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
        stopPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlayer();
    }
}