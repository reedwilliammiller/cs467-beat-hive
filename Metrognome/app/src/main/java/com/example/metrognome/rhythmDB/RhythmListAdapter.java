package com.example.metrognome.rhythmDB;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.metrognome.PlaybackActivity;
import com.example.metrognome.R;

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
    private List<RhythmEntity> mRhythmEntities;

    public RhythmListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public RhythmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_rhythm_file, parent, false);
        return new RhythmViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(RhythmViewHolder holder, int position) {
        if (mRhythmEntities != null) {
            final RhythmEntity current = mRhythmEntities.get(position);
            holder.rhythmItemView.setText(current.getTitle());
            holder.rhythmItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, PlaybackActivity.class);
                    intent.putExtra("ID", current.getId());
                    context.startActivity(intent);
                }
            });
        } else {
            holder.rhythmItemView.setText("No RhythmEntity");
        }
    }

    public void setRhythms(List<RhythmEntity> rhythmEntities) {
        mRhythmEntities = rhythmEntities;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mRhythmEntities != null)
            return mRhythmEntities.size();
        else return 0;
    }
}
