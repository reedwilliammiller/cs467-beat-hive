package com.example.metrognome.rhythmDB;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;



// Handles holding and processing rhythm data from database so the Activities can only handle
// drawing data to the screen

public class RhythmViewModel extends AndroidViewModel {
    private RhythmRepository mRepository;

    private LiveData<List<RhythmEntity>> mAllRhythms;

    public RhythmViewModel (Application application) {
        super(application);
        mRepository = new RhythmRepository(application);
        mAllRhythms = mRepository.getAllRhythms();
    }

    public LiveData<List<RhythmEntity>> getAllRhythms() { return mAllRhythms; }

    public void insert(RhythmEntity rhythmEntity) { mRepository.insert(rhythmEntity); }

    public int getID(RhythmEntity rhythmEntity) { return rhythmEntity.getId(); }

}
