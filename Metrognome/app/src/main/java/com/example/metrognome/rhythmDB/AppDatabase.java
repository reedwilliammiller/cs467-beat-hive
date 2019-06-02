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
                public void onCreate (@NonNull SupportSQLiteDatabase db){
                    super.onCreate(db);
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
            RUMBA_CLAVE.addMeasure(new Measure(4));
            RUMBA_CLAVE.addMeasure(new Measure(4));
            RUMBA_CLAVE.getBeatAt(0).setSoundAt(0, SoundPoolWrapper.INAUDIBLE);
            RUMBA_CLAVE.getBeatAt(3).setSoundAt(0, SoundPoolWrapper.INAUDIBLE);
            RUMBA_CLAVE.getBeatAt(5).subdivideBy(2);
            RUMBA_CLAVE.getBeatAt(5).setSoundAt(0, SoundPoolWrapper.INAUDIBLE);
            RUMBA_CLAVE.getBeatAt(5).setSoundAt(1, SoundPoolWrapper.DEFAULT_SOUND);
            RUMBA_CLAVE.getBeatAt(6).setSoundAt(0, SoundPoolWrapper.INAUDIBLE);
            RUMBA_CLAVE.getBeatAt(7).subdivideBy(2);
            RUMBA_CLAVE.getBeatAt(7).setSoundAt(0, SoundPoolWrapper.INAUDIBLE);
            RUMBA_CLAVE.getBeatAt(7).setSoundAt(1, SoundPoolWrapper.DEFAULT_SOUND);
            mDao.insert(new RhythmEntity(0, "Rumba Clave", RUMBA_CLAVE));

            Rhythm FOUR = new Rhythm( 120);
            FOUR.addMeasure(new Measure( 4));
            mDao.insert(new RhythmEntity(1, "Four on the Floor", FOUR));

            Rhythm SPEED = new Rhythm( 220);
            SPEED.addMeasure(new Measure( 4));
            SPEED.addMeasure(new Measure( 4));
            SPEED.getBeatAt(4).subdivideBy(2);
            SPEED.getBeatAt(4).setSoundAt(0, SoundPoolWrapper.DEFAULT_SOUND);
            SPEED.getBeatAt(4).setSoundAt(1, SoundPoolWrapper.DEFAULT_SOUND);
            SPEED.getBeatAt(5).subdivideBy(2);
            SPEED.getBeatAt(5).setSoundAt(0, SoundPoolWrapper.DEFAULT_SOUND);
            SPEED.getBeatAt(5).setSoundAt(1, SoundPoolWrapper.DEFAULT_SOUND);
            SPEED.getBeatAt(6).subdivideBy(2);
            SPEED.getBeatAt(6).setSoundAt(0, SoundPoolWrapper.DEFAULT_SOUND);
            SPEED.getBeatAt(6).setSoundAt(1, SoundPoolWrapper.DEFAULT_SOUND);
            SPEED.getBeatAt(7).subdivideBy(2);
            SPEED.getBeatAt(7).setSoundAt(0, SoundPoolWrapper.DEFAULT_SOUND);
            SPEED.getBeatAt(7).setSoundAt(1, SoundPoolWrapper.DEFAULT_SOUND);
            mDao.insert(new RhythmEntity(2, "Speed Up", SPEED));

            Rhythm SPORT = new Rhythm( 120);
            SPORT.addMeasure(new Measure( 4));
            SPORT.addMeasure(new Measure( 4));
            SPORT.getBeatAt(1).setSoundAt(0,SoundPoolWrapper.INAUDIBLE);
            SPORT.getBeatAt(3).setSoundAt(0,SoundPoolWrapper.INAUDIBLE);
            SPORT.getBeatAt(4).subdivideBy(3);
            SPORT.getBeatAt(4).setSoundAt(0, SoundPoolWrapper.DEFAULT_SOUND);
            SPORT.getBeatAt(4).setSoundAt(1, SoundPoolWrapper.INAUDIBLE);
            SPORT.getBeatAt(4).setSoundAt(2, SoundPoolWrapper.DEFAULT_SOUND);
            SPORT.getBeatAt(5).subdivideBy(3);
            SPORT.getBeatAt(5).setSoundAt(0, SoundPoolWrapper.DEFAULT_SOUND);
            SPORT.getBeatAt(5).setSoundAt(1, SoundPoolWrapper.INAUDIBLE);
            SPORT.getBeatAt(5).setSoundAt(2, SoundPoolWrapper.DEFAULT_SOUND);
            SPORT.getBeatAt(6).subdivideBy(3);
            SPORT.getBeatAt(6).setSoundAt(0, SoundPoolWrapper.INAUDIBLE);
            SPORT.getBeatAt(6).setSoundAt(1, SoundPoolWrapper.DEFAULT_SOUND);
            SPORT.getBeatAt(6).setSoundAt(2, SoundPoolWrapper.INAUDIBLE);
            SPORT.getBeatAt(7).setSoundAt(0, SoundPoolWrapper.INAUDIBLE);
            mDao.insert(new RhythmEntity(3, "Sports Clap", SPORT));
            return null;
        }
    }
}