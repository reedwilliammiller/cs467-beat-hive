package com.example.metrognome.rhythmDB;

import android.arch.persistence.room.TypeConverter;

import com.example.metrognome.rhythmProcessor.RhythmJSONConverter;
import com.example.metrognome.time.Rhythm;

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