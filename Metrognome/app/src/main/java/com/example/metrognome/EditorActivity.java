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
import com.example.metrognome.time.Measure;
import com.example.metrognome.time.Rhythm;

import static com.example.metrognome.intent.IntentBuilder.KEY_ID;

public class EditorActivity extends AppCompatActivity implements MeasureEditorAlertDialog.DialogListener, BeatEditorAlertDialog.DialogListener {
    private RhythmObjectViewModel mRhythmObjectViewModel;
    private RhythmEntity rhythmEntity;
    private Rhythm rhythm;
    private RecyclerView recyclerView;;
    private Button playButton;
    private Button addMeasureButton;
    private Button saveButton;
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
        RhythmObjectViewModelFactory factory = new RhythmObjectViewModelFactory(this.getApplication(), ID);
        mRhythmObjectViewModel = ViewModelProviders.of(this, factory).get(RhythmObjectViewModel.class);
        rhythmEntity = mRhythmObjectViewModel.getRhythmEntity();
        rhythm = rhythmEntity.getRhythm();

        recyclerView = findViewById(R.id.recycler_view_rhythm);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new BeatAdapter(this, getFragmentManager(), rhythm, true));

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

        saveButton = findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msgFormat = getString(R.string.text_toast_save_format);
                Toast.makeText(v.getContext(), String.format(msgFormat, rhythmEntity.getTitle()), Toast.LENGTH_SHORT).show();
                mRhythmObjectViewModel.getRhythmRepository().update(rhythmEntity);
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
