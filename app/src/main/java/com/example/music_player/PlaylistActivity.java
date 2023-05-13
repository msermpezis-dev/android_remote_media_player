package com.example.music_player;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class PlaylistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_activity);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinkedList<Song> SongList = new LinkedList<>();

        for (int i=0;i<10;i++){
            SongList.add(new Song("title", "author", "url"));
        }

        SongAdapter songAdapter = new SongAdapter(this, SongList);
        recyclerView.setAdapter(songAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));







    }
}
