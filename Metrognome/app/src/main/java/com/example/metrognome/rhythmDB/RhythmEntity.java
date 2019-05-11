package com.example.metrognome.rhythmDB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.example.metrognome.rhythmProcessor.RhythmObject;

import java.util.Date;


@Entity(tableName = "rhythm_table")
@TypeConverters({TimestampConverter.class, RhythmObjectConverter.class})
public class RhythmEntity {
    public RhythmEntity(int id, @NonNull String title, RhythmObject rhythm) {
        this.id = id;
        this.title = title;
        this.rhythm = rhythm;
        this.opened = new Date();
    }

    @PrimaryKey(autoGenerate = true)
    private int id;
    public int getId() {
        return id;
    }

    @NonNull
    private String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    private Date opened;
    public Date getOpened() {
        return opened;
    }
    public void setOpened(Date opened) {
        this.opened = opened;
    }

    private RhythmObject rhythm;
    public RhythmObject getRhythm() {
        return rhythm;
    }
    public void setRhythm(RhythmObject rhythm) {
        this.rhythm = rhythm;
    }
}


