package com.example.metrognome.editor;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

import com.example.metrognome.R;
import com.example.metrognome.audio.SoundPoolWrapper;

public class NoteImageButton extends AppCompatImageButton {
    private SoundPoolWrapper soundPoolWrapper;
    private Drawable[] noteDrawables;
    private int state = -1;

    public NoteImageButton(Context context) {
        super(context);
        init(context);
    }

    public NoteImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NoteImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        soundPoolWrapper = SoundPoolWrapper.get(context);
        TypedArray typedArray = context.getResources().obtainTypedArray(R.array.noteDrawables);
        noteDrawables = new Drawable[typedArray.length()];
        for (int i = 0; i < typedArray.length(); i++) {
            noteDrawables[i] = typedArray.getDrawable(i);
        }
        typedArray.recycle();
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        updateImageDrawable();
    }

    /**
     * Updates the state of this drawable to the next note.
     * When this state is the last note, this updates the state to -1.
     */
    public void nextState() {
        state++;
        if (state == noteDrawables.length) {
            state = -1;
        }
        updateImageDrawable();
        soundPoolWrapper.play(state);
    }

    private void updateImageDrawable() {
        Drawable drawable = null;
        if (state != -1) {
            drawable = noteDrawables[state];
        }
        setImageDrawable(drawable);
    }
}
