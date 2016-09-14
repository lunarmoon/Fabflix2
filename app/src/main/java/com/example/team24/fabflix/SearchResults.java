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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchResults extends AppCompatActivity {

    ArrayList<String> movieTitles;
    ArrayList<String> movieIds;
    int pageIndex;
    int max;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        movieTitles = intent.getStringArrayListExtra("movieTitles");
        movieIds =  intent.getStringArrayListExtra("movieIds");
        pageIndex = 0;
        max = movieTitles.size() / 10;

        populateList();

        Button nextButton = (Button) findViewById(R.id.nextButton);
        Button prevButton = (Button) findViewById(R.id.prevButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (pageIndex < max) {
                    pageIndex++;
                    populateList();
                }
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (pageIndex > 0)
                {
                    pageIndex--;
                    populateList();
                }
            }
        });


        final ListView resultListView = (ListView) findViewById(R.id.resultListView);
        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String item = ((TextView)view).getText().toString();
                int itemPosition = position + pageIndex*10;
                Intent intent = new Intent(getBaseContext(), MoviePage.class);
                intent.putExtra("movieId", movieIds.get(itemPosition));
                startActivity(intent);

            }


        });
    }

    public void populateList()
    {
        ListView resultListView = (ListView) findViewById(R.id.resultListView);

        ArrayList<String> temp = new ArrayList<String>();
        int numItems = 10;
        if (pageIndex == max)
            numItems = movieTitles.size() - (max*10);

        for(int i = 0; i < numItems; i++){
            temp.add(movieTitles.get((10*pageIndex) +i));
        }

        // fill in the grid_item layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, temp);
        if (temp.size() > 0)
            resultListView.setAdapter(adapter);
    }

}
