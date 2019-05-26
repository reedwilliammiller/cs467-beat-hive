package com.example.metrognome.rhythmDB;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.Date;
import java.util.List;



// Manages the query threads to allow us to use multiple backends in case we want to store rhythms
// in the cloud at any point

public class RhythmRepository {
    private RhythmDao mRhythmDao;
    private RhythmEntity mRhythmEntity;
    private LiveData<List<RhythmEntity>> mAllRhythms;
    private LiveData<List<RhythmEntity>> mRecentRhythms;
    private int ID;

    RhythmRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mRhythmDao = db.rhythmDao();
        mAllRhythms = mRhythmDao.getAllRhythms();
        mRecentRhythms = mRhythmDao.getRecentRhythms();
    }

    RhythmRepository(Application application, int ID) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mRhythmDao = db.rhythmDao();
        if(ID == 0){
            mRhythmEntity = mRhythmDao.getRecentRhythm();
        }
        else{
            mRhythmEntity = mRhythmDao.getRhythmById(ID);
        }
        this.ID = ID;
    }

    LiveData<List<RhythmEntity>> getAllRhythms() {
        return mAllRhythms;
    }

    LiveData<List<RhythmEntity>> getRecentRhythms() {
        return mRecentRhythms;
    }

    RhythmEntity getmRhythmEntity() { return mRhythmEntity; }

    public void setLastOpened() {
        mRhythmDao.setLastOpened(ID, new Date());
    }

    public void insert (RhythmEntity rhythmEntity) {
       new InsertAsyncTask(mRhythmDao).execute(rhythmEntity);
    }

    public void update(RhythmEntity rhythmEntity) {
        new UpdateAsyncTask(mRhythmDao).execute(rhythmEntity);
    }

    public void delete(RhythmEntity rhythmEntity) {
        new DeleteAsyncTask(mRhythmDao).execute(rhythmEntity);
    }

    private static class InsertAsyncTask extends AsyncTask<RhythmEntity, Void, Void> {

        private RhythmDao mAsyncTaskDao;

        InsertAsyncTask(RhythmDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final RhythmEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<RhythmEntity, Void, Void> {
        private RhythmDao mAsyncTaskDao;

        UpdateAsyncTask(RhythmDao rhythmDao) {
            mAsyncTaskDao = rhythmDao;
        }

        @Override
        protected Void doInBackground(final RhythmEntity... params) {
            mAsyncTaskDao.update(params);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<RhythmEntity, Void, Void> {
        private RhythmDao mAsyncTaskDao;

        DeleteAsyncTask(RhythmDao rhythmDao) {
            mAsyncTaskDao = rhythmDao;
        }

        @Override
        protected Void doInBackground(final RhythmEntity... params) {
            mAsyncTaskDao.delete(params);
            return null;
        }
    }
}
