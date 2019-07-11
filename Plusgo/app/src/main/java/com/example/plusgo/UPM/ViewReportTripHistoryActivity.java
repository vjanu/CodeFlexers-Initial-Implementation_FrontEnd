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
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.plusgo.Adapters.ReportedDriverHistoryAdapter;
import com.example.plusgo.Adapters.ReportedDriverListAdapter;
import com.example.plusgo.BaseContent;
import com.example.plusgo.Login;
import com.example.plusgo.OPR.MainActivity;
import com.example.plusgo.R;
import com.example.plusgo.Utility.DriverListItem;
import com.example.plusgo.Utility.ReportedDriverHistoryItem;
import com.example.plusgo.Utility.ReportedDriverListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class ViewReportTripHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private static final String KEY_EMPTY = "";
    private RecyclerView.Adapter adapter;
    private List<ReportedDriverHistoryItem> driverListItems;
    private List<String> drivers;
    private TextView rate, count, welcome, noDrivers;
    private Button btnReport;
    BaseContent BASECONTENT = new BaseContent();
    private String dId;
    private String pId;
    private String uid;
    private TempGPSActivity tempGPSActivity;
    private String URL_TRIPS_HISTORY = BASECONTENT.IpAddress + "/spouse/";
    private String URL_REPORT_DRIVERS = BASECONTENT.IpAddress + "/spouse/report";
    private String URL_CHECK_REPORT_DRIVERS = BASECONTENT.IpAddress + "/spouse/available/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reported_trips);

        SharedPreferences user = getSharedPreferences("userStore",MODE_PRIVATE);
        uid = user.getString("UId", null);


        Intent intent = getIntent();

        pId = intent.getStringExtra("PUID");
        dId = intent.getStringExtra("DUID");

        recyclerView = (RecyclerView)findViewById(R.id.tripsRecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnReport = (Button)findViewById(R.id.reportD);


        driverListItems = new ArrayList<>();
        //initiate methods
        loadTripsDetails();
        checkReportedStatus();

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportDriver();
            }
        });




    }

    //method to authenticate the user
    private void checkReportedStatus() {
        Log.d("checkReportedStatus", "checkReportedStatus");
        JsonArrayRequest request = new JsonArrayRequest(URL_CHECK_REPORT_DRIVERS+pId+"/"+dId, new Response.Listener<JSONArray>() {

            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;
                Log.d("response.length()", String.valueOf(response.length()));
                for(int i= 0; i<response.length(); i++){
                    btnReport.setEnabled(false);
                    btnReport.setText("Already Reported");

                }

                }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("xxx", error.toString());

            }
        });

        requestQueue = Volley.newRequestQueue(ViewReportTripHistoryActivity.this);
        requestQueue.add(request);

    }

    private void reportDriver(){
        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("PUID", pId);
            jsonObject.put("DUID", dId);
            final String mRequestBody = jsonObject.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REPORT_DRIVERS, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_VOLLEY", response);
                    btnReport.setEnabled(false);
                    btnReport.setText("Already Reported");
                    Toast.makeText(ViewReportTripHistoryActivity.this, "Driver is reported", Toast.LENGTH_SHORT).show();


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_VOLLEY", error.toString());
                    Toast.makeText(ViewReportTripHistoryActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //load all relevant driver details from the database
    private void loadTripsDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Trip Details");
        progressDialog.show();

        try {
            JsonArrayRequest stringRequest = new JsonArrayRequest(URL_TRIPS_HISTORY+pId+"/"+dId, new Response.Listener<JSONArray>() {

                public void onResponse(JSONArray response) {
                    Log.d("qqq1", response.toString());
                    progressDialog.dismiss();
                    RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.user2).error(R.drawable.user2);
                    JSONObject jsonObject = null;
                    for(int i= 0; i<response.length(); i++){
                        try{

                            jsonObject = response.getJSONObject(i);
                            ReportedDriverHistoryItem item = new ReportedDriverHistoryItem(
                                    jsonObject.getString("source"),
                                    jsonObject.getString("destination"),
                                    String.format("Rs.%.2f",jsonObject.getDouble("price")),
                                    jsonObject.getString("dateTime")
                            );

                            driverListItems.add(item);

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    adapter = new ReportedDriverHistoryAdapter(driverListItems, getApplicationContext());
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




    //destroy the session created
    private void logout(){
        SharedPreferences sharedPreferences = getSharedPreferences("userStore", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear().commit();

        SharedPreferences user = getSharedPreferences("userStore",MODE_PRIVATE);
        boolean uid = user.getBoolean("Islogin", false);
        Log.d("login2:", String.valueOf(uid));

        finish();
        Intent login = new Intent(ViewReportTripHistoryActivity.this, Login.class);
        startActivity(login);

    }
    @Override
    public void onBackPressed() {
        finish();
        Intent map = new Intent(ViewReportTripHistoryActivity.this, ReportedDriverListActivity.class);
        startActivity(map);


    }
}
