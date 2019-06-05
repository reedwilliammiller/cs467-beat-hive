package com.example.beathive.rhythmDB;

import android.arch.persistence.room.TypeConverter;

import com.example.beathive.rhythmProcessor.RhythmJSONConverter;
import com.example.beathive.time.Rhythm;

public class RhythmObjectConverter {

    @TypeConverter
    public Rhythm fromString(String value) {
        Rhythm rhythm = RhythmJSONConverter.fromJSON(value);
        return rhythm;
    }

    @TypeConverter
    public String fromRhythm(Rhythm rhythm) {
        String string = RhythmJSONConverter.toJSON(rhythm);
        return string;
    }
}