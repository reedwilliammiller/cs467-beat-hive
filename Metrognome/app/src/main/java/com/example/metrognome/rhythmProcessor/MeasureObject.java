package com.example.metrognome.rhythmProcessor;

import java.util.ArrayList;

public class MeasureObject {
    private Integer top_sig;
    private Integer bottom_sig;

    private ArrayList<Integer> beats;

    //Constructor
    public MeasureObject(Integer top, Integer bottom){
        this.top_sig = top;
        this.bottom_sig = bottom;
        this.beats = new ArrayList<>();
        for (int i = 0; i < top; i++){
            this.beats.add(0);
        }
    }

    int[] getTimeSignature(){
        int[] timesig = new int[2];
        timesig[0] = top_sig;
        timesig[1] = bottom_sig;
        return timesig;
    }

    public Integer getSubdivisions(){
        return beats.size();
    }

    public void addNote(Integer note){
        this.beats.add(note);
    }

    public void changeNote(Integer note, Integer index){
        this.beats.set(index, note);
    }

    public void removeNote(Integer index){
        this.beats.remove(index);
    }

}
