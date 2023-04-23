package com.example.music_player;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.music_player.databinding.ActivityMainBinding;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class JukeboxActivity extends AppCompatActivity {

    private Button btn_play;
    private Button btn_pause;
    private Button btn_request;
    private TextView tv_status_view;
    private final MediaPlayer mediaPlayer = new MediaPlayer();;
    private int mediaFileLengthInMilliseconds;
    private String[] songValues; // [0]: song title, [1]: song artist, [2]: song url


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jukebox_activity);
        this.btn_play = findViewById(R.id.btn_play);
        this.btn_pause = findViewById(R.id.btn_pause);
        this.btn_request = findViewById(R.id.btn_request);
        this.tv_status_view = findViewById(R.id.tv_status);

        disableButton(this.btn_play);
        disableButton(this.btn_pause);

        this.btn_play.setOnClickListener((View view) -> {
            playSong();
        });

        this.btn_pause.setOnClickListener((View view) -> {
            pauseSong();
        });
        this.btn_request.setOnClickListener((View view) -> {
            requestSong();
        });
    }

    public void requestSong() {

        requestButtonFirstActions();

        TextView url_value = findViewById(R.id.url_value);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://mad.mywork.gr/get_song.php?t=1546";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        url_value.setText(response);
                        try {
                            loadXMLFromString(response);
                        } catch (Exception e) {
                            Log.e("Volley Error", e.getMessage());
                            url_value.setText(R.string.invalid_url);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getMessage() != null) {
                            Log.e("Volley Error", error.getMessage());
                            url_value.setText(R.string.unknown_error);
                        } else {
                            Log.e("Volley Possible Timeout Error", "Unknown error occurred");
                        }
                    }
                });
        queue.add(stringRequest);
    }

    private void requestButtonFirstActions() {
        disableButton(this.btn_play);
        disableButton(this.btn_pause);
        disableButton(this.btn_request);
        this.tv_status_view.setText(R.string.status_requesting);
        stopSong();
    }

    private void requestButtonFinalActions() {
        enableButton(this.btn_play);
        disableButton(this.btn_pause);
        enableButton(this.btn_request);
        playSong();
    }

    private void disableButton(Button button){
        button.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        button.setEnabled(false);
        button.setClickable(false);
    }
    private void enableButton(Button button){
        button.getBackground().setColorFilter(null);
        button.setEnabled(true);
        button.setClickable(true);

    }

    public void loadXMLFromString(String xml) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource inputSource = new InputSource(new StringReader(xml));
        Document doc = builder.parse(inputSource);
        if (doc.getElementsByTagName("status").item(0).getTextContent().equals("2-OK")){
            showSongDetails(getSongValues(doc));
        }
    }

    private String[] getSongValues(Document doc) {
        String[] songValues = new String[3];

        if (doc.getElementsByTagName("song").item(0).hasChildNodes()){
            songValues[0] = doc.getElementsByTagName("title").item(0).getTextContent();
            songValues[1] = doc.getElementsByTagName("artist").item(0).getTextContent();
            songValues[2] = doc.getElementsByTagName("url").item(0).getTextContent();
            return songValues;
        } else {
            return null;
        }
    }

    public void showSongDetails(String[] songValues) {
        this.songValues = songValues;
        TextView artist_value = findViewById(R.id.artist_value);
        TextView song_value = findViewById(R.id.song_value);
        TextView url_value = findViewById(R.id.url_value);
        artist_value.setText(songValues[0]);
        song_value.setText(songValues[1]);
        url_value.setText(songValues[2]);

        requestButtonFinalActions();
    }

    public void playSong(){
        this.tv_status_view.setText(R.string.status_playing);
        disableButton(this.btn_play);
        enableButton(this.btn_pause);

        String url = songValues[2];
        this.mediaPlayer.setAudioAttributes(
                new AudioAttributes
                        .Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build());
        try {
            if (this.mediaFileLengthInMilliseconds == 0){
                this.mediaPlayer.setDataSource(url);
                this.mediaPlayer.prepare();
                this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer)
                    {
                        requestSong();
                    }
                });
                this.mediaPlayer.setLooping(true);
                this.mediaPlayer.start();
            } else {
                this.mediaPlayer.seekTo(this.mediaFileLengthInMilliseconds);
                this.mediaPlayer.start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void pauseSong(){
        this.tv_status_view.setText(R.string.status_stopped);
        disableButton(this.btn_pause);
        enableButton(this.btn_play);
        if(this.mediaPlayer.isPlaying()) {
            this.mediaFileLengthInMilliseconds = this.mediaPlayer.getCurrentPosition();
            this.mediaPlayer.pause();
        }
    }

    public void stopSong(){
        this.mediaPlayer.stop();
        this.mediaPlayer.reset();
        this.mediaFileLengthInMilliseconds = 0;
    }

    public void goToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}