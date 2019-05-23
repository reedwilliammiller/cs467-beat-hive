package com.example.metrognome.rhythmDB;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.example.metrognome.time.Rhythm;


// Handles holding and processing rhythm data from database so the Activities can only handle
// drawing data to the screen

public class RhythmObjectViewModel extends AndroidViewModel {
    private RhythmRepository mRepository;
    private Rhythm mRhythm;

    RhythmObjectViewModel(Application application, int ID) {
        super(application);
        mRepository = new RhythmRepository(application, ID);
        RhythmEntity mRhythmEntityByID = mRepository.getmRhythmEntity();
        mRhythm = mRhythmEntityByID.getRhythm();

    }

    public Rhythm getRhythm() { return mRhythm;}

}
