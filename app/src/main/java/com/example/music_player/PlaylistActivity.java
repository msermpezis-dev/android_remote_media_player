package com.example.music_player;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class PlaylistActivity extends AppCompatActivity implements VolleyCallback{
    private RecyclerView recyclerView;
    private SongRequester songRequester = new SongRequester();
    LinkedList<Song> songList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_activity);

        recyclerView = findViewById(R.id.recyclerView);

        songRequester.requestSongList(this, this);

    }

    @Override
    public void onSuccess(String[] response) {

    }

    @Override
    public void onSuccess(LinkedList<Song> songList) {
        this.songList = songList;

        SongAdapter songAdapter = new SongAdapter(this, songList);
        recyclerView.setAdapter(songAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onError(int error) {

    }
}
