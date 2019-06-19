/*
 * *
 *  * Created by Viraj Wickramasinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/20/19 12:16 AM
 *
 */

package com.example.plusgo.UPM;/*
 * *
 *  * Created by Viraj Wickramasinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/1/19 11:28 PM
 *
 */


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.request.RequestOptions;
import com.example.plusgo.Adapters.DriverListAdapter;
import com.example.plusgo.BaseContent;
import com.example.plusgo.Login;
import com.example.plusgo.R;
import com.example.plusgo.Utility.DriverListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DriverListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private static final String KEY_EMPTY = "";
    private RecyclerView.Adapter adapter;
    private List<DriverListItem> driverListItems;
    private TextView rate, count, welcome;
    private ImageButton btnLogout;
    BaseContent BASECONTENT = new BaseContent();
    private String id;
//    private ImageView profileImage;
//    private String JSON_URL = BASECONTENT.IpAddress + "/comments/specific/";
    private String JSON_URL = "https://gist.githubusercontent.com/vjanu/4720773fad79534af4460be44002789e/raw/f9125dfa3f728c91ab62ac5c5b7064e2808de2f5/drivers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_list);

        //set shared preference
//        SharedPreferences mPrefs = getSharedPreferences("teacherStore",MODE_PRIVATE);
//        id = mPrefs.getString("TeacherId", null);
//        String user = mPrefs.getString("TeacherUserName", null);
//
//        welcome = findViewById(R.id.welcome1);
//        welcome.setText("Signed as "+user);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerviewid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        btnLogout = (ImageButton) findViewById(R.id.logout1);


        driverListItems = new ArrayList<>();
        //initiate methods
        loadDriverDetails();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }
    //load all relevant driver details from the database
    private void loadDriverDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Drivers");
        progressDialog.show();

        try {
            JsonArrayRequest stringRequest = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {

                public void onResponse(JSONArray response) {
                    Log.d("qqq1", response.toString());
                    progressDialog.dismiss();
                    RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.user2).error(R.drawable.user2);
                    JSONObject jsonObject = null;
                    for(int i= 0; i<response.length(); i++){
                        try{
                            jsonObject = response.getJSONObject(i);
                           DriverListItem item = new DriverListItem(jsonObject.getString("TripID"),jsonObject.getString("UserID"),jsonObject.getString("Name"), jsonObject.getString("Start"),
                                    jsonObject.getString("Dest"), jsonObject.getString("Vehicle"), Double.parseDouble(jsonObject.getString("Rate")),
                                    jsonObject.getString("img"),
                                    jsonObject.getString("Time"));

                            driverListItems.add(item);

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    adapter = new DriverListAdapter(driverListItems, getApplicationContext());
                    recyclerView.setAdapter(adapter);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Log.d("qqq", error.toString());
                    Toast.makeText(DriverListActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)); //set delay of the server
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //destroy the session created
    private void logout(){
        finish();
        Intent login = new Intent(DriverListActivity.this, Login.class);
        startActivity(login);

    }
    @Override
    public void onBackPressed() {
        logout();
    }
}
