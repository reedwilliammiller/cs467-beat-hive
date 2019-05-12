package com.example.metrognome.rhythmDB;

import android.arch.persistence.room.TypeConverter;

import com.example.metrognome.rhythmProcessor.RhythmJSONConverter;
import com.example.metrognome.time.Rhythm;

public class RhythmObjectConverter {
        @TypeConverter
        public Rhythm fromString(String value) {
            System.out.println("from string " + value);
            return RhythmJSONConverter.fromJSON(value);
        }

        @TypeConverter
        public String fromRhythm(Rhythm rhythm) {
            String string = RhythmJSONConverter.toJSON(rhythm);
            System.out.println("to string " + string);
            return string;
        }
}