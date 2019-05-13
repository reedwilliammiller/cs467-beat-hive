package com.example.metrognome.rhythmDB;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;



// Manages the query threads to allow us to use multiple backends in case we want to store rhythms
// in the cloud at any point

public class RhythmRepository {

    private RhythmDao mRhythmDao;
    private LiveData<List<RhythmEntity>> mAllRhythms;
    private LiveData<List<RhythmEntity>> mRecentRhythms;

    RhythmRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mRhythmDao = db.rhythmDao();
        mAllRhythms = mRhythmDao.getAllRhythms();
        mRecentRhythms = mRhythmDao.getRecentRhythms();
    }

    LiveData<List<RhythmEntity>> getAllRhythms() {
        return mAllRhythms;
    }

    LiveData<List<RhythmEntity>> getRecentRhythms() {
        return mRecentRhythms;
    }

    public void insert (RhythmEntity rhythmEntity) {
        new insertAsyncTask(mRhythmDao).execute(rhythmEntity);
    }

    private static class insertAsyncTask extends AsyncTask<RhythmEntity, Void, Void> {

        private RhythmDao mAsyncTaskDao;

        insertAsyncTask(RhythmDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final RhythmEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
