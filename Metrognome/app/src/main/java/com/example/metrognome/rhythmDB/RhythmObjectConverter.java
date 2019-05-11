package com.example.metrognome.rhythmDB;

import android.arch.persistence.room.TypeConverter;

import com.example.metrognome.time.Rhythm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RhythmObjectConverter {
    @TypeConverter
    public Rhythm fromSerialrhythmObject(String value) {
        if (value == null){
            return null;
        }
        else {
            try {
                byte serialized[] = value.getBytes();
                ByteArrayInputStream in = new ByteArrayInputStream(serialized);
                ObjectInputStream oin = null;
                oin = new ObjectInputStream(in);
                Rhythm rhythm = (Rhythm) oin.readObject();
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
    public String rhythmObjectToSerial(Rhythm rhythm) {
        if (rhythm == null) {
            return null;
        } else {
            String serializedObject;
            try {
                System.out.println("serializing!!");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ObjectOutputStream ostr = new ObjectOutputStream(out);
                ostr.writeObject(rhythm);
                ostr.flush();
                serializedObject = out.toString();
                System.out.println(serializedObject);
                return serializedObject;
            } catch (Exception e) {
                System.out.println(e);
                return null;
            }
        }
    }
}