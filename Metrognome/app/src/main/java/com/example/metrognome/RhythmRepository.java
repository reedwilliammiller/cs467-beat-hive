package com.example.metrognome;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;



// Manages the query threads to allow us to use multiple backends in case we want to store rhythms
// in the cloud at any point

public class RhythmRepository {

    private RhythmDao mRhythmDao;
    private LiveData<List<Rhythm>> mAllRhythms;
    private LiveData<List<Rhythm>> mRecentRhythms;

    RhythmRepository(Application application) {
        RhythmRoomDatabase db = RhythmRoomDatabase.getDatabase(application);
        mRhythmDao = db.rhythmDao();
        mAllRhythms = mRhythmDao.getAllRhythms();
        mRecentRhythms = mRhythmDao.getRecentRhythms();
    }

    LiveData<List<Rhythm>> getAllRhythms() {
        return mAllRhythms;
    }

    LiveData<List<Rhythm>> getRecentRhythms() {
        return mRecentRhythms;
    }

    public void insert (Rhythm rhythm) {
        new insertAsyncTask(mRhythmDao).execute(rhythm);
    }

    private static class insertAsyncTask extends AsyncTask<Rhythm, Void, Void> {

        private RhythmDao mAsyncTaskDao;

        insertAsyncTask(RhythmDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Rhythm... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
