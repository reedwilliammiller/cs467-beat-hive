package com.example.metrognome.rhythmDB;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface RhythmDao {
    @Insert
    long insert(RhythmEntity rhythmEntity);

    @Update
    void update(RhythmEntity... rhythmEntities);

    @Delete
    void delete(RhythmEntity... rhythmEntities);

    @Query("DELETE FROM rhythm_table")
    void deleteAll();

    @Query("SELECT * from rhythm_table ORDER BY opened DESC")
    LiveData<List<RhythmEntity>> getAllRhythms();

    @Query("SELECT * from rhythm_table ORDER BY opened DESC LIMIT 3")
    LiveData<List<RhythmEntity>> getRecentRhythms();

    @Query("SELECT * from rhythm_table WHERE ID = :id LIMIT 1")
    RhythmEntity getRhythmById(int id);

    @Query("UPDATE rhythm_table SET opened = :today WHERE ID = :id")
    void setLastOpened(int id, Date today);

    @Query("SELECT * from rhythm_table ORDER BY opened DESC LIMIT 1")
    RhythmEntity getRecentRhythm();
}
