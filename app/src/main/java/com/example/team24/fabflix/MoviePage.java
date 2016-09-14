package com.example.team24.fabflix;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

public class MoviePage extends AppCompatActivity {

    ArrayList<String> movieData;
    ArrayList<String> starIds;
    int starStart=5;
    int starEnd=5;
    String url_string = "";
    ImageView img;
    Bitmap bm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        img = (ImageView) findViewById(R.id.movieImage);

        Intent intent = getIntent();
        String id = intent.getStringExtra("movieId");

        getData(id);

        final ListView movieInfoList = (ListView) findViewById(R.id.movieInfoList);
        movieInfoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                if (position >= starStart && position < starEnd) {
                    String starIdToSend = starIds.get(position - starStart);

                    Intent intent = new Intent(getBaseContext(), StarPage.class);
                    intent.putExtra("starId", starIdToSend);
                    startActivity(intent);
                }
            }


        });


    }

    public void getData(String id)
    {
// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + getString(R.string.ip) + "/fabflix/servlet/movieAndroid?movie_id=" + URLEncoder.encode(id);
        final ListView movieInfo = (ListView) findViewById(R.id.movieInfoList);
        Log.d("URL", url);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string
                        ArrayList<String> finalInfos = new ArrayList<String>();

                        if (!response.contains("FAILED")) {

                            ArrayList<String> temp = new ArrayList<String>(Arrays.asList(response.split("[;]")));
                            finalInfos.add("Title: " + temp.get(0));
                            finalInfos.add("Year: " + temp.get(1));
                            finalInfos.add("Director: " + temp.get(2));
                            finalInfos.add("Movie ID: " + temp.get(3));
                            finalInfos.add("Stars");

                            ArrayList<String> starNames = new ArrayList<String>(Arrays.asList(temp.get(4).split("[,]")));
                            starIds = new ArrayList<String>(Arrays.asList(temp.get(5).split("[,]")));

                            for (String st : starNames) {
                                finalInfos.add("\t\t" + st);
                                starEnd++;
                            }

                            finalInfos.add("Genres");
                            for (String ge : new ArrayList<String>(Arrays.asList(temp.get(6).split("[,]")))) {
                                finalInfos.add("\t\t" + ge);
                            }
                            url_string = temp.get(7);

                            Picasso.with(getBaseContext()).load(url_string).into(img);

                            finalInfos.add("Trailer: " + temp.get(8));
                        }
                        else
                            finalInfos.add("Movie could not be found");

                        // fill in the grid_item layout
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, finalInfos);
                        movieInfo.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //loginResults.setText("failed to connect to server");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
