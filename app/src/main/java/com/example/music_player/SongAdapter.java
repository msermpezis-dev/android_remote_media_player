package com.example.music_player;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    LayoutInflater layoutInflater;
    private List<Song> songList;

    public SongAdapter(Context context, LinkedList<Song> songList) {
        this.layoutInflater =  LayoutInflater.from(context);
        this.songList = songList;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.song_layout, parent, false);
        return new SongViewHolder(itemView);
    }

    // listen to the whole item in recycleview
    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songList.get(position);

        holder.song_title_tv.setText(song.getTitle());
        holder.artist_name_tv.setText(song.getArtist());
        holder.song_url_tv.setText(song.getUrl());

    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView song_title_tv;
        TextView artist_name_tv;
        TextView song_url_tv;
        ImageButton play_btn;
        ImageButton pause_btn;
        View gray_line_view;

        // Listens to item in recyclerview
        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            song_title_tv = itemView.findViewById(R.id.song_title_tv);
            artist_name_tv = itemView.findViewById(R.id.artist_name_tv);
            play_btn = itemView.findViewById(R.id.play_btn);
            gray_line_view = itemView.findViewById(R.id.gray_line_view);
            song_url_tv = itemView.findViewById(R.id.song_url_tv);
            pause_btn = itemView.findViewById(R.id.pause_btn);

            // Set click listeners for buttons if needed
            play_btn.setOnClickListener((View view) -> {
//                playSong();
            });

            pause_btn.setOnClickListener((View view) -> {
//                playSong();
            });
        }
    }
}