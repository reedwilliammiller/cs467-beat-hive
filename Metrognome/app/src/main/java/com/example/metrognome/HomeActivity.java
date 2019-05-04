package com.example.metrognome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class HomeActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        RecyclerView recyclerView = findViewById(R.id.recyclerviewrecent);
        final RecentRhythmListAdapter adapter = new RecentRhythmListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecentRhythmViewModel = ViewModelProviders.of(this).get(RecentRhythmViewModel.class);

        mRecentRhythmViewModel.getRecentRhythms().observe(this, new Observer<List<Rhythm>>() {
            @Override
            public void onChanged(@Nullable final List<Rhythm> rhythms) {
                // Update the cached copy of the words in the adapter.
                adapter.setRhythms(rhythms);
            }
        });
    }

    public void startPlayback(View view) {
        Intent startPlaybackActivity = new Intent(this, PlaybackActivity.class);
        startActivity(startPlaybackActivity);
    }
}
