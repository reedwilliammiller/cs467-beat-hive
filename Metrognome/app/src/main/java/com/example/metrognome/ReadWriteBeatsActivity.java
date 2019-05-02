package com.example.metrognome;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;

public class ReadWriteBeatsActivity extends AppCompatActivity{

    public File getPrivateBeatStorageDir(Context context, String beatName) {
        File file = new File(context.getExternalFilesDir(
                null), beatName);
        if(!file.mkdirs()) {
            Log.e(null, "Directory not created");
        }
        return file;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

}
