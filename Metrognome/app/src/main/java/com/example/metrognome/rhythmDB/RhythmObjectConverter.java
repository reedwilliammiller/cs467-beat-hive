package com.example.metrognome.rhythmDB;

import android.arch.persistence.room.TypeConverter;

import com.example.metrognome.rhythmProcessor.RhythmObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RhythmObjectConverter {
    @TypeConverter
    public RhythmObject fromSerialrhythmObject(String value) {
        if (value == null){
            return null;
        }
        else {
            try {
                byte serialized[] = value.getBytes();
                ByteArrayInputStream in = new ByteArrayInputStream(serialized);
                ObjectInputStream oin = null;
                oin = new ObjectInputStream(in);
                RhythmObject rhythm = (RhythmObject) oin.readObject();
                return rhythm;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @TypeConverter
    public String rhythmObjectToSerial(RhythmObject rhythm) {
        if (rhythm == null) {
            return null;
        } else {
            String serializedObject;
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ObjectOutputStream ostr = new ObjectOutputStream(out);
               ostr.writeObject(rhythm);
                ostr.flush();
                serializedObject = out.toString();
                return serializedObject;
            } catch (Exception e) {
                System.out.println(e);
                return null;
            }

        }
    }
}