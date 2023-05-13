package com.example.music_player;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class JukeboxActivity extends AppCompatActivity implements VolleyCallback {

    private ImageButton btn_play;
    private ImageButton btn_pause;
    private ImageButton btn_request;
    private TextView tv_status_view;
    private TextView url_value;
    private String[] songValues; // [0]: song title, [1]: song artist, [2]: song url
    private String song_title;
    private String song_artist;
    private String song_url;

    private SongRequester songRequester = new SongRequester();
    private MediaPlayer mediaPlayer = new MediaPlayer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jukebox_activity);
        this.btn_play = findViewById(R.id.btn_play);
        this.btn_pause = findViewById(R.id.btn_pause);
        this.btn_request = findViewById(R.id.btn_request);
        this.tv_status_view = findViewById(R.id.tv_status);
        this.url_value = findViewById(R.id.url_value);


        disableButton(this.btn_play);
        disableButton(this.btn_pause);

        this.btn_play.setOnClickListener((View view) -> {
            this.tv_status_view.setText(R.string.status_playing);
            disableButton(this.btn_play);
            enableButton(this.btn_pause);
            this.mediaPlayer.playSong(this.song_url, this, this);
        });

        this.btn_pause.setOnClickListener((View view) -> {
            pauseSong();
        });
        this.btn_request.setOnClickListener((View view) -> {
            requestButtonFirstActions();
            songRequester.requestSong(this, this);
        });
    }

    @Override
    public void onSuccess(String[] songValues) {
        this.song_title = songValues[0];
        this.song_artist = songValues[1];
        this.song_url = songValues[2];
        showSongDetails();
    }

    @Override
    public void onError(int error) {
        url_value.setText(error);
    }

    private void requestButtonFirstActions() {
        disableButton(this.btn_play);
        disableButton(this.btn_pause);
        disableButton(this.btn_request);
        this.tv_status_view.setText(R.string.status_requesting);
        this.mediaPlayer.stopSong();
    }

    private void requestButtonFinalActions() {
        enableButton(this.btn_pause);
        disableButton(this.btn_play);
        enableButton(this.btn_request);
        this.mediaPlayer.playSong(this.song_url, this, this);
    }

    private void disableButton(ImageButton button){
        button.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        button.setEnabled(false);
        button.setClickable(false);
    }
    private void enableButton(ImageButton button){
        button.getBackground().setColorFilter(null);
        button.setEnabled(true);
        button.setClickable(true);

    }

    public void pauseSong(){
        pauseSongVisual();
        this.mediaPlayer.pauseSong();
    }

    public void pauseSongVisual(){
        tv_status_view.setText(R.string.status_stopped);
        disableButton(btn_pause);
        enableButton(btn_play);
    }

    public void showSongDetails() {
        TextView artist_value = findViewById(R.id.artist_value);
        TextView song_value = findViewById(R.id.song_value);
        TextView url_value = findViewById(R.id.url_value);
        artist_value.setText(this.song_title);
        song_value.setText(this.song_artist);
        url_value.setText(this.song_url);

        requestButtonFinalActions();
    }



    public void closeJukeboxActivity() {
        JukeboxActivity.this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseSong();

    }

    @Override
    protected void onStop() {
        super.onStop();
        pauseSong();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        pauseSong();

    }
}