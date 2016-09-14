package com.example.team24.fabflix;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

public class StarPage extends AppCompatActivity {

    ArrayList<String> movieData;
    ArrayList<String> movieIds;
    int movieStart=5;
    int movieEnd=5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String id = intent.getStringExtra("starId");

        getData(id);

        final ListView starInfoList = (ListView) findViewById(R.id.starInfoList);
        starInfoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                if (position >= movieStart && position < movieEnd) {
                    String starIdToSend = movieIds.get(position - movieStart);

                    Intent intent = new Intent(getBaseContext(), MoviePage.class);
                    intent.putExtra("movieId", starIdToSend);
                    startActivity(intent);
                }
            }


        });
    }

    public void getData(String id)
    {
// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + getString(R.string.ip) + "/fabflix/servlet/starAndroid?star_id=" + URLEncoder.encode(id);
        final ListView starInfo = (ListView) findViewById(R.id.starInfoList);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string
                        ArrayList<String> finalInfos = new ArrayList<String>();
                        if (!response.contains("FAILED")) {
                            ArrayList<String> temp = new ArrayList<String>(Arrays.asList(response.split(";")));

                            finalInfos.add("First Name: " + temp.get(0));
                            finalInfos.add("Last Name: " + temp.get(1));
                            finalInfos.add("DOB: " + temp.get(2));
                            finalInfos.add("Star ID: " + temp.get(3));
                            finalInfos.add("Movies");

                            ArrayList<String> movieNames = new ArrayList<String>(Arrays.asList(temp.get(4).split("\\*")));
                            movieIds = new ArrayList<String>(Arrays.asList(temp.get(5).split(",")));

                            for (String st : movieNames) {
                                finalInfos.add("\t\t" + st);
                                movieEnd++;
                            }

                            Picasso.with(getBaseContext()).load(temp.get(6)).into((ImageView) findViewById(R.id.starImage));
                        }
                        else
                            finalInfos.add("Star could not be found");

                        // fill in the grid_item layout
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, finalInfos);
                        starInfo.setAdapter(adapter);
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
