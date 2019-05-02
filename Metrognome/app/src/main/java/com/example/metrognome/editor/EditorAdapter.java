package com.example.metrognome.editor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.metrognome.R;
import com.example.metrognome.time.Beat;

public class EditorAdapter extends RecyclerView.Adapter<EditorAdapter.EditorViewHolder> {

    private Beat[] beats;
    public LayoutInflater inflater;

    public EditorAdapter(Context context, Beat[] beats) {
        this.inflater = LayoutInflater.from(context);
        this.beats = beats;
    }

    @Override
    public EditorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.beat, parent, false); // need to create beatView
        EditorViewHolder holder = new EditorViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(EditorViewHolder holder, int position) {
        // update all of the beat fields based on the position, from the beats array above
        holder.beat = beats[position];
        /* make vertical layout from a beat for each subdivision, layout_weight=1 for each to make them
       each equally spaced, within that do the same for a vertical layout

       all of these need to be within something with a fairly large weight (maybe 3) to something
       with 1 weight that goes at the bottom and has the subdivisions button, this can be skipped
       in the playback activity

       at a high level the idea is to parse beats as 150dp wide things which are a horizontal layout
       within which there will be n subdivision vertical layouts, and within that the beatLayouts
       will hold a bunch of vertical layouts to hold the number (or +, trip, e, a, etc.), the button
       which will link to a popup view to select the sound

       I think here too for the vertical layout we can just do equal weights of 1 for everything, but
       the text size and button may need to be specified

       as a last note at this point we essentially have the horizontal linear layout that will
       represent the whole beat and the only other thing to do are the vertical borders (bolder for
       the  measure bar)*/
    }

    @Override
    public int getItemCount() {
        return beats.length;
    }

    public class EditorViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layout;
        public Beat beat;
        public LinearLayout[] beatLayouts;


        public EditorViewHolder(View v) {
            super(v);
            layout = (LinearLayout) v;
        }
    }
}
