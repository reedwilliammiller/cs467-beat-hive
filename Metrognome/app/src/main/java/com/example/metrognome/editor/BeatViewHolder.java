package com.example.metrognome.editor;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.metrognome.R;
import com.example.metrognome.time.Beat;
import com.example.metrognome.time.Measure;
import com.example.metrognome.time.Rhythm;

public class BeatViewHolder extends RecyclerView.ViewHolder {
    public Rhythm rhythm;
    public Measure measure;
    public Beat beat;
    public boolean isEditable;

    private View view;
    private TextView measureTextView;
    private TextView beatCountTextView;
    private View measureView;

    public BeatViewHolder(View v) {
        super(v);
        view = v;
    }

    public void init() {
        boolean isFirstBeat = rhythm.getBeatNumberInMeasure(beat) == 1;

        measureView = view.findViewById(R.id.measure_label);
        if (!isFirstBeat) {
            measureView.setVisibility(View.GONE);
        } else {
            measureView.setVisibility(View.VISIBLE);
            measureTextView = view.findViewById(R.id.text_view_measure);
            beatCountTextView = view.findViewById(R.id.text_view_measure_beat_count);
            measureTextView.setText(String.format("%d/%d", rhythm.getMeasureNumberInRhythm(measure), rhythm.getMeasureCount()));
            beatCountTextView.setText(String.format("%d", measure.getBeatCount()));
        }

        updateVisibility();
        setupNote(R.id.note_1, 0);
        setupNote(R.id.note_2, 1);
        setupNote(R.id.note_3, 2);
        setupNote(R.id.note_4, 3);
        if (isEditable) {
            setupSubdivisionButtons();
        } else {
            ImageView addSubdivision = view.findViewById(R.id.button_add_subdivision);
            ImageView removeSubdivision = view.findViewById(R.id.button_remove_subdivision);
            addSubdivision.setVisibility(View.GONE);
            removeSubdivision.setVisibility(View.GONE);
        }

        if (measure.getBeatCount() == rhythm.getBeatNumberInMeasure(beat)) {
            view.findViewById(R.id.beat_divider).setVisibility(View.GONE);
            view.findViewById(R.id.measure_divider).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.beat_divider).setVisibility(View.VISIBLE);
            view.findViewById(R.id.measure_divider).setVisibility(View.GONE);
        }
    }

    private void setupNote(final int noteId, final int subdivisionIndex) {
        View noteView = view.findViewById(noteId);
        final NoteImageButton noteButton = noteView.findViewById(R.id.button_note);
        updateNoteText(noteId, subdivisionIndex);
        if (subdivisionIndex < beat.getSubdivisions() ) {
            noteButton.setState(beat.getSoundAt(subdivisionIndex));
        }
        if (isEditable) {
            noteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noteButton.nextState();
                    beat.setSoundAt(subdivisionIndex, noteButton.getState());
                }
            });

        } else {
            noteButton.setEnabled(false);
        }
    }

    private void updateNoteText(final int noteId, final int subdivisionIndex) {
        View noteView = view.findViewById(noteId);
        TextView noteText = noteView.findViewById(R.id.text_view_note);
        if (noteText != null) {
            String text;
            Resources resources = view.getResources();
            if (subdivisionIndex == 0) {
                text = Integer.toString(rhythm.getBeatNumberInMeasure(beat));
            } else if (subdivisionIndex == 1) {
                if (beat.getSubdivisions() == 4) {
                    text = resources.getString(R.string.e_mnemonic);
                } else {
                    text = resources.getString(R.string.and_mnemonic);
                }
            } else if (subdivisionIndex == 2) {
                if (beat.getSubdivisions() == 4) {
                    text = resources.getString(R.string.and_mnemonic);
                } else {
                    text = resources.getString(R.string.a_mnemonic);
                }
            } else {
                text = resources.getString(R.string.a_mnemonic);
            }
            noteText.setText(text);
        }
    }

    private void updateVisibility() {
        int subdivisionCount = beat.getSubdivisions();
        if (subdivisionCount < 2) {
            view.findViewById(R.id.button_remove_subdivision).setVisibility(View.INVISIBLE);
            hideNoteView(view.findViewById(R.id.note_2));
        } else {
            view.findViewById(R.id.button_remove_subdivision).setVisibility(View.VISIBLE);
            view.findViewById(R.id.note_2).setVisibility(View.VISIBLE);
            updateNoteText(R.id.note_2, 1);
        }
        if (subdivisionCount < 3) {
            hideNoteView(view.findViewById(R.id.note_3));
        } else {
            view.findViewById(R.id.note_3).setVisibility(View.VISIBLE);
            updateNoteText(R.id.note_3, 2);
        }
        if (subdivisionCount < 4) {
            view.findViewById(R.id.button_add_subdivision).setVisibility(View.VISIBLE);
            hideNoteView(view.findViewById(R.id.note_4));
        } else {
            view.findViewById(R.id.button_add_subdivision).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.note_4).setVisibility(View.VISIBLE);
            updateNoteText(R.id.note_4, 3);
        }
    }

    private void setupSubdivisionButtons() {
        ImageView addSubdivision = view.findViewById(R.id.button_add_subdivision);
        ImageView removeSubdivision = view.findViewById(R.id.button_remove_subdivision);

        addSubdivision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beat.addSubdivision();
                updateVisibility();
            }
        });

        removeSubdivision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View noteView;
                int subdivision = beat.getSubdivisions();
                switch (subdivision) {
                    case 4:
                        noteView = view.findViewById(R.id.note_4);
                        break;
                    case 3:
                        noteView = view.findViewById(R.id.note_3);
                        break;
                    case 2:
                        noteView = view.findViewById(R.id.note_2);
                        break;
                    default:
                        noteView = view.findViewById(R.id.note_1);
                }
                NoteImageButton noteButton = noteView.findViewById(R.id.button_note);
                noteButton.setState(-1);
                beat.removeSubdivision();
                updateVisibility();
            }
        });
    }

    private void hideNoteView(View noteView) {
        NoteImageButton noteButton = noteView.findViewById(R.id.button_note);
        noteButton.setState(-1);
        noteView.setVisibility(View.GONE);
    }


}