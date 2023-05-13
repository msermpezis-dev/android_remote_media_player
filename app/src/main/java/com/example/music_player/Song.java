package com.example.music_player;

public class Song {

    private String title;

    private String artist;

    private String url;

    public Song(String title, String artist, String url){
        this.title = title;
        this.artist = artist;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }
}
