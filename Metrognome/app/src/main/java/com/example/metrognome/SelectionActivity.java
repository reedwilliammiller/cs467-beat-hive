package com.example.metrognome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Selection;
import android.util.Log;
import android.view.View;

import java.io.File;

public class SelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
    }

    public void startPlayback(View view) {
        Intent startSelectionActivity = new Intent(this, SelectionActivity.class);
        startActivity(startSelectionActivity);
    }

}
