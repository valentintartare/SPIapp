package com.example.valentin.spiapplication;

import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Valentin on 27/03/2017.
 */

public class SetConnexionRest {
    boolean connexion=false;
    String message;

    public void appelRestUser(final String login, final String mdp,final MainActivity main, Response.Listener<String> listener, Response.ErrorListener errorListener){
        System.setProperty("http.proxyHost","cache.univ-lille1.fr");
        System.setProperty("http.proxyPort","3128");
        RequestQueue queue = Volley.newRequestQueue(main);
        String url="http://51.255.131.194/v1/secure/who";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,listener,errorListener) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Basic " + login + ":" + mdp);
                return headers;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
