package com.example.metrognome;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import java.util.List;



public class RhythmListAdapter extends RecyclerView.Adapter<RhythmListAdapter.RhythmViewHolder> {
    class RhythmViewHolder extends RecyclerView.ViewHolder {
        private final Button rhythmItemView;

        private RhythmViewHolder(View itemView) {
            super(itemView);
            rhythmItemView = itemView.findViewById(R.id.rhythmOpenButton);
        }
    }

    private final LayoutInflater mInflater;
    private List<Rhythm> mRhythms;

    RhythmListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public RhythmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_rhythm_file, parent, false);
        return new RhythmViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(RhythmViewHolder holder, int position) {
        if (mRhythms != null) {
            Rhythm current = mRhythms.get(position);
            holder.rhythmItemView.setText(current.getTitle());
        } else {
            holder.rhythmItemView.setText("No Rhythm");
        }
    }

    void setRhythms(List<Rhythm> rhythms) {
        mRhythms = rhythms;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mRhythms != null)
            return mRhythms.size();
        else return 0;
    }
}
