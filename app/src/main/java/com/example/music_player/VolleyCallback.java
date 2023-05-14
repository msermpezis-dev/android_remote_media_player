package com.example.music_player;

import java.util.LinkedList;

public interface VolleyCallback{
    void onSuccess(String[] response);

    void onSuccess(LinkedList<Song> response);
    void onError(int error);
}
