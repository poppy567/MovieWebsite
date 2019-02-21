package com.grp59.cs122b.fabflix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SingleMovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_movie);
        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();
        String msg = bundle.getString("message");
        Log.d("msg",msg);
        if (msg != null && !"".equals(msg)){
            try {
                JSONArray array = new JSONArray(msg);
//                for(int i=0;i<array.length();i++){
//
//                }
                JSONObject reader=array.getJSONObject(0);
                String title=reader.getString("movie_title");
                ((TextView)findViewById(R.id.movie_title)).setText(title);
                String year=reader.getString("movie_year");
                ((TextView)findViewById(R.id.movie_year)).setText(year);
                String director=reader.getString("movie_director");
                ((TextView)findViewById(R.id.movie_director)).setText(director);
                String genres=reader.getString("movie_genre");
                ((TextView)findViewById(R.id.movie_genres)).setText(genres);
                String stars=reader.getString("movie_star");
                ((TextView)findViewById(R.id.movie_stars)).setText(stars);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
