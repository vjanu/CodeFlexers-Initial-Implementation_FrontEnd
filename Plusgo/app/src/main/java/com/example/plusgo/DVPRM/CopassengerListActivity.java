/*
 * *
 *  * Created by Ashane Edirisinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/18/19 1:48 PM
 *
 */

package com.example.plusgo.DVPRM;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.plusgo.BaseContent;
import com.example.plusgo.R;
import com.example.plusgo.DVPRM.adapters.RecyclerViewAdapter;
import com.example.plusgo.DVPRM.model.copassenger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CopassengerListActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    BaseContent BASECONTENT = new BaseContent();
    //TODO:CHANGE THIS ADDRESS TO RETRIEVE DATA FROM VIEW
//    private final String JSON_URL= "https://gist.githubusercontent.com/AshaneEdiri/3151daee1b96041e6e7e690425e69e3b/raw/80c053c4566f06a2aa2ad1e7688df75c0b54541b/teacherListDummy";
    private final String JSON_URL= BASECONTENT.IpAddress+"/ratings/copassengerslist/";
    private String COOP_JSON_URL;
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private List<copassenger> lstCopassengers;
    private RecyclerView recyclerView;
    Button donebtn;
    SharedPreferences sharedpreferences2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copassenger_list);

        DisplayMetrics dm1 = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm1);

        int width = dm1.widthPixels;
        int height = dm1.heightPixels;

        getWindow().setLayout((int)(width*.80),(int)(height*.70));

        lstCopassengers = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerviewid);
        this.jsonrequest();

        donebtn =(Button)findViewById(R.id.done_coop_rating);
        donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        sharedpreferences = getSharedPreferences("rating_preference", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove("selectedKey").apply();
    }

    private void jsonrequest() {

        sharedpreferences2 = getSharedPreferences("rating_preference", Context.MODE_PRIVATE);
        String tempTripID = sharedpreferences2.getString("TripId", "O1558711443513");

        COOP_JSON_URL = JSON_URL + tempTripID;
        Log.e("JSONREQUEST","started");
        request = new JsonArrayRequest(COOP_JSON_URL, new Response.Listener<JSONArray>() {

            public void onResponse(JSONArray response) {
                Log.e("JSON_URL",COOP_JSON_URL);
                JSONObject jsonObject = null;
                for(int i= 0; i<response.length(); i++){
                    try{
                        Log.e("JSONREQUEST","INSIDE JSONEOBJECT");
                        jsonObject = response.getJSONObject(i);
                        copassenger teach = new copassenger();
                        teach.setUserId(jsonObject.getString("userId"));
                        teach.setName(jsonObject.getString("name"));
//                        teach.setImage_url(jsonObject.getString("img"));
                        teach.setImage_url(BASECONTENT.IpAddress+jsonObject.getString("img"));

                        lstCopassengers.add(teach);

                    }catch (JSONException e){
                        e.printStackTrace();
                        Log.e("JSONREQUEST","ERROR");
                    }
                }
                setuprecyclerview(lstCopassengers);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(CopassengerListActivity.this);
        requestQueue.add(request);
    }

    private void setuprecyclerview(List<copassenger> lstCopassengers) {

        Log.e("setuprecyclerview","started");
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(CopassengerListActivity.this,lstCopassengers);
        recyclerView.setLayoutManager(new LinearLayoutManager(CopassengerListActivity.this));

        recyclerView.setAdapter(myAdapter);
    }
}
