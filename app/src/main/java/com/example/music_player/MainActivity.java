package com.example.music_player;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.music_player.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout jukeboxButton = findViewById(R.id.jukebox_btn);
        RelativeLayout playlistButton = findViewById(R.id.playlist_btn);

        jukeboxButton.setOnClickListener((View view) -> {
            goToJukeboxActivity(view);

        });

        playlistButton.setOnClickListener((View view) -> {
            goToPlaylistActivity(view);

        });
    }

    public void goToJukeboxActivity(View view) {
        Intent intent = new Intent(MainActivity.this, JukeboxActivity.class);
        startActivity(intent);
    }

    public void goToPlaylistActivity(View view) {
        Intent intent = new Intent(MainActivity.this, PlaylistActivity.class);
        startActivity(intent);
    }
}