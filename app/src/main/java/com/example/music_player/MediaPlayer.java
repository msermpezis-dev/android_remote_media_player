package com.example.music_player;

import android.content.Context;
import android.media.AudioAttributes;

import java.io.IOException;

public class MediaPlayer {

    private SongRequester songRequester = new SongRequester();

    private final android.media.MediaPlayer mediaPlayer = new android.media.MediaPlayer();
    private int mediaFileLengthInMilliseconds;

    public void playSong(String url, final VolleyCallback callback, Context context){

        this.mediaPlayer.setAudioAttributes(
                new AudioAttributes
                        .Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build());
        try {
            if (this.mediaFileLengthInMilliseconds == 0){
                this.mediaPlayer.setDataSource(url);
                this.mediaPlayer.prepare();
                this.mediaPlayer.setOnCompletionListener(new android.media.MediaPlayer.OnCompletionListener()
                {
                    @Override
                    public void onCompletion(android.media.MediaPlayer mediaPlayer)
                    {
                        songRequester.requestSong(callback, context);
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
}
