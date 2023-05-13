package com.example.music_player;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class SongRequester extends AppCompatActivity {



    public void requestSong(final VolleyCallback callback, Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://mad.mywork.gr/get_song.php?t=1546";
        String[] songValues = new String[3];
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            callback.onSuccess(loadXMLFromString(response));
                        } catch (Exception e) {
                            Log.e("Volley Error", e.getMessage());
                            callback.onError(R.string.invalid_url);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getMessage() != null) {
                            Log.e("Volley Error", error.getMessage());
                            callback.onError(R.string.unknown_error);
                        } else {
                            Log.e("Volley Possible Timeout Error", "Unknown error occurred");
                        }
                    }
                });
        queue.add(stringRequest);
    }

    public String[] loadXMLFromString(String xml) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource inputSource = new InputSource(new StringReader(xml));
        Document doc = builder.parse(inputSource);
        String[] songValues = new String[3];
        if (doc.getElementsByTagName("status").item(0).getTextContent().equals("2-OK")
                && doc.getElementsByTagName("song").item(0).hasChildNodes()){
            songValues[0] = doc.getElementsByTagName("title").item(0).getTextContent();
            songValues[1] = doc.getElementsByTagName("artist").item(0).getTextContent();
            songValues[2] = doc.getElementsByTagName("url").item(0).getTextContent();
        }
        return songValues;
    }
}
