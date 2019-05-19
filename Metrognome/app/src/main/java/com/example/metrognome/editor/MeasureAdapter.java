package com.example.metrognome.editor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.metrognome.R;
import com.example.metrognome.audio.SoundPoolWrapper;
import com.example.metrognome.time.Beat;
import com.example.metrognome.time.Measure;
import com.example.metrognome.time.Rhythm;

public class MeasureAdapter extends RecyclerView.Adapter<MeasureAdapter.BeatViewHolder> {
    private static final String TAG = MeasureAdapter.class.getSimpleName();
    private Rhythm rhythm;
    private LayoutInflater inflater;
    private boolean isEditable;
    private FragmentManager fragmentManager;

    public MeasureAdapter(Context context, FragmentManager fragmentManager, Rhythm rhythm, boolean isEditable) {
        this.inflater = LayoutInflater.from(context);
        this.fragmentManager = fragmentManager;
        this.rhythm = rhythm;
        this.isEditable = isEditable;
    }

    @Override
    public BeatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.beat, parent, false);
        BeatViewHolder holder = new BeatViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(BeatViewHolder holder, int position) {
        final Beat beat = rhythm.getBeatAt(position);
        holder.beat = beat;
        holder.init();
    }

    @Override
    public int getItemCount() {
        return rhythm.getBeatCount();
    }

    public class BeatViewHolder extends RecyclerView.ViewHolder {
        private Rhythm rhythm;
        private Measure measure;
        public Beat beat;

        private View view;
        private TextView measureTextView;
        private TextView timeSignatureTextView;
        private View measureView;

        public BeatViewHolder(View v) {
            super(v);
            view = v;
        }

        public void init() {
            measure = beat.getMeasure();
            rhythm = measure.getRhythm();
            Log.d(TAG, beat.toString());
            boolean isFirstBeat = beat.getIndex() == 0;

            measureView = view.findViewById(R.id.measure_label);
            if (!isFirstBeat) {
                measureView.setVisibility(View.GONE);
            } else {
                measureTextView = view.findViewById(R.id.text_view_measure);
                timeSignatureTextView = view.findViewById(R.id.text_view_measure_time_signature);
                measureTextView.setText(measure.getIndex() + 1 + "/" + rhythm.getMeasureCount());
                timeSignatureTextView.setText(measure.getTimeSignature().getTopSignature() + "/" + measure.getTimeSignature().getBottomSignature());
                if (isEditable) {
                    measureView.setBackgroundColor(R.color.colorGray);
                    measureView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDialog();
                        }
                    });
                }
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
        }

        private void setupNote(final int noteId, final int subdivisionIndex) {
            View noteView = view.findViewById(noteId);
            ToggleButton noteButton = noteView.findViewById(R.id.button_note);
            TextView noteText = noteView.findViewById(R.id.text_view_note);
            if (noteText != null) {
                String text;
                Resources resources = view.getResources();
                if (subdivisionIndex == 0) {
                    text = Integer.toString(beat.getIndex() + 1);
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

            if (subdivisionIndex < beat.getSubdivisions() && beat.getSoundAt(subdivisionIndex) != SoundPoolWrapper.INAUDIBLE) {
                noteButton.setChecked(true);
            }
            if (isEditable) {
                noteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            beat.setSoundAt(subdivisionIndex, SoundPoolWrapper.DEFAULT_SOUND);
                        } else {
                            beat.setSoundAt(subdivisionIndex, SoundPoolWrapper.INAUDIBLE);
                        }
                    }
                });
            } else {
                noteButton.setEnabled(false);
            }
        }

        private void updateVisibility() {
            int subdivisionCount = beat.getSubdivisions();
            if (subdivisionCount < 2) {
                view.findViewById(R.id.button_remove_subdivision).setVisibility(View.GONE);
                view.findViewById(R.id.note_2).setVisibility(View.GONE);
            } else {
                view.findViewById(R.id.button_remove_subdivision).setVisibility(View.VISIBLE);
                view.findViewById(R.id.note_2).setVisibility(View.VISIBLE);
            }
            if (subdivisionCount < 3) {
                view.findViewById(R.id.note_3).setVisibility(View.GONE);
            } else {
                view.findViewById(R.id.note_3).setVisibility(View.VISIBLE);
            }
            if (subdivisionCount < 4) {
                view.findViewById(R.id.button_add_subdivision).setVisibility(View.VISIBLE);
                view.findViewById(R.id.note_4).setVisibility(View.GONE);
            } else {
                view.findViewById(R.id.button_add_subdivision).setVisibility(View.GONE);
                view.findViewById(R.id.note_4).setVisibility(View.VISIBLE);
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
                    beat.removeSubdivision();
                    updateVisibility();
                }
            });
        }

        public void showDialog() {
            TimeSignatureDialogFragment dialogFragment = new TimeSignatureDialogFragment();
            dialogFragment.measure = measure;
            dialogFragment.show(fragmentManager, "TimeSignatureDialog");
        }
    }
}
