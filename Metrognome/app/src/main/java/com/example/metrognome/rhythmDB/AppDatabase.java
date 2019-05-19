package com.example.metrognome.rhythmDB;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.metrognome.audio.SoundPoolWrapper;
import com.example.metrognome.time.Measure;
import com.example.metrognome.time.Rhythm;
import com.example.metrognome.time.TimeSignature;

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
                            .allowMainThreadQueries()
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

            Rhythm RUMBA_CLAVE = new Rhythm( 120);

            Measure first = new Measure(RUMBA_CLAVE, 0, TimeSignature.COMMON_TIME);
            first.getBeatAt(0).setSoundAt(0, SoundPoolWrapper.INAUDIBLE);
            first.getBeatAt(3).setSoundAt(0, SoundPoolWrapper.INAUDIBLE);

            Measure second = new Measure(RUMBA_CLAVE, 1, TimeSignature.COMMON_TIME);
            second.getBeatAt(1).subdivideBy(2);
            second.getBeatAt(1).setSoundAt(0, SoundPoolWrapper.INAUDIBLE);
            second.getBeatAt(1).setSoundAt(1, SoundPoolWrapper.DEFAULT_SOUND);
            second.getBeatAt(2).setSoundAt(0, SoundPoolWrapper.INAUDIBLE);
            second.getBeatAt(3).subdivideBy(2);
            second.getBeatAt(3).setSoundAt(0, SoundPoolWrapper.INAUDIBLE);
            second.getBeatAt(3).setSoundAt(1, SoundPoolWrapper.DEFAULT_SOUND);

            RUMBA_CLAVE.addMeasure(first);
            RUMBA_CLAVE.addMeasure(second);

            RhythmEntity rhythmEntity = new RhythmEntity(0, "Rumba Clave", RUMBA_CLAVE);

            mDao.insert(rhythmEntity);

            Rhythm FOUR = new Rhythm( 120);
            first = new Measure(FOUR, 0, TimeSignature.COMMON_TIME);
            FOUR.addMeasure(first);
            rhythmEntity = new RhythmEntity(0, "Four on the Floor", FOUR);

            mDao.insert(rhythmEntity);

            Rhythm SPEED = new Rhythm( 220);

            first = new Measure(SPEED, 0, TimeSignature.COMMON_TIME);

            second = new Measure(SPEED, 1, TimeSignature.COMMON_TIME);
            second.getBeatAt(0).subdivideBy(2);
            second.getBeatAt(0).setSoundAt(0, SoundPoolWrapper.DEFAULT_SOUND);
            second.getBeatAt(0).setSoundAt(1, SoundPoolWrapper.DEFAULT_SOUND);
            second.getBeatAt(1).subdivideBy(2);
            second.getBeatAt(1).setSoundAt(0, SoundPoolWrapper.DEFAULT_SOUND);
            second.getBeatAt(1).setSoundAt(1, SoundPoolWrapper.DEFAULT_SOUND);
            second.getBeatAt(2).subdivideBy(2);
            second.getBeatAt(2).setSoundAt(0, SoundPoolWrapper.DEFAULT_SOUND);
            second.getBeatAt(2).setSoundAt(1, SoundPoolWrapper.DEFAULT_SOUND);
            second.getBeatAt(3).subdivideBy(2);
            second.getBeatAt(3).setSoundAt(0, SoundPoolWrapper.DEFAULT_SOUND);
            second.getBeatAt(3).setSoundAt(1, SoundPoolWrapper.DEFAULT_SOUND);

            SPEED.addMeasure(first);
            SPEED.addMeasure(second);

            rhythmEntity = new RhythmEntity(0, "Speed Up", SPEED);

            mDao.insert(rhythmEntity);

            Rhythm SPORT = new Rhythm( 120);

            first = new Measure(SPORT, 0, TimeSignature.COMMON_TIME);
            first.getBeatAt(1).setSoundAt(0,SoundPoolWrapper.INAUDIBLE);
            first.getBeatAt(3).setSoundAt(0,SoundPoolWrapper.INAUDIBLE);

            second = new Measure(SPORT, 1, TimeSignature.COMMON_TIME);
            second.getBeatAt(0).subdivideBy(3);
            second.getBeatAt(0).setSoundAt(0, SoundPoolWrapper.DEFAULT_SOUND);
            second.getBeatAt(0).setSoundAt(1, SoundPoolWrapper.INAUDIBLE);
            second.getBeatAt(0).setSoundAt(2, SoundPoolWrapper.DEFAULT_SOUND);
            second.getBeatAt(1).subdivideBy(3);
            second.getBeatAt(1).setSoundAt(0, SoundPoolWrapper.DEFAULT_SOUND);
            second.getBeatAt(1).setSoundAt(1, SoundPoolWrapper.INAUDIBLE);
            second.getBeatAt(1).setSoundAt(2, SoundPoolWrapper.DEFAULT_SOUND);
            second.getBeatAt(2).subdivideBy(3);
            second.getBeatAt(2).setSoundAt(0, SoundPoolWrapper.INAUDIBLE);
            second.getBeatAt(2).setSoundAt(1, SoundPoolWrapper.DEFAULT_SOUND);
            second.getBeatAt(2).setSoundAt(2, SoundPoolWrapper.INAUDIBLE);
            second.getBeatAt(3).setSoundAt(0, SoundPoolWrapper.INAUDIBLE);

            SPORT.addMeasure(first);
            SPORT.addMeasure(second);

            rhythmEntity = new RhythmEntity(0, "Sports Clap", SPORT);
            mDao.insert(rhythmEntity);
            return null;
        }
    }
}