package com.example.metrognome;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class TimestampConverter {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }
}
