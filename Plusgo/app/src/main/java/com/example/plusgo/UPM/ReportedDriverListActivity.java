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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.plusgo.Adapters.DriverListAdapter;
import com.example.plusgo.Adapters.ReportedDriverListAdapter;
import com.example.plusgo.BaseContent;
import com.example.plusgo.Login;
import com.example.plusgo.OPR.MainActivity;
import com.example.plusgo.R;
import com.example.plusgo.Utility.DriverListItem;
import com.example.plusgo.Utility.ReportedDriverListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class ReportedDriverListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private static final String KEY_EMPTY = "";
    private RecyclerView.Adapter adapter;
    private List<ReportedDriverListItem> driverListItems;
    private List<String> drivers;
    private TextView rate, count, welcome, noDrivers;
    private ImageButton btnLogout;
    BaseContent BASECONTENT = new BaseContent();
    private String id;
    private String uid;
    private TempGPSActivity tempGPSActivity;
    private String URL_DRIVERS = BASECONTENT.IpAddress + "/spouse/";
    private String URL_PUID = BASECONTENT.IpAddress + "/spouse/specific/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reported_driver_list);

        SharedPreferences user = getSharedPreferences("userStore",MODE_PRIVATE);
        uid = user.getString("UId", null);


        recyclerView = (RecyclerView)findViewById(R.id.recyclerviewid1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnLogout = (ImageButton) findViewById(R.id.logout1);


        driverListItems = new ArrayList<>();
        //initiate methods
        getPassengerID();



        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }


    //load all relevant driver details from the database
    private void loadDriverDetails(String puid) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Drivers");
        progressDialog.show();

        try {
            JsonArrayRequest stringRequest = new JsonArrayRequest(URL_DRIVERS+puid, new Response.Listener<JSONArray>() {

                public void onResponse(JSONArray response) {
                    Log.d("qqq1", response.toString());
                    progressDialog.dismiss();
                    RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.user2).error(R.drawable.user2);
                    JSONObject jsonObject = null;
                    for(int i= 0; i<response.length(); i++){
                        try{

                            DecimalFormat dc = new DecimalFormat("#0.00");
                            jsonObject = response.getJSONObject(i);
                            ReportedDriverListItem item = new ReportedDriverListItem(
                                    jsonObject.getString("PUID"),
                                    jsonObject.getString("Fullname"),
                                    String.format("Rs.%.2f",jsonObject.getDouble("Amt")),
                                    Long.parseLong(jsonObject.getString("Count")),
                                    jsonObject.getString("img"),
                                    jsonObject.getString("DUID")
                            );

                            driverListItems.add(item);

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    adapter = new ReportedDriverListAdapter(driverListItems, getApplicationContext());
                    recyclerView.setAdapter(adapter);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
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

//    private void getPassengerID(){
//        Log.i("getPassengerID", "getPassengerID");
//        Log.i("ID", uid);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PUID+uid, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.i("spouseID", response);
////                loadDriverDetails(response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("LOG_VOLLEY", error.toString());
//            }
//        }) ;
//
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                10000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.add(stringRequest);
//    }

    private void getPassengerID() {
        Log.i("getPassengerID", "getPassengerID");
        Log.i("ID", uid);
        Log.e("JSONREQUEST","started");
        JsonArrayRequest request = new JsonArrayRequest(URL_PUID+uid, new Response.Listener<JSONArray>() {

            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for(int i= 0; i<response.length(); i++){
                    try{
                        jsonObject = response.getJSONObject(i);
                        if(jsonObject.length()!=0) {
                            Log.i("jsonObject", jsonObject.toString());
                            loadDriverDetails(jsonObject.getString("PUID"));


                        }

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue = Volley.newRequestQueue(ReportedDriverListActivity.this);
        requestQueue.add(request);

    }


    //destroy the session created
    private void logout(){
        SharedPreferences sharedPreferences = getSharedPreferences("userStore", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear().commit();

        SharedPreferences user = getSharedPreferences("userStore",MODE_PRIVATE);
        boolean uid = user.getBoolean("Islogin", false);
        Log.d("login2:", String.valueOf(uid));

        finish();
        Intent login = new Intent(ReportedDriverListActivity.this, Login.class);
        startActivity(login);

    }
    @Override
    public void onBackPressed() {
        finish();
        Intent map = new Intent(ReportedDriverListActivity.this, ReportedDriverListActivity.class);
        startActivity(map);


    }
}
