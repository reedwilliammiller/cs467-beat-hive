package com.example.metrognome.animation;

import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.widget.NumberPicker;

import com.example.metrognome.R;
import com.example.metrognome.time.Rhythm;

/*
The idea for this class comes from: https://mcochin.wordpress.com/2015/05/13/android-customizing-smoothscroller-for-the-recyclerview/
 */

public class ScrollingLayoutManager extends LinearLayoutManager {

    private Context context;
    private float MILLIS_PER_INCH = 0.5f;
    private Rhythm rhythm;

    public ScrollingLayoutManager(Context context) {
        super(context, LinearLayoutManager.HORIZONTAL, false);
        this.context = context;
    }

    public void setRhythm(Rhythm rhythm) {
        this.rhythm = rhythm;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView view, RecyclerView.State state, final int pos) {
        LinearSmoothScroller scroller = new LinearSmoothScroller(context) {

            @Override
            public PointF computeScrollVectorForPosition(int position) {
                return ScrollingLayoutManager.this.computeScrollVectorForPosition(position);
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics dm) {
                return MILLIS_PER_INCH/dm.densityDpi;
                /* TODO: AP - need to figure out this equation for all BPM and impact is has */
            }

            @Override
            protected int getHorizontalSnapPreference() {
                return SNAP_TO_END;
            }

            @Override
            protected int calculateTimeForScrolling(int dx) {
                NumberPicker bpmPicker = ((Activity) context).findViewById(R.id.number_picker_tempo);
                int bpm = bpmPicker.getValue();
                return dx * bpm / bpm;
                /* TODO: AP - fix for all BPM.. works "ok" around 100-120
                 * Part of the problem here is that no matter how I pass rhythm to this class
                  * I never seem to get the updated tempo, which throws off calculations. So */
            }
        };

        scroller.setTargetPosition(pos);
        startSmoothScroll(scroller);
    }
}
