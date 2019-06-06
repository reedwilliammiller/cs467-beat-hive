package com.example.beathive.rhythmDB;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.example.beathive.rhythmProcessor.RhythmJSONConverter;
import com.example.beathive.time.Rhythm;

import java.util.Date;



@Entity(tableName = "rhythm_table")
@TypeConverters({TimestampConverter.class, RhythmObjectConverter.class})
public class RhythmEntity {
    public RhythmEntity(int id, @NonNull String title, Rhythm rhythm) {
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

    private Rhythm rhythm;
    public Rhythm getRhythm() {
        return rhythm;
    }

    public void setRhythm(Rhythm rhythm) {
        this.rhythm = rhythm;
    }

    public String getRhythmString() {
        return RhythmJSONConverter.toJSON(rhythm);
    }

    public void setRhythmString(String rhythmString){
        this.setRhythm(RhythmJSONConverter.fromJSON(rhythmString));
    }

}


