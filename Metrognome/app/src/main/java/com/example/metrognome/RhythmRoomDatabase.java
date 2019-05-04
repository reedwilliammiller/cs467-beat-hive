package com.example.metrognome;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;


@Database(entities = {Rhythm.class}, version = 1)
@TypeConverters({TimestampConverter.class})
public abstract class RhythmRoomDatabase extends RoomDatabase {
    public abstract RhythmDao rhythmDao();

    private static volatile RhythmRoomDatabase INSTANCE;

    static RhythmRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RhythmRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RhythmRoomDatabase.class, "rhythm_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final RhythmDao mDao;

        PopulateDbAsync(RhythmRoomDatabase db) {
            mDao = db.rhythmDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            Rhythm rhythm = new Rhythm(0, "My Phat Beat", "beat.json", true);
            mDao.insert(rhythm);
            rhythm = new Rhythm(0, "My Phat Beat 2", "beat2.json", true);
            mDao.insert(rhythm);
            rhythm = new Rhythm(0, "My Phat Beat 3", "beat3.json", true);
            mDao.insert(rhythm);
            rhythm = new Rhythm(0, "My Phat Beat 4", "beat4.json", true);
            mDao.insert(rhythm);
            rhythm = new Rhythm(0, "My Phat Beat 5", "beat5.json", true);
            mDao.insert(rhythm);
            rhythm = new Rhythm(0, "Mambo #5", "beat6.json", true);
            mDao.insert(rhythm);
            return null;
        }
    }

}