package com.example.metrognome.rhythmDB;

import android.arch.persistence.room.TypeConverter;
import android.os.Build;

import com.example.metrognome.time.Rhythm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class RhythmObjectConverter {
    @TypeConverter
    public Rhythm fromSerialrhythmObject(String value) {
        if (value == null){
            return null;
        }
        else {
            final byte[] bytes;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                System.out.println("De-serializing");
                bytes = Base64.getDecoder().decode(value);
                try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes); ObjectInput in = new ObjectInputStream(bis)) {
                    return (Rhythm) in.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            else return null;
        }
    }

    @TypeConverter
    public String rhythmObjectToSerial(Rhythm rhythm) {
        if (rhythm == null) {
            return null;
        } else {
            String serializedObject;
            try {
                System.out.println("serializing");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ObjectOutputStream ostr = new ObjectOutputStream(out);
                ostr.writeObject(rhythm);
                ostr.flush();
                final byte[] byteArray = out.toByteArray();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    return Base64.getEncoder().encodeToString(byteArray);
                }
                else return null;
            } catch (Exception e) {
                System.out.println(e);
                return null;
            }
        }
    }
}