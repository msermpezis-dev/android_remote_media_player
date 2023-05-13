package com.example.music_player;

public interface VolleyCallback{
    void onSuccess(String[] response);
    void onError(int error);
}
