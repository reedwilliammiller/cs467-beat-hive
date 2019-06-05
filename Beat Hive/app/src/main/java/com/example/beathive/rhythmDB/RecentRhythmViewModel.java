package com.example.beathive.rhythmDB;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;



// Handles holding and processing rhythm data from database so the Activities can only handle
// drawing data to the screen

public class RecentRhythmViewModel extends AndroidViewModel {
    private RhythmRepository mRepository;

    private LiveData<List<RhythmEntity>> mRecentRhythms;

    public RecentRhythmViewModel(Application application) {
        super(application);
        mRepository = new RhythmRepository(application);
        mRecentRhythms = mRepository.getRecentRhythms();
    }

    public LiveData<List<RhythmEntity>> getRecentRhythms() { return mRecentRhythms; }

}
