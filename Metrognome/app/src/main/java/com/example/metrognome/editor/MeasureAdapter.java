package com.example.metrognome.editor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.metrognome.R;
import com.example.metrognome.audio.SoundPoolWrapper;
import com.example.metrognome.time.Beat;
import com.example.metrognome.time.Rhythm;
import com.example.metrognome.time.TimeSignature;

public class MeasureAdapter extends RecyclerView.Adapter<MeasureAdapter.BeatViewHolder> {
    private static final String TAG = MeasureAdapter.class.getSimpleName();
    private Rhythm rhythm;
    public LayoutInflater inflater;

    public MeasureAdapter(Context context, Rhythm rhythm) {
        this.inflater = LayoutInflater.from(context);
        this.rhythm = rhythm;
    }

    @Override
    public BeatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.beat, parent, false);
        BeatViewHolder holder = new BeatViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(BeatViewHolder holder, int position) {
        holder.beat = rhythm.getBeatAt(position);
        holder.measureIndex = rhythm.getMeasureIndexOfBeatAt(position);
        holder.totalMeasures = rhythm.getMeasureCount();
        holder.timeSignature = rhythm.getMeasureAt(holder.measureIndex).getTimeSignature();
        holder.isFirstBeat = rhythm.isFirstBeatOfMeasureAt(position);
        holder.init();
    }

    @Override
    public int getItemCount() {
        return rhythm.getBeatCount();
    }

    public class BeatViewHolder extends RecyclerView.ViewHolder {
        public Beat beat;
        public boolean isFirstBeat;
        public int measureIndex;
        public int totalMeasures;
        public TimeSignature timeSignature;

        private View view;
        private TextView measureTextView;
        private TextView timeSignatureTextView;

        public BeatViewHolder(View v) {
            super(v);
            view = v;
        }
        public void init() {
            if (!isFirstBeat) {
                view.findViewById(R.id.measure_label).setVisibility(View.GONE);
            } else {
                measureTextView = view.findViewById(R.id.text_view_measure);
                timeSignatureTextView = view.findViewById(R.id.text_view_measure_time_signature);
                measureTextView.setText(measureIndex + 1 + "/" + totalMeasures);
                timeSignatureTextView.setText(timeSignature.getBeats() + "/" + timeSignature.getNote());
            }

            int subdivisionCount = beat.getSubdivisions();
            if (subdivisionCount < 2) {
                view.findViewById(R.id.note_2).setVisibility(View.GONE);
            }
            if (subdivisionCount < 3) {
                view.findViewById(R.id.note_3).setVisibility(View.GONE);
            }
            if (subdivisionCount < 4) {
                view.findViewById(R.id.note_4).setVisibility(View.GONE);
            }

            ToggleButton noteButton = view.findViewById(R.id.note_1).findViewById(R.id.button_note);
            noteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        beat.setSoundAt(0, SoundPoolWrapper.DEFAULT_SOUND);
                    } else {
                        beat.setSoundAt(0, SoundPoolWrapper.INAUDIBLE);
                    }
                }
            });

            noteButton = view.findViewById(R.id.note_2).findViewById(R.id.button_note);
            noteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        beat.setSoundAt(1, SoundPoolWrapper.DEFAULT_SOUND);
                    } else {
                        beat.setSoundAt(1, SoundPoolWrapper.INAUDIBLE);
                    }
                }
            });

            noteButton = view.findViewById(R.id.note_3).findViewById(R.id.button_note);
            noteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        beat.setSoundAt(2, SoundPoolWrapper.DEFAULT_SOUND);
                    } else {
                        beat.setSoundAt(2, SoundPoolWrapper.INAUDIBLE);
                    }
                }
            });

            noteButton = view.findViewById(R.id.note_4).findViewById(R.id.button_note);
            noteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        beat.setSoundAt(3, SoundPoolWrapper.DEFAULT_SOUND);
                    } else {
                        beat.setSoundAt(3, SoundPoolWrapper.INAUDIBLE);
                    }
                }
            });
        }
    }
}
