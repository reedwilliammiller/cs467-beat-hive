package com.example.metrognome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.metrognome.editor.MeasureAdapter;
import com.example.metrognome.time.Rhythm;

public class EditorActivity extends AppCompatActivity {
    Rhythm rhythm = Rhythm.RUMBA_CLAVE;
    RecyclerView rv;
    RecyclerView.LayoutManager lm;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        init();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {}
    }

    private void init() {
        rv = findViewById(R.id.recycler_view_measure);
        lm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(lm);
        adapter = new MeasureAdapter(this, rhythm);
        rv.setAdapter(adapter);
    }
}
