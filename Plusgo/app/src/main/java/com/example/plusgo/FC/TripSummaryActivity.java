/*
 * *
 *  * Created by Surath Gunawardena
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 4/30/19 9:47 AM
 *
 */

package com.example.plusgo.FC;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.plusgo.BaseContent;
import com.example.plusgo.Notification.API;
import com.example.plusgo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TripSummaryActivity extends AppCompatActivity {

    private BaseContent BASECONTENT = new BaseContent();
    private String JSON_URL_TRIP_SUMMARY = BASECONTENT.IpAddress + "/tripsummary/";
    private String JSON_URL_CURRENT_PASSENGER_COUNT = BASECONTENT.IpAddress + "/tripsummary/currentPassenger/";
    private String JSON_URL_VEHICLE_DETAILS = BASECONTENT.IpAddress + "/vehicle/specific/";
    private String JSON_URL_USER_DETAILS = BASECONTENT.IpAddress + "/users/specific/";
    private String PYTHON_URL_GET_DISTANCE = BASECONTENT.pythonIpAddressGetDistance + "/map/";
    private String PYTHON_URL_FUEL_ESTIMATE = BASECONTENT.pythonIpAddressGetEstimateFuel + "/fuel/";

    //Define Variables in Globally
    private TextView txtDate,txtTime,txtCurrentPassenger,txtEstimateCost,txtWaitingTime,txtDistance,txtViewProfile,txtTripId,txtUserId,txtVehicle,txtToken;
    private String date,time,waitingTime;
    public String  brand,model ,manYear,regYear,cylinders,fuel,capacity,kw,mileage;
    RequestQueue requestQueue ;
    private JsonArrayRequest request;
    private TextView txtUserName;
    public String oid; //Offer Ride ID / Trip ID
    public String userId; //User ID
    public String FCMtoken,image;

    public static final String CHANNEL_ID = "plus_go";
    private static final String CHANNEL_NAME = "Plus Go";
    private static final String CHANNEL_DESC = "Plus Go Notification";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_summary);

        //added Viraj------------
        Intent intent = getIntent();

        String TripId = intent.getStringExtra("TID");
        String UserId = intent.getStringExtra("UID");
        String FullName = intent.getStringExtra("Name");
        String myVar4 = intent.getStringExtra("Source");
        String myVar5 = intent.getStringExtra("Destination");

        requestQueue = Volley.newRequestQueue(this);


        //Variable assign With TextView Which is used in the layout
        txtTripId = (TextView)findViewById(R.id.txtTripId);
        txtUserId = (TextView)findViewById(R.id.UserID);
        txtUserName = (TextView)findViewById(R.id.txtUserName);
        txtDate = (TextView)findViewById(R.id.txtDate);
        txtTime = (TextView)findViewById(R.id.txtTime);
        txtCurrentPassenger = (TextView)findViewById(R.id.txtCurrentPassenger);
        txtWaitingTime = (TextView)findViewById(R.id.txtWaitingTime);
        txtEstimateCost = (TextView)findViewById(R.id.txtEstimateCost);
        txtDistance = (TextView)findViewById(R.id.txtDistance);
        txtVehicle = (TextView)findViewById(R.id.VehicleName);
        txtToken = (TextView)findViewById(R.id.FCMToken);

        Log.d("q1:", TripId);
        Log.d("q2:", UserId);
        Log.d("q3:", FullName);
        Log.d("q4:", myVar4);
        Log.d("q5:", myVar5);

        //added Viraj------------


        txtViewProfile = (TextView)findViewById(R.id.txtviewprofile);
        txtViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openUserProfile();
            }
        });

        //added Viraj
        txtUserName.setText(FullName);
        txtTripId.setText(TripId);
        txtUserId.setText(UserId);

        GetTripSummaryDetails();
        GetCurrentPassengers();
        getDistance();
        GetVehicleDetails();
        GetUserDetails();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }

        findViewById(R.id.btnJoinRide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // sendNotification();
            }
        });
    }

    public void openUserProfile(){
        Intent intent = new Intent(this,UserProfile.class);
        startActivity(intent);
    }

    //Fetch details from the MySQL database using Node WebService
    public void GetTripSummaryDetails() {

        //Display Progress Dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();

        //Get Id from the UserID TextView

        oid = txtTripId.getText().toString();

        Log.d("Check Get user ", oid);
        //Call the Web Service which is implementing node js and it pass to the name parameter to get relevant information of the trip
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL_TRIP_SUMMARY+oid,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("sdss", String.valueOf(response));
                        progressDialog.dismiss();
                        try {

                            date = response.getString("StartDate");
                            time = response.getString("StartTime");
                            waitingTime = response.getString("WaitingTime");

                            Log.d("testDate", String.valueOf(date));

                            txtDate.setText(date);
                            txtTime.setText(time);
                            txtWaitingTime.setText(waitingTime + " mins");

                            //Log.d("testSetDate", String.valueOf(date));

                        } catch (JSONException e) {
                            Log.d("expe",e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }


    //Get Current Passengers
    public void GetCurrentPassengers() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();

        oid = txtTripId.getText().toString();
        Log.d("Check Get passen", oid);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL_CURRENT_PASSENGER_COUNT+oid,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("sdss", String.valueOf(response));
                        progressDialog.dismiss();
                        try {


                            String count = response.getString("Count");


                            Log.d("testcount", String.valueOf(count));

                            txtCurrentPassenger.setText(count);

                            //Log.d("testSetDate", String.valueOf(date));

                        } catch (JSONException e) {
                            Log.d("expe",e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }


    //Get Trip Distance
    public void getDistance() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();

        String source = "Malabe";
        String destination = "SLIIT";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, PYTHON_URL_GET_DISTANCE+source+"/"+destination,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("distance", String.valueOf(response));
                        progressDialog.dismiss();
                        try {


                            String distance = response.getString("distance");
                            String tripDistanceWithoutKm = distance.split(" km")[0];
                            txtDistance.setText(tripDistanceWithoutKm);

                            Log.d("distance", tripDistanceWithoutKm);
                            if(!txtDistance.getText().toString().isEmpty()){
                                getEstimatedCost();
                            }

                        } catch (JSONException e) {
                            Log.d("expe",e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    //Get Estimated Cost
    public void getEstimatedCost() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();

        GetVehicleDetails();
        Log.d("vehicle1",brand);
        Log.d("vehicle1",model);
        Log.d("vehicle1",manYear);

//        String manYear = "2015";
//        String regYear = "2015";
//        String cylinders = "3";
//        String fuel ="2" ;
//        String capacity="658";
//        String kW = "38";
//        String mileage = "48232";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, PYTHON_URL_FUEL_ESTIMATE+manYear+"/"+regYear+"/"+cylinders+"/"+fuel+"/"+capacity+"/"+kw+"/"+mileage,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("fuelPrediction", String.valueOf(response));
                        progressDialog.dismiss();
                        try {

                            String fuelPrediction = response.getString("fuelPrediction");
                            //  Log.d("123", txtDistance.getText().toString());
                            float float_fuelPrediction = Float.parseFloat(fuelPrediction);
                            //String getDistance = tripDistanceWithoutKm;
                            String getDistance = txtDistance.getText().toString();
                            float getcurrentPassenger = Float.parseFloat(txtCurrentPassenger.getText().toString());
                            float float_distance = Float.parseFloat(getDistance);


                            DecimalFormat df = new DecimalFormat("#.##");
                            df.setRoundingMode(RoundingMode.CEILING);

                            String cost = df.format((float_distance * float_fuelPrediction)/(getcurrentPassenger+1));

                            System.out.println(cost);
                            System.out.println("2");


                            txtEstimateCost.setText("Rs." + cost);

                        } catch (JSONException e) {
                            Log.d("expe",e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }


    //Fetch Vehicle details
    public void GetVehicleDetails() {

        //Display Progress Dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();

        //Get Id from the UserID TextView

        userId = txtUserId.getText().toString();

        Log.d("Check Get user ", userId);
        //Call the Web Service which is implementing node js and it pass to the name parameter to get relevant information of the trip
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL_VEHICLE_DETAILS+userId,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("sdss", String.valueOf(response));
                        progressDialog.dismiss();
                        try {

                            brand = response.getString("Brand");
                            model = response.getString("Model");
                            manYear = response.getString("MYear");
                            regYear = response.getString("RYear");
                            cylinders = "3";
                            fuel = response.getString("FuelType");
                            capacity = response.getString("EngineCapacity");
                            kw = "38";
                            mileage = response.getString("Mileage");

                            txtVehicle.setText(brand + " "+model);

                            //Log.d("testSetDate", String.valueOf(date));

                        } catch (JSONException e) {
                            Log.d("expe",e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    public void GetUserDetails() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();
        //   Log.e("JSON_URL",JSON_URL+username+"/"+password);
        request = new JsonArrayRequest(JSON_URL_USER_DETAILS+userId, new Response.Listener<JSONArray>() {

            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;
                for(int i= 0; i<response.length(); i++){
                    progressDialog.dismiss();
                    try{
                        jsonObject = response.getJSONObject(i);
                        txtToken.setText(jsonObject.getString("Token"));

                    }catch (JSONException e){

                        e.printStackTrace();
                        Log.d("JSONREQUEST","ERROR");
                        //Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_LONG).show();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  pDialog.dismiss();
                Log.d("xxx", error.toString());
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);

    }

    private void sendNotification(){
        String title = "Notification Title";
        String body = "sesdsdsd";
        String token = "f71ik2dgMUo:APA91bEaQF1ehEDWUpRjjZpIHWSN12EWmgZ9snEvmBniYPStrLn2OcKle6RAh_hmiRDD7JuzdxgwrElEPNl-uJz9TFta8JwbjSOeu5NhAy78WimkbFxCxegu_zegHFclSGNatdBfNyYH";
        String reqPassenger = "Surath Gunawardena";
        String reqDriver = "U1111111-Driver";
        String source = "Kaduwela";
        String destination = "Battaramulla";
        String passengerToken = "Battaramulla";



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://plusgo-ce90f.firebaseapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> call = api.sendNotification(token,title,body,reqPassenger,reqDriver,source,destination, passengerToken);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    Toast.makeText(TripSummaryActivity.this,response.body().string(),Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

}
