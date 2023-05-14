package com.example.music_player;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class PlaylistActivity extends AppCompatActivity implements VolleyCallback{

    private RecyclerView recyclerView;
    private SongRequester songRequester = new SongRequester();
    private LinkedList<Song> songList;
    private SeekBar seekBar;

    private TextView song_duration_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_activity);

        recyclerView = findViewById(R.id.recyclerView);
        seekBar = findViewById(R.id.seekBar);
        seekBar.setEnabled(false);
        song_duration_tv = findViewById(R.id.song_duration_tv);

        songRequester.requestSongList(this, this);

    }

    @Override
    public void onSuccess(String[] response) {

    }

    @Override
    public void onSuccess(LinkedList<Song> songList) {
        this.songList = songList;

        SongAdapter songAdapter = new SongAdapter(this, this, songList, seekBar,
                song_duration_tv);
        recyclerView.setAdapter(songAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onError(int error) {

    }
}
