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
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public void updateWelcomeMessage() {

        String username = "EMULATOR DEVICE";
        String current_datetime = new Date().toString();
        String newText = "Hello " + username + ", \n your last login was at " + current_datetime;
        TextView myTextView = findViewById(R.id.welcome_message);
        myTextView.setText(newText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateWelcomeMessage();

        Button jukeboxButton = findViewById(R.id.jukebox_button);

        jukeboxButton.setOnClickListener((View view) -> {
            goToJukeboxActivity(view);

        });
    }

    public void goToJukeboxActivity(View view) {
        Intent intent = new Intent(MainActivity.this, JukeboxActivity.class);
        startActivity(intent);
    }
}