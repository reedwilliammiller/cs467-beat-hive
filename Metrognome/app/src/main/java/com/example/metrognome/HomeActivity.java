package com.example.metrognome;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.metrognome.rhythmDB.RecentRhythmListAdapter;
import com.example.metrognome.rhythmDB.RecentRhythmViewModel;
import com.example.metrognome.rhythmDB.RhythmEntity;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecentRhythmViewModel mRecentRhythmViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        RecyclerView recyclerView = findViewById(R.id.recyclerviewrecent);
        final RecentRhythmListAdapter adapter = new RecentRhythmListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecentRhythmViewModel = ViewModelProviders.of(this).get(RecentRhythmViewModel.class);
        mRecentRhythmViewModel.getRecentRhythms().observe(this, new Observer<List<RhythmEntity>>() {
            @Override
            public void onChanged(@Nullable final List<RhythmEntity> rhythmEntities) {
                // Update the cached copy of the rhythms in the adapter.
                adapter.setRhythms(rhythmEntities);
            }
        });
    }

    public void startSelection(View view) {
        Intent startSelectionActivity = new Intent(this, SelectionActivity.class);
        startActivity(startSelectionActivity);
    }

    public void startEditor(View view) {
        Intent startEditorActivity = new Intent(this, EditorActivity.class);
        startActivity(startEditorActivity);
    }
}
