package com.example.metrognome;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;



// Handles holding and processing rhythm data from database so the Activities can only handle
// drawing data to the screen

public class RhythmViewModel extends AndroidViewModel {
    private RhythmRepository mRepository;

    private LiveData<List<Rhythm>> mAllRhythms;

    public RhythmViewModel (Application application) {
        super(application);
        mRepository = new RhythmRepository(application);
        mAllRhythms = mRepository.getAllRhythms();
    }

    LiveData<List<Rhythm>> getAllRhythms() { return mAllRhythms; }

    public void insert(Rhythm rhythm) { mRepository.insert(rhythm); }

}
