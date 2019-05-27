package com.example.metrognome;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.DialogFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.metrognome.editor.BeatAdapter;
import com.example.metrognome.editor.BeatEditorAlertDialog;
import com.example.metrognome.editor.MeasureEditorAlertDialog;
import com.example.metrognome.intent.IntentBuilder;
import com.example.metrognome.rhythmDB.RhythmEntity;
import com.example.metrognome.rhythmDB.RhythmObjectViewModel;
import com.example.metrognome.rhythmDB.RhythmObjectViewModelFactory;
import com.example.metrognome.rhythmProcessor.RhythmJSONConverter;
import com.example.metrognome.time.Measure;
import com.example.metrognome.time.Rhythm;

import static com.example.metrognome.intent.IntentBuilder.KEY_ID;
import static com.example.metrognome.intent.IntentBuilder.KEY_RHYTHM_STRING;
import static com.example.metrognome.intent.IntentBuilder.KEY_TITLE;

public class EditorActivity extends AppCompatActivity implements MeasureEditorAlertDialog.DialogListener, BeatEditorAlertDialog.DialogListener {
    private RhythmObjectViewModel mRhythmObjectViewModel;
    private Rhythm rhythm;
    private RecyclerView recyclerView;;
    private Button playButton;
    private Button saveButton;
    private Button addMeasureButton;
    private Button deleteMeasureButton;
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
        recyclerView = findViewById(R.id.recycler_view_rhythm);
        String title = intent.getStringExtra(KEY_TITLE);

        if (ID == 0) {
            Rhythm NEW = new Rhythm( 120);
            Measure first = new Measure(4);
            NEW.addMeasure(first);
            rhythm = NEW;
        }
        else{
            final String rhythmString = intent.getStringExtra(KEY_RHYTHM_STRING);
            rhythm = RhythmJSONConverter.fromJSON(rhythmString);
        }

        recyclerView = findViewById(R.id.recycler_view_rhythm);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new BeatAdapter(this, getFragmentManager(), rhythm, true));

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
                String title = titleEditText.getText().toString();
                if(ID==0){
                    mRhythmEntity = new RhythmEntity(0, titleEditText.getText().toString(), rhythm);
                    mRhythmObjectViewModel.getRhythmRepository().insert(mRhythmEntity);
                }
                else{
                    mRhythmEntity = mRhythmObjectViewModel.getRhythmEntity();
                    mRhythmEntity.setRhythm(rhythm);
                    mRhythmEntity.setTitle(title);
                    mRhythmObjectViewModel.getRhythmRepository().update(mRhythmEntity);
                }
                String msgFormat = getString(R.string.text_toast_save_format);
                Toast.makeText(view.getContext(), String.format(msgFormat, title), Toast.LENGTH_SHORT).show();
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
    public void onDialogAddMeasure(DialogFragment dialogFragment, int measureIndex) {
        rhythm.addMeasureAt(measureIndex, new Measure(4));
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onDialogRemoveMeasure(DialogFragment dialogFragment, int measureIndex) {
        rhythm.removeMeasureAt(measureIndex);
        recyclerView.getAdapter().notifyDataSetChanged();
        if (rhythm.getMeasureCount() == 1) {
            deleteMeasureButton.setEnabled(false);
        }
    }

    @Override
    public void onDialogAddBeat(DialogFragment dialogFragment, int beatIndex) {
        recyclerView.getLayoutManager().findViewByPosition(beatIndex).setSelected(false);
        rhythm.addBeatAt(beatIndex);
        recyclerView.getAdapter().notifyDataSetChanged();
        final View newView = recyclerView.getLayoutManager().findViewByPosition(beatIndex);
        ObjectAnimator animator = ObjectAnimator.ofArgb(newView, "backgroundColor", Color.GREEN, Color.WHITE)
                .setDuration(500);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                newView.setBackground(newView.getResources().getDrawable(R.drawable.background_beat));
            }
        });
        animator.start();
    }

    @Override
    public void onDialogRemoveBeat(DialogFragment dialogFragment, final int beatIndex) {
        final View removeView = recyclerView.getLayoutManager().findViewByPosition(beatIndex);
        removeView.setSelected(false);
        ObjectAnimator animator = ObjectAnimator.ofArgb(removeView, "backgroundColor", Color.RED, Color.WHITE)
                .setDuration(300);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                rhythm.removeBeatAt(beatIndex);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });
        animator.start();
    }

    @Override
    public void onDialogCancel(DialogFragment dialogFragment, int beatIndex) {
        recyclerView.getLayoutManager().findViewByPosition(beatIndex).setSelected(false);
    }
}
