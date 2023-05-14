package com.example.music_player;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> {

    static android.media.MediaPlayer mediaPlayer;
    private Activity activity;

    private LayoutInflater layoutInflater;
    private List<Song> songList;
    private int currentPlayingPosition;

    private final SeekBarUpdater seekBarUpdater;

    private Context context;
    private MyViewHolder playingHolder;

    private SeekBar seekBar;

    private TextView song_duration_tv;

    public SongAdapter(Activity activity, Context context, LinkedList<Song> songList,
                       SeekBar seekBar, TextView textView) {
        this.songList = songList;
        this.context = context;
        this.currentPlayingPosition = -1;
        this.seekBarUpdater = new SeekBarUpdater();
        this.activity = activity;
        this.seekBar = seekBar;
        this.song_duration_tv = textView;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.song_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Song song = songList.get(position);

        holder.song_title_tv.setText(song.getTitle());
        holder.artist_name_tv.setText(song.getArtist());
        holder.song_url_tv.setText(song.getUrl());
        if (position == currentPlayingPosition) {
            playingHolder = holder;
            updatePlayingView();
        } else {
            updateNonPlayingView(holder);
        }
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    private void updateNonPlayingView(MyViewHolder holder) {
        seekBar.removeCallbacks(seekBarUpdater);
        seekBar.setEnabled(false);
        seekBar.setProgress(0);
        holder.play_btn.setVisibility(View.VISIBLE);
        holder.pause_btn.setVisibility(View.GONE);
    }

    private void updatePlayingView() {
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        seekBar.setEnabled(true);
        if (mediaPlayer.isPlaying()) {
            seekBar.postDelayed(seekBarUpdater, 100);
            playingHolder.play_btn.setVisibility(View.GONE);
            playingHolder.pause_btn.setVisibility(View.VISIBLE);
        } else {
            seekBar.removeCallbacks(seekBarUpdater);
            playingHolder.play_btn.setVisibility(View.VISIBLE);
            playingHolder.pause_btn.setVisibility(View.GONE);
        }
    }

    private class SeekBarUpdater implements Runnable {
        @Override
        public void run() {
            if (null != playingHolder && null != mediaPlayer) {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                seekBar.postDelayed(this, 100);
                long  current_position = mediaPlayer.getCurrentPosition();
                long  total = mediaPlayer.getDuration();
                String formatted_duration = getTimeString(current_position) + " / " +  getTimeString(total);
                song_duration_tv.setText(formatted_duration);
            }
        }
    }
    private String getTimeString(long millis) {

        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

        return String.format(Locale.ENGLISH,"%02d", minutes) + ":"
                + String.format(Locale.ENGLISH,"%02d", seconds);
    }
    private void playNextSong() {
        int nextPosition = currentPlayingPosition + 1;
        if (nextPosition < songList.size()) {
            currentPlayingPosition = nextPosition;
            Song nextSong = songList.get(nextPosition);
            playSong(nextSong.getUrl());
            notifyItemChanged(nextPosition);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, SeekBar.OnSeekBarChangeListener{
        TextView song_title_tv;
        TextView artist_name_tv;
        TextView song_url_tv;
        ImageButton play_btn;
        ImageButton pause_btn;

        // Listens to item in recyclerview
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            song_title_tv = itemView.findViewById(R.id.song_title_tv);
            artist_name_tv = itemView.findViewById(R.id.artist_name_tv);
            play_btn = itemView.findViewById(R.id.play_btn);
            song_url_tv = itemView.findViewById(R.id.song_url_tv);
            pause_btn = itemView.findViewById(R.id.pause_btn);

            // Set click listeners for buttons if needed
            play_btn.setOnClickListener(this);

            pause_btn.setOnClickListener(this);

            seekBar.setOnSeekBarChangeListener(this);

        }

        @Override
        public void onClick(View v) {
            if (getAbsoluteAdapterPosition() == currentPlayingPosition) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                } else {
                    if (mediaPlayer != null)
                        mediaPlayer.start();
                }
            } else {
                currentPlayingPosition = getAbsoluteAdapterPosition();
                if (mediaPlayer != null) {
                    if (null != playingHolder) {
                        updateNonPlayingView(playingHolder);
                    }
                    mediaPlayer.release();
                }
                playingHolder = this;

                playSong(song_url_tv.getText().toString());


            }
            if (mediaPlayer != null)
                updatePlayingView();
        }


        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                mediaPlayer.seekTo(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }
    private void playSong(String url) {
        mediaPlayer = new android.media.MediaPlayer();
        try {
            mediaPlayer.setAudioAttributes(
                new AudioAttributes
                        .Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build());
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e ) {
            e.printStackTrace();
        }
        mediaPlayer.setOnCompletionListener(new android.media.MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(android.media.MediaPlayer mediaPlayer) {
                    releaseMediaPlayer();
                    playNextSong();
                }
            });

    }
    private void releaseMediaPlayer() {
        if (null != playingHolder) {
            updateNonPlayingView(playingHolder);
        }

        mediaPlayer.release();
        mediaPlayer = null;
//        currentPlayingPosition = -1;
    }

}