package com.example.metrognome.editor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.metrognome.R;

public class EditorAdapter extends RecyclerView.Adapter<EditorAdapter.EditorViewHolder> {

    private String[] beatData = {"beat1", "beat2", "beat3"};

    @Override
    public EditorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.beatView, parent, false); // need to create beatView
        EditorViewHolder holder = new EditorViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(EditorViewHolder holder, int position) {
        holder.beatView.setText(beatData[position]);
    }

    @Override
    public int getItemCount() {
        return beatData.length;
    }

    public class EditorViewHolder extends RecyclerView.ViewHolder {
        public TextView beatView;

        public EditorViewHolder(TextView v) {
            super(v);
            beatView = v;
        }
    }

}
