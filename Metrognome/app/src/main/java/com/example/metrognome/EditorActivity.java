package com.example.metrognome;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    private Button saveAsButton;
    private Button resetButton;
    private Button addMeasureButton;
    private TextView titleTextView;
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

        titleTextView = findViewById(R.id.text_view_title);


        titleTextView.setText(title);
        playButton = findViewById(R.id.button_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = view.getContext();
                Intent startEditorIntent = IntentBuilder.getBuilder(context, PlaybackActivity.class)
                        .withId(ID)
                        .withTitle(titleTextView.getText().toString())
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
        if(ID == 0){
            saveButton.setEnabled(false);
        }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RhythmEntity mRhythmEntity;
                mRhythmEntity = mRhythmObjectViewModel.getRhythmEntity();
                mRhythmEntity.setRhythm(rhythm);
                mRhythmEntity.setTitle(titleTextView.getText().toString());
                mRhythmObjectViewModel.getRhythmRepository().update(mRhythmEntity);
            }
        });

        saveAsButton = findViewById(R.id.button_save_as);
        saveAsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(view.getContext());
                View promptsView = li.inflate(R.layout.save_as_dialog, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        view.getContext());

                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.edit_title_save_as);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        RhythmEntity mRhythmEntity;
                                        mRhythmEntity = new RhythmEntity(0, titleTextView.getText().toString(), rhythm);
                                        int new_id = (int) mRhythmObjectViewModel.getRhythmRepository().insert(mRhythmEntity);
                                        final Context context = view.getContext();
                                        Intent intent = IntentBuilder.getBuilder(context, EditorActivity.class)
                                                .withId(new_id)
                                                .withTitle(userInput.getText().toString())
                                                .withRhythm(RhythmJSONConverter.toJSON(rhythm))
                                                .toIntent();
                                        context.startActivity(intent);
                                        finish();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
