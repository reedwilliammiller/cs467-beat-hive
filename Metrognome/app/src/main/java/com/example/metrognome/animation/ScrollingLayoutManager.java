package com.example.metrognome.animation;

import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.NumberPicker;

import com.example.metrognome.R;

/*
The idea for this class comes from:
https://mcochin.wordpress.com/2015/05/13/android-customizing-smoothscroller-for-the-recyclerview/
*/

public class ScrollingLayoutManager extends LinearLayoutManager {

    private Context context;
    private LinearSmoothScroller scroller;
    private int iteration = 1;

    public ScrollingLayoutManager(Context context) {
        super(context, LinearLayoutManager.HORIZONTAL, false);
        this.context = context;
        scroller  = new LinearSmoothScroller(context) {
            @Override
            public PointF computeScrollVectorForPosition(int position) {
                return ScrollingLayoutManager.this.computeScrollVectorForPosition(position);
            }

//            @Override
//            protected float calculateSpeedPerPixel(DisplayMetrics dm) { return 2; }

            @Override
            protected int calculateTimeForScrolling(int dx) {
                return 19000;
                /* TODO: AP
                 * Estimates:
                 * 160 ~= 7150
                 * 120 ~= 9550
                 * 80 ~= 14250
                 * 60 ~= 19000
                 */
            }
        };
    }

    @Override
    public void smoothScrollToPosition(RecyclerView view, RecyclerView.State state, final int pos) {
        scroller.setTargetPosition(pos);
        startSmoothScroll(scroller);
    }

    private int getBPM() {
        NumberPicker bpmPicker = ((Activity) context).findViewById(R.id.number_picker_tempo);
        return bpmPicker.getValue();
    }
}
