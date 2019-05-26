package com.example.metrognome.rhythmDB;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;


// Handles holding and processing rhythm data from database so the Activities can only handle
// drawing data to the screen

public class RhythmObjectViewModel extends AndroidViewModel {
    private RhythmRepository mRepository;
    private RhythmEntity rhythmEntity;

    RhythmObjectViewModel(Application application, int ID) {
        super(application);
        mRepository = new RhythmRepository(application, ID);
        rhythmEntity = mRepository.getmRhythmEntity();
    }

    public RhythmEntity getRhythmEntity() {
        return rhythmEntity;
    }

    public RhythmRepository getRhythmRepository() {
        return mRepository;
    }

    public void setLastOpened() { mRepository.setLastOpened(); }

}
