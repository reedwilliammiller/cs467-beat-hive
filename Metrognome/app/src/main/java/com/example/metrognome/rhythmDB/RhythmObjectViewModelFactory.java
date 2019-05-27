package com.example.metrognome.rhythmDB;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class RhythmObjectViewModelFactory implements ViewModelProvider.Factory {

    private Application mApplication;
    private int mID;

    public RhythmObjectViewModelFactory(Application application, int ID) {
        mApplication = application;
        mID = ID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RhythmObjectViewModel(mApplication, mID);
    }
}