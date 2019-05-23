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
import com.example.metrognome.time.Rhythm;

import static com.example.metrognome.intent.IntentBuilder.KEY_ID;

public class EditorActivity extends AppCompatActivity {
    private RhythmObjectViewModel mRhythmObjectViewModel;
    private RhythmEntity rhythmEntity;
    private Rhythm rhythm;
    private RecyclerView recyclerView;;
    private Button playButton;
    private Button resetButton;
    private Button addMeasureButton;
    private EditText titleEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        final int ID = intent.getIntExtra(KEY_ID, 0);
        RhythmObjectViewModelFactory factory = new RhythmObjectViewModelFactory(this.getApplication(), ID);
        mRhythmObjectViewModel = ViewModelProviders.of(this, factory).get(RhythmObjectViewModel.class);
        rhythmEntity = mRhythmObjectViewModel.getRhythmEntity();
        rhythm = rhythmEntity.getRhythm();

        recyclerView = findViewById(R.id.recycler_view_measure);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new MeasureAdapter(this, getFragmentManager(), rhythm, true));

        titleEditText = findViewById(R.id.edit_text_title);

        if (ID != 0) {
            titleEditText.setText(rhythmEntity.getTitle());
        }

        playButton = findViewById(R.id.button_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rhythmEntity.setTitle(titleEditText.getText().toString());
                mRhythmObjectViewModel.getRhythmRepository().update(rhythmEntity);
                final Context context = view.getContext();
                Intent startEditorIntent = IntentBuilder.getBuilder(context, PlaybackActivity.class)
                        .withId(ID)
                        .withPlayback(true)
                        .toIntent();
                context.startActivity(startEditorIntent);
                finish();
            }
        });
    }
}
