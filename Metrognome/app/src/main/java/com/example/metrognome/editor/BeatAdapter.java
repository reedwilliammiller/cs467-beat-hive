package com.example.metrognome.editor;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.metrognome.R;
import com.example.metrognome.time.Beat;
import com.example.metrognome.time.Rhythm;

public class BeatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = BeatAdapter.class.getSimpleName();
    private final FragmentManager fragmentManager;
    private final Rhythm rhythm;
    private final LayoutInflater inflater;
    private final boolean isEditable;

    public BeatAdapter(Context context, FragmentManager fragmentManager, Rhythm rhythm, boolean isEditable) {
        this.inflater = LayoutInflater.from(context);
        this.rhythm = rhythm;
        this.isEditable = isEditable;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public BeatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.beat, parent, false);
        BeatViewHolder holder = new BeatViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BeatViewHolder) {
            BeatViewHolder viewHolder = (BeatViewHolder) holder;
            viewHolder.beat = rhythm.getBeatAt(position % rhythm.getBeatCount());
            viewHolder.measure = rhythm.getMeasureForBeatAt(position % rhythm.getBeatCount());
            viewHolder.rhythm = rhythm;
            viewHolder.isEditable = this.isEditable;
            if (isEditable) {
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        v.setSelected(true);
                        DialogFragment beatEditorAlertDialog = new BeatEditorAlertDialog();
                        Bundle args = new Bundle();
                        args.putInt(BeatEditorAlertDialog.KEY_BEAT_INDEX, holder.getAdapterPosition());
                        beatEditorAlertDialog.setArguments(args);
                        beatEditorAlertDialog.show(fragmentManager, "beat_editor");
                        return true;
                    }
                });
            }

            viewHolder.init();
        }
    }

    @Override
    public int getItemCount() {
        if (isEditable) {
            return rhythm.getBeatCount();
        }
        return Integer.MAX_VALUE;
    }
}
