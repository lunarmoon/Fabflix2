package com.example.team24.fabflix;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        final Button button = (Button) findViewById(R.id.searchButton);
        final EditText searchText = (EditText) findViewById(R.id.searchText);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String title = searchText.getText().toString();
                if (title.equals("DEBUG")) {
                    Intent intent = new Intent(v.getContext(), SearchResults.class);
                    ArrayList<String> resultList = new ArrayList<String>();
                    ArrayList<String> resultList2 = new ArrayList<String>();
                    for (int i = 0; i < 44; i++) {
                        resultList.add("Movie " + i);
                        resultList2.add(""+755009);
                    }
                    intent.putStringArrayListExtra("movieTitles", resultList);
                    intent.putStringArrayListExtra("movieIds", resultList2);
                    startActivity(intent);
                }
                else
                    attemptSearch(title);


            }
        });
    }

    public void attemptSearch(String title)
    {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + getString(R.string.ip) + "/fabflix/servlet/searchAndroid?title=" + URLEncoder.encode(title);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        ArrayList<String> temp = new ArrayList<String>(Arrays.asList(response.split("\\r?\\n")));
                        ArrayList<String> titles = new ArrayList<String>();
                        ArrayList<String> ids = new ArrayList<String>();
                        if (!(temp.size() < 3 && (temp.get(0).equals("") || temp.get(0).contains("syntax error")))) {
                            for (String data : temp) {
                                String datatemp[] = data.split(";");
                                titles.add(datatemp[0]);
                                ids.add(datatemp[1]);
                            }
                        }
                        Intent intent = new Intent(getBaseContext(), SearchResults.class);
                        intent.putStringArrayListExtra("movieTitles", titles);
                        intent.putStringArrayListExtra("movieIds", ids);
                        startActivity(intent);
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
