package com.example.metrognome;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RhythmDao {

    @Insert
    void insert(Rhythm rhythm);

    @Query("DELETE FROM rhythm_table")
    void deleteAll();

    @Query("SELECT * from rhythm_table ORDER BY opened DESC")
    LiveData<List<Rhythm>> getAllRhythms();

    @Query("SELECT * from rhythm_table ORDER BY opened DESC LIMIT 3")
    LiveData<List<Rhythm>> getRecentRhythms();
}
