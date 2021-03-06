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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.request.RequestOptions;
import com.example.plusgo.Adapters.DriverListAdapter;
import com.example.plusgo.BaseContent;
import com.example.plusgo.Login;
import com.example.plusgo.OPR.MainActivity;
import com.example.plusgo.R;
import com.example.plusgo.Utility.Driver;
import com.example.plusgo.Utility.DriverListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;


public class DriverListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private static final String KEY_EMPTY = "";
    private RecyclerView.Adapter adapter;
    private List<DriverListItem> driverListItems;
    private List<String> drivers;
    private List<Driver> reportedDrivers;
    private TextView rate, count, welcome, noDrivers;
    private ImageButton btnLogout;
    BaseContent BASECONTENT = new BaseContent();
    private String id;
    private String uid;
//    String c = "U1558711443502";
    private TempGPSActivity tempGPSActivity;
    private String URL_AVAILABLE_DRIVERS = BASECONTENT.IpAddress + "/available/driver/";
    private String PYTHON_URL_GET_DRIVERS = BASECONTENT.pythonIpAddress + "/ridematching/kmeans/";
    private String URL_REPORTED_DRIVERS = BASECONTENT.IpAddress + "/spouse/specific/report/passenger/";

    //    private ImageView profileImage;
//    private String JSON_URL = BASECONTENT.IpAddress + "/comments/specific/";
//    private String JSON_URL = "https://gist.githubusercontent.com/vjanu/4720773fad79534af4460be44002789e/raw/f9125dfa3f728c91ab62ac5c5b7064e2808de2f5/drivers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_list);
        tempGPSActivity = new TempGPSActivity();

        SharedPreferences user = getSharedPreferences("userStore",MODE_PRIVATE);
        uid = user.getString("UId", null);


        recyclerView = (RecyclerView)findViewById(R.id.recyclerviewid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        noDrivers = (TextView)findViewById(R.id.noDrivers);
        btnLogout = (ImageButton) findViewById(R.id.logout1);


        driverListItems = new ArrayList<>();
        reportedDrivers = new ArrayList<>();
        //initiate methods
//       getAvailableDriverList();
        getReportedDrivers();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }

    List<String> availableDrivers = new ArrayList<>();
    List<String> singleQouteAvailableDrivers = new ArrayList<>();
    public void getAvailableDriverList(final List<Driver> reportedDrivers) {

        Log.e("too","started");
        RequestQueue requestQueue = Volley.newRequestQueue(DriverListActivity.this);
        StringRequest request = new StringRequest(PYTHON_URL_GET_DRIVERS+uid, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonObject = new JSONArray(response);

//                    Log.d("oo",  String.valueOf(jsonObject.length()));
//                    Log.d("oo1",  String.valueOf(jsonObject.get(0)));
//                    Log.d("oo2",  String.valueOf(jsonObject.get(1)));

                    for(int i=0; i <jsonObject.length(); i++){
                        availableDrivers.add(jsonObject.get(i).toString());

                    }
                    availableDrivers.remove(uid);

                    Log.d("reportsize", String.valueOf(reportedDrivers.size()));
                    for(int i=0; i<reportedDrivers.size();i++){
                        availableDrivers.remove(reportedDrivers.get(i).getUID());
                        Log.d("report", reportedDrivers.get(i).getUID());
                    }

                    for(int i=0; i<availableDrivers.size();i++){
                        singleQouteAvailableDrivers.add("'"+availableDrivers.get(i)+"'");
                        Log.d("rtt", "'"+availableDrivers.get(i)+"'");
                    }
                    loadDriverDetails(singleQouteAvailableDrivers);
                }
                catch(JSONException e){

                    e.printStackTrace();
                    Log.d("JSONREQUEST","ERROR");
                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue = Volley.newRequestQueue(DriverListActivity.this);
        requestQueue.add(request);

    }
    //load all relevant driver details from the database
    private void loadDriverDetails(List<String> dri) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Drivers");
        progressDialog.show();

        try {
            JsonArrayRequest stringRequest = new JsonArrayRequest(URL_AVAILABLE_DRIVERS+dri, new Response.Listener<JSONArray>() {

                public void onResponse(JSONArray response) {
                    Log.d("qqq1", response.toString());
                    progressDialog.dismiss();
                    RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.user2).error(R.drawable.user2);
                    JSONObject jsonObject = null;
                    for(int i= 0; i<response.length(); i++){
                        try{

                            jsonObject = response.getJSONObject(i);
                           DriverListItem item = new DriverListItem(jsonObject.getString("OID"),jsonObject.getString("UserID"),jsonObject.getString("FullName"), jsonObject.getString("Source"),
                                    jsonObject.getString("Destination"), jsonObject.getString("Model"), Double.parseDouble(jsonObject.getString("AverageRating")),
                                    jsonObject.getString("img"),
                                    jsonObject.getString("StartTime"),
                                    jsonObject.getString("Token")
                                   );

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
//                    noDrivers.setEnabled(true);
                    noDrivers.setVisibility(View.VISIBLE);
//                    Toast.makeText(DriverListActivity.this, "No Drivers Available", Toast.LENGTH_SHORT).show();
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

        private void getReportedDrivers() {
            Log.d("getReportedDrivers", "getReportedDrivers");
            JsonArrayRequest request = new JsonArrayRequest(URL_REPORTED_DRIVERS+uid, new Response.Listener<JSONArray>() {

            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for(int i= 0; i<response.length(); i++){
                    try{
                        jsonObject = response.getJSONObject(i);
                        if(jsonObject.length()!=0) {
                            Driver reportedD = new Driver();
                            reportedD.setUID(jsonObject.getString("DUID"));
                            reportedDrivers.add(reportedD);
                            Log.d("yoo",  reportedD.getUID());
                        }
                        else{

                        }

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                getAvailableDriverList(reportedDrivers);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(DriverListActivity.this);
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
        Intent login = new Intent(DriverListActivity.this, Login.class);
        startActivity(login);

    }
    @Override
    public void onBackPressed() {
        finish();
        Intent map = new Intent(DriverListActivity.this, MainActivity.class);
        startActivity(map);


    }
}
