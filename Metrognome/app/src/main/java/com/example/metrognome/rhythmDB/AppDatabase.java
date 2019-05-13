package com.example.metrognome.rhythmDB;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.metrognome.time.Rhythm;


@Database(entities = {RhythmEntity.class}, version = 1, exportSchema = false)
@TypeConverters({TimestampConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract RhythmDao rhythmDao();

    private static volatile AppDatabase INSTANCE;

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "rhythm_database")
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

        PopulateDbAsync(AppDatabase db) {
            mDao = db.rhythmDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            Rhythm phatBeat = new Rhythm("My Phat Beat", 120);
            RhythmEntity rhythmEntity = new RhythmEntity(0, phatBeat.getName(), phatBeat);
            mDao.insert(rhythmEntity);
            Rhythm phatBeat1 = new Rhythm("My Phat Beat 2", 120);
            RhythmEntity rhythmEntity1 = new RhythmEntity(0, phatBeat1.getName(), phatBeat);
            mDao.insert(rhythmEntity1);
            Rhythm phatBeat2 = new Rhythm("My Phat Beat 3", 120);
            RhythmEntity rhythmEntity2 = new RhythmEntity(0, phatBeat2.getName(), phatBeat);
            mDao.insert(rhythmEntity2);
            Rhythm phatBeat3 = new Rhythm("My Phat Beat 4", 120);
            RhythmEntity rhythmEntity3 = new RhythmEntity(0, phatBeat3.getName(), phatBeat);
            mDao.insert(rhythmEntity3);
            return null;
        }
    }

}