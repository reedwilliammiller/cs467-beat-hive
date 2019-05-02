package com.example.metrognome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.metrognome.editor.EditorAdapter;
import com.example.metrognome.time.Beat;

public class EditorActivity extends AppCompatActivity {

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
        rv = findViewById(R.id.recycler);
        lm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(lm);
        adapter = new EditorAdapter(this, new Beat[]{new Beat(), new Beat()});
        rv.setAdapter(adapter);
    }
}
