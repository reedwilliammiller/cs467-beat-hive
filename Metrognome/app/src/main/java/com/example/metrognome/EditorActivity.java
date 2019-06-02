package com.example.metrognome;


import android.app.AlertDialog;
import android.app.DialogFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.metrognome.editor.BeatAdapter;
import com.example.metrognome.editor.BeatEditorAlertDialog;
import com.example.metrognome.editor.MeasureEditorAlertDialog;
import com.example.metrognome.intent.IntentBuilder;
import com.example.metrognome.rhythmDB.RhythmEntity;
import com.example.metrognome.rhythmDB.RhythmObjectViewModel;
import com.example.metrognome.rhythmDB.RhythmObjectViewModelFactory;
import com.example.metrognome.rhythmDB.RhythmViewModel;
import com.example.metrognome.rhythmProcessor.RhythmJSONConverter;
import com.example.metrognome.time.Measure;
import com.example.metrognome.time.Rhythm;

import static com.example.metrognome.intent.IntentBuilder.KEY_ID;
import static com.example.metrognome.intent.IntentBuilder.KEY_RHYTHM_STRING;
import static com.example.metrognome.intent.IntentBuilder.KEY_TITLE;

public class EditorActivity extends AppCompatActivity implements MeasureEditorAlertDialog.DialogListener, BeatEditorAlertDialog.DialogListener {
    private static final String TAG = EditorActivity.class.getSimpleName();
    private RhythmObjectViewModel mRhythmObjectViewModel;
    private Rhythm rhythm;
    private RecyclerView recyclerView;;
    private Button playButton;
    private Button saveButton;
    private Button saveAsButton;
    private TextView titleTextView;
    private RhythmViewModel mRhythmViewModel;
    private Button addMeasureButton;
    private Button deleteMeasureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        final int ID = intent.getIntExtra(KEY_ID, 0);
        recyclerView = findViewById(R.id.recycler_view_rhythm);
        final String title = intent.getStringExtra(KEY_TITLE);

        if (ID == 0) {
            Rhythm NEW = new Rhythm(120);
            Measure first = new Measure(4);
            NEW.addMeasure(first);
            rhythm = NEW;
        } else {
            final String rhythmString = intent.getStringExtra(KEY_RHYTHM_STRING);
            rhythm = RhythmJSONConverter.fromJSON(rhythmString);
        }


