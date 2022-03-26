package com.codewithsaurabh.covid_19tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final static String URL ="https://api.rootnet.in/covid19-in/stats/latest";
    ProgressBar progressBar;
    RecyclerView recyclerViewStates;
    ArrayList<Model> statesArrayList;
    Button buttonStartFetching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statesArrayList=new ArrayList<>();
        progressBar=findViewById(R.id.progressBar);
        recyclerViewStates=findViewById(R.id.recyclerViewStates);
        buttonStartFetching=findViewById(R.id.buttonStartFetching);

        buttonStartFetching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                startFetching();
            }
        });
    }

    void startFetching()
    {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            buttonStartFetching.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);

                            JSONObject data=response.getJSONObject("data");
                            JSONArray stateJSONArray=data.getJSONArray("regional");
                            for(int i=0;i<stateJSONArray.length();i++)
                            {
                                JSONObject obj=stateJSONArray.getJSONObject(i);
                                String state=obj.getString("loc");
                                String recovered=obj.getString("discharged");
                                String deaths=obj.getString("deaths");
                                String confirmed=obj.getString("totalConfirmed");
                                statesArrayList.add(new Model(state,recovered,deaths,confirmed));
                            }

                            StateAdapter stateAdapter=new StateAdapter(statesArrayList);
                            recyclerViewStates.setAdapter(stateAdapter);
                            recyclerViewStates.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        }
                        catch(JSONException e)
                        {
                            Log.e("ResponseError",e.toString());
                        }
                        catch(Exception e)
                        {
                            Log.e("UnexpectedError",e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Failed to get data...", Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }
}