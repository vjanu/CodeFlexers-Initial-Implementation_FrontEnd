/*
 * *
 *  * Created by Ashane Edirisinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/18/19 1:48 PM
 *
 */

package com.example.plusgo.dvprm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.plusgo.BaseContent;
import com.example.plusgo.R;
import com.example.plusgo.dvprm.adapters.RecyclerViewAdapter;
import com.example.plusgo.dvprm.model.copassenger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CopassengerListActivity extends AppCompatActivity {

    BaseContent BASECONTENT = new BaseContent();
    private final String JSON_URL= "https://gist.githubusercontent.com/AshaneEdiri/3151daee1b96041e6e7e690425e69e3b/raw/4aff7b9d4db212826284949174b6e77856be4f5d/teacherListDummy";
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private List<copassenger> lstCopassengers;
    private RecyclerView recyclerView;

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
    }

    private void jsonrequest() {
        Log.e("JSONREQUEST","started");
        request = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {

            public void onResponse(JSONArray response) {
                Log.e("JSON_URL",JSON_URL);
                JSONObject jsonObject = null;
                for(int i= 0; i<response.length(); i++){
                    try{
                        Log.e("JSONREQUEST","INSIDE JSONEOBJECT");
                        jsonObject = response.getJSONObject(i);
                        copassenger teach = new copassenger();
//                        teach.setTeacherid(jsonObject.getString("teacherId"));
                        teach.setName(jsonObject.getString("name"));
//                        teach.setPosition(jsonObject.getString("Position"));
//                        teach.setFaculty(jsonObject.getString("Faculty"));
//                        teach.setSpecialization(jsonObject.getString("Specialization"));
//                        teach.setDescription(jsonObject.getString("Description"));
//                        teach.setExperience(jsonObject.getString("Experience"));
//                        if(jsonObject.getString("Rating").equals("null")){
//                            teach.setRating("0.0");
//                        }else if(jsonObject.getString("Rating").length()<=1){
//                            teach.setRating(jsonObject.getString("Rating")+".0");
//                        }else{
//                            teach.setRating(jsonObject.getString("Rating"));
//                        }

                        teach.setImage_url(jsonObject.getString("img"));

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
