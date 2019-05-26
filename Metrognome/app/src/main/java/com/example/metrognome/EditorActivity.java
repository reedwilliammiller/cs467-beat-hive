package com.example.metrognome;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.metrognome.editor.MeasureAdapter;
import com.example.metrognome.intent.IntentBuilder;
import com.example.metrognome.rhythmDB.RhythmEntity;
import com.example.metrognome.rhythmDB.RhythmObjectViewModel;
import com.example.metrognome.rhythmDB.RhythmObjectViewModelFactory;
import com.example.metrognome.rhythmDB.RhythmViewModel;
import com.example.metrognome.rhythmProcessor.RhythmJSONConverter;
import com.example.metrognome.time.Measure;
import com.example.metrognome.time.Rhythm;

import static com.example.metrognome.intent.IntentBuilder.KEY_ID;
import static com.example.metrognome.intent.IntentBuilder.KEY_RHYTHMSTRING;
import static com.example.metrognome.intent.IntentBuilder.KEY_TITLE;

public class EditorActivity extends AppCompatActivity {
    private RhythmObjectViewModel mRhythmObjectViewModel;
    private RhythmEntity rhythmEntity;
    private Rhythm rhythm;
    private RecyclerView recyclerView;;
    private Button playButton;
    private Button saveButton;
    private Button resetButton;
    private Button addMeasureButton;
    private EditText titleEditText;
    private RhythmViewModel mRhythmViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        final int ID = intent.getIntExtra(KEY_ID, 0);
        String title = intent.getStringExtra(KEY_TITLE);

        if (ID == 0) {
            Rhythm NEW = new Rhythm( 120);
            Measure first = new Measure(NEW, 4);
            NEW.addMeasure(first);
            rhythm = NEW;
        }
        else{
            final String rhythmString = intent.getStringExtra(KEY_RHYTHMSTRING);
            rhythm = RhythmJSONConverter.fromJSON(rhythmString);
        }



        recyclerView = findViewById(R.id.recycler_view_measure);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new MeasureAdapter(this, getFragmentManager(), rhythm, true));

        titleEditText = findViewById(R.id.edit_text_title);


        titleEditText.setText(title);
        playButton = findViewById(R.id.button_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = view.getContext();
                Intent startEditorIntent = IntentBuilder.getBuilder(context, PlaybackActivity.class)
                        .withId(ID)
                        .withTitle(titleEditText.getText().toString())
                        .withRhythm(RhythmJSONConverter.toJSON(rhythm))
                        .withPlayback(true)
                        .toIntent();
                context.startActivity(startEditorIntent);
                finish();
            }
        });

        RhythmObjectViewModelFactory factory = new RhythmObjectViewModelFactory(this.getApplication(), ID);
        mRhythmObjectViewModel = ViewModelProviders.of(this, factory).get(RhythmObjectViewModel.class);


        saveButton = findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RhythmEntity mRhythmEntity;
                if(ID==0){
                    mRhythmEntity = new RhythmEntity(0, titleEditText.getText().toString(), rhythm);
                    mRhythmObjectViewModel.getRhythmRepository().insert(mRhythmEntity);
                }
                else{
                    mRhythmEntity = mRhythmObjectViewModel.getRhythmEntity();
                    mRhythmEntity.setRhythm(rhythm);
                    mRhythmEntity.setTitle(titleEditText.getText().toString());
                    mRhythmObjectViewModel.getRhythmRepository().update(mRhythmEntity);
                }

            }
        });
    }
}