        recyclerView = findViewById(R.id.recycler_view_rhythm);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new BeatAdapter(this, getFragmentManager(), rhythm, true));

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
        if (ID == 0) {
            saveButton.setEnabled(false);
        }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RhythmEntity mRhythmEntity;
                mRhythmEntity = mRhythmObjectViewModel.getRhythmEntity();
                mRhythmEntity.setRhythm(rhythm);
                mRhythmEntity.setTitle(title);
                mRhythmObjectViewModel.getRhythmRepository().update(mRhythmEntity);
                String msgFormat = getString(R.string.text_toast_save_format);
                Toast.makeText(view.getContext(), String.format(msgFormat, title), Toast.LENGTH_SHORT).show();
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
                                    public void onClick(DialogInterface dialog, int id) {
                                        RhythmEntity mRhythmEntity;
                                        mRhythmEntity = new RhythmEntity(0, userInput.getText().toString(), rhythm);
                                        int new_id = (int) mRhythmObjectViewModel.getRhythmRepository().insert(mRhythmEntity);
                                        final Context context = view.getContext();
                                        Intent intent = IntentBuilder.getBuilder(context, EditorActivity.class)
                                                .withId(new_id)
                                                .withTitle(userInput.getText().toString())
                                                .withRhythm(RhythmJSONConverter.toJSON(rhythm))
                                                .toIntent();
                                        context.startActivity(intent);
                                        String msgFormat = getString(R.string.text_toast_save_format);
                                        Toast.makeText(view.getContext(), String.format(msgFormat, userInput.getText().toString()), Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show alert dialog
                alertDialog.show();
            }
        });

        addMeasureButton = findViewById(R.id.button_add_measure);
        addMeasureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMeasureDialogFragment(true);
            }
        });

        deleteMeasureButton = findViewById(R.id.button_delete_measure);
        deleteMeasureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMeasureDialogFragment(false);
            }
        });
        if (rhythm.getMeasureCount() == 1) {
            deleteMeasureButton.setEnabled(false);
        }
    }
    
    public void showMeasureDialogFragment(boolean isAddMeasure) {
        DialogFragment dialogFragment = new MeasureEditorAlertDialog();
        Bundle args = new Bundle();
        args.putInt(MeasureEditorAlertDialog.KEY_MEASURE_COUNT, rhythm.getMeasureCount());
        args.putBoolean(MeasureEditorAlertDialog.KEY_IS_ADD_MEASURE, isAddMeasure);
        dialogFragment.setArguments(args);
        dialogFragment.show(getFragmentManager(), "measure_dialog");
    }

    @Override
    public void onDialogAddMeasure(DialogFragment dialogFragment, int measureOrdinal) {
        Measure toAdd = new Measure(4);
        rhythm.addMeasureAt(measureOrdinal - 1, toAdd);
        int beatIndex = rhythm.getBeatsBeforeMeasure(toAdd);
        recyclerView.getAdapter().notifyItemRangeInserted(beatIndex, 4);
        for (int idx : rhythm.getIndicesForFirstBeatsOfEachMeasure()) {
            recyclerView.getAdapter().notifyItemChanged(idx);
        }
        recyclerView.smoothScrollToPosition(beatIndex);
        deleteMeasureButton.setEnabled(true);
    }

    @Override
    public void onDialogRemoveMeasure(DialogFragment dialogFragment, int measureOrdinal) {
        Measure toRemove = rhythm.getMeasureAt(measureOrdinal - 1);
        int beatIndex = rhythm.getBeatsBeforeMeasure(toRemove);
        rhythm.removeMeasureAt(measureOrdinal - 1);
        recyclerView.getAdapter().notifyItemRangeRemoved(beatIndex, toRemove.getBeatCount());
        for (int idx : rhythm.getIndicesForFirstBeatsOfEachMeasure()) {
            recyclerView.getAdapter().notifyItemChanged(idx);
        }
        if (rhythm.getMeasureCount() == 1) {
            deleteMeasureButton.setEnabled(false);
        }
    }

    @Override
    public void onDialogAddBeat(DialogFragment dialogFragment, final int beatIndex) {
        Log.d(TAG, "Adding beat at: " + beatIndex);
        final RecyclerView.ViewHolder oldViewHolder = recyclerView.findViewHolderForAdapterPosition(beatIndex);
        final View oldView = oldViewHolder.itemView;
        oldView.setSelected(false);
        rhythm.addBeatAt(beatIndex);
        recyclerView.getAdapter().notifyItemInserted(beatIndex);
        recyclerView.getAdapter().notifyItemRangeChanged(beatIndex, rhythm.getIndexOfFirstBeatOfNextMeasure(beatIndex) + 1);
    }

    @Override
    public void onDialogRemoveBeat(DialogFragment dialogFragment, final int beatIndex) {
        Log.d(TAG, "Removing beat at: " + beatIndex);
        final RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(beatIndex);
        final View view = viewHolder.itemView;
        if (view != null) {
            view.setSelected(false);
        }
        rhythm.removeBeatAt(beatIndex);
        recyclerView.getAdapter().notifyItemRemoved(beatIndex);
        recyclerView.getAdapter().notifyItemRangeChanged(beatIndex - 1, rhythm.getIndexOfFirstBeatOfNextMeasure(beatIndex) + 1);
    }

    @Override
    public void onDialogCancel(DialogFragment dialogFragment, int beatIndex) {
        final RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(beatIndex);
        final View view = viewHolder.itemView;
        if (view != null) {
            view.setSelected(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
