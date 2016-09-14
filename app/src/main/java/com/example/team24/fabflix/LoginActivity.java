package com.example.team24.fabflix;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Button button = (Button) findViewById(R.id.loginButton);
        final EditText usernameText = (EditText) findViewById(R.id.username);
        final EditText passwordText = (EditText) findViewById(R.id.password);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();

                attemptLogin(username, password);

                if (username.equals("DEBUG")) {
                    Intent intent = new Intent(v.getContext(), SearchActivity.class);
                    intent.putExtra("LoginName", username);
                    startActivity(intent);
                }
            }
        });
    }

    public void attemptLogin(String username, String password)
    {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + getString(R.string.ip) + "/fabflix/servlet/loginAndroid?username=" + URLEncoder.encode(username) + "&password=" + URLEncoder.encode(password);
        final TextView loginResults = (TextView) findViewById(R.id.loginResults);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        loginResults.setText("'" + response + "'");

                        if (response.equals("SUCCESS\n")) {
                            Intent intent = new Intent(loginResults.getContext(), SearchActivity.class);
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loginResults.setText(error.getMessage());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
