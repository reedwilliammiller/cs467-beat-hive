package com.example.metrognome.animation;

import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.widget.NumberPicker;

import com.example.metrognome.R;

/*
The idea for this class comes from:
https://mcochin.wordpress.com/2015/05/13/android-customizing-smoothscroller-for-the-recyclerview/
*/

public class ScrollingLayoutManager extends LinearLayoutManager {

    private Context context;
    private LinearSmoothScroller scroller;
    private int offset;

    public ScrollingLayoutManager(Context context) {
        super(context, LinearLayoutManager.HORIZONTAL, false);
        this.context = context;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView view, RecyclerView.State state, final int pos) {
        this.scroller  = new LinearSmoothScroller(context) {
            @Override
            public PointF computeScrollVectorForPosition(int position) {
                return ScrollingLayoutManager.this.computeScrollVectorForPosition(position);
            }

            @Override
            protected int calculateTimeForScrolling(int dx) {
                return (int)(1.0857 * Math.pow(getBPM(), 2.) + -353.9635 * getBPM() + 36500) + offset; // 36081.9094
            }
        };
        scroller.setTargetPosition(pos);
        startSmoothScroll(scroller);
    }

    public void deltaDynamicOffset(int offset) { this.offset = offset; }

    private int getBPM() {
        NumberPicker bpmPicker = ((Activity) context).findViewById(R.id.number_picker_tempo);
        return bpmPicker.getValue();
    }
}
