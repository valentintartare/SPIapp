package com.example.valentin.spiapplication;

import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

/**
 * Created by Valentin on 27/03/2017.
 */

public class SetConnexionRest {
    boolean connexion=false;
    volatile String message;

    synchronized public void appelRestUser(final String login,final MainActivity main, Response.Listener<String> listener, Response.ErrorListener errorListener){
        RequestQueue queue = Volley.newRequestQueue(main);
        String url="http://51.255.131.194/v1/user";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,listener, errorListener);
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
