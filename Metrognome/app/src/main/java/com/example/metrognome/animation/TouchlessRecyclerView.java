package com.example.metrognome.animation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * A simple override of RecyclerView to disable touch events.
 */
public class TouchlessRecyclerView extends RecyclerView {

    public TouchlessRecyclerView(Context context) {
        super(context);
    }
    public TouchlessRecyclerView(Context context, AttributeSet as) { super(context, as); }
    public TouchlessRecyclerView(Context context, AttributeSet as, int a) { super(context, as, a); }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) { return true; }
}
