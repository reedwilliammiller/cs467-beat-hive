package com.example.metrognome.rhythmDB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;



@Entity(tableName = "rhythm_table")
public class RhythmEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    @NonNull
    @ColumnInfo(name = "title")
        private String title;

    public RhythmEntity(int id, @NonNull String title, @NonNull String filename, @NonNull boolean asset) {
        this.id = id;
        this.title = title;
        this.filename = filename;
        this.asset = asset;
    }

    @NonNull
    public String getTitle(){return this.title;}

    @NonNull
    @ColumnInfo(name="filename")
        private String filename;


    @NonNull
    public String getFilename(){return this.filename;}

    @ColumnInfo(name="opened")
        private String opened;

    public String getOpened(){return this.opened;}

    @NonNull
    @ColumnInfo(name="asset")
        private Boolean asset;
    @NonNull
    public Boolean getAsset(){return this.asset;}


    public void setOpened(String opened){this.opened = opened;}
    public void setPath(String filename){this.filename = filename;}




}


