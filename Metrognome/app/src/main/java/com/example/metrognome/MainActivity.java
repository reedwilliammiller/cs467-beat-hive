package com.example.metrognome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent startPlaybackActivity = new Intent(this, PlaybackActivity.class);
//        startActivity(startPlaybackActivity);
        Intent startPlaybackActivity = new Intent(this, EditorActivity.class);
        startActivity(startPlaybackActivity);
    }
}
