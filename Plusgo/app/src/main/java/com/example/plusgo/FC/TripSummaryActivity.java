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
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.plusgo.BaseContent;
import com.example.plusgo.Notification.API;
import com.example.plusgo.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TripSummaryActivity extends AppCompatActivity {

    private BaseContent BASECONTENT = new BaseContent();
    private String JSON_URL_TRIP_SUMMARY = BASECONTENT.IpAddress + "/tripsummary/get/";
    private String JSON_URL_CURRENT_PASSENGER_COUNT = BASECONTENT.IpAddress + "/tripsummary/currentPassenger/";
    private String JSON_URL_VEHICLE_DETAILS = BASECONTENT.IpAddress + "/vehicle/specific/object/";
    private String JSON_URL_USER_DETAILS = BASECONTENT.IpAddress + "/users/specific/";
    private String PYTHON_URL_GET_DISTANCE = BASECONTENT.pythonIpAddressGetDistance + "/map/";
    private String PYTHON_URL_FUEL_ESTIMATE = BASECONTENT.pythonIpAddressGetEstimateFuel + "/fuel/";
    private String JSON_URL_POST_NEW_REQUEST = BASECONTENT.IpAddress + "/trip/newRequest";

    //Define Variables in Globally
    private TextView txtDate,txtTime,txtCurrentPassenger,txtEstimateCost,txtWaitingTime,txtDistance,txtViewProfile,txtTripId,txtUserId,txtVehicle,txtToken,VehicleId;
    private String date,time,waitingTime;
    public String  vehicleId,brand,model ,manYear,regYear,cylinders,fuel,capacity,kw,mileage;
    Button btnJoinRide;
    RequestQueue requestQueue ;
    private JsonArrayRequest request;
    private TextView txtUserName;
    public String tripId; //Offer Ride ID / Trip ID
    public String userId; //User ID
    public String FCMtoken,image;
    public CircleImageView profile_image;
    String pickupPoint,dropOffPoint;
    public static final String MY_PREFS_NAME = "MyPrefsFile";


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
        String Token = intent.getStringExtra("Token");
        String img = intent.getStringExtra("img");



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
        VehicleId = (TextView)findViewById(R.id.VehicleId);
        txtToken = (TextView)findViewById(R.id.FCMToken);
        profile_image = (CircleImageView)findViewById(R.id.profile_image);
        btnJoinRide = (Button) findViewById(R.id.btnJoinRide);

        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.user2).error(R.drawable.user2);

        //To load image using Glide
        Glide.with(this)
                .load(BASECONTENT.IpAddress  +img)
                .apply(requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .into(profile_image);

        SharedPreferences location = getSharedPreferences("LOCATION",MODE_PRIVATE);
        pickupPoint = location.getString("source", null);
        dropOffPoint = location.getString("destination", null);

        //added Viraj------------
        txtUserName.setText(FullName);
        txtTripId.setText(TripId);
        txtUserId.setText(UserId);
        txtToken.setText(Token);

        GetVehicleDetails();
        GetTripSummaryDetails();
        GetCurrentPassengers();
        getDistance();


        //Send Values to the User Profile Intent
        txtViewProfile = (TextView)findViewById(R.id.txtviewprofile);
        txtViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //implement onClick
                Intent i = new Intent(TripSummaryActivity.this, UserProfile.class);

                i.putExtra("driverName",txtUserName.getText().toString());
                i.putExtra("vehicleId", VehicleId.getText().toString());
                i.putExtra("txtVehicle", txtVehicle.getText().toString());

                Log.d("driver Name",txtUserName.getText().toString());
                Log.d("vehicleId",VehicleId.getText().toString());
                Log.d("txtVehicle",txtVehicle.getText().toString());
                view.getContext().startActivity(i);

            }
        });



        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
    }

    //Confirmation Trip Message Box
    public void btn_showConfirmation(View view){
        btnJoinRide.setEnabled(false);
        //btnJoinRide.setBackgroundColor(getResources().getColor(R.color.colorBlack));
        btnJoinRide.setText("Please Wait for the Driver Response");
        //btnJoinRide.setBackgroundColor(2);

        final AlertDialog.Builder alert = new AlertDialog.Builder(TripSummaryActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.join_a_ride_dialog,null);

        Button btnNo = (Button)mView.findViewById(R.id.btnNo);
        Button btnYes = (Button)mView.findViewById(R.id.btnYes);

        alert.setView(mView);

        final  AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AddNewRequest();
                sendNotification();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }



    //Fetch details from the MySQL database using Node WebService
    public void GetTripSummaryDetails() {

        //Display Progress Dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Trip Data...");
        progressDialog.show();

        //Get Id from the UserID TextView

        tripId = txtTripId.getText().toString();
        Log.d("tripId",tripId);

        Log.d("Check Get user ", tripId);
        //Call the Web Service which is implementing node js and it pass to the name parameter to get relevant information of the trip
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL_TRIP_SUMMARY+tripId,null,
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
                            Log.d("expe1",e.toString());
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

        tripId = txtTripId.getText().toString();
        Log.d("Check Get passen", tripId);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL_CURRENT_PASSENGER_COUNT+tripId,null,
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
                            Log.d("expe2",e.toString());
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

        String source = pickupPoint;
        String destination = dropOffPoint;

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
                            Log.d("@@@expe3",e.toString());
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

        //GetVehicleDetails();

        SharedPreferences getVehicleDetails = getSharedPreferences("VehicleStore",MODE_PRIVATE);
        brand = getVehicleDetails.getString("Brand", null);
        model = getVehicleDetails.getString("Model", null);
        manYear = getVehicleDetails.getString("MYear", null);
        regYear = getVehicleDetails.getString("RYear", null);
        cylinders = getVehicleDetails.getString("cylinders", null);
        fuel = getVehicleDetails.getString("fuel", null);
        capacity = getVehicleDetails.getString("EngineCapacity", null);
        kw = getVehicleDetails.getString("kw", null);
        mileage = getVehicleDetails.getString("mileage", null);

//        Log.d("@@@@brand",brand);
//        Log.d("@@@@model",model);

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
                            Log.d("expe4",e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        SharedPreferences.Editor editor = getVehicleDetails.edit();
        editor.clear().commit();
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

                        Log.d("zzzz", String.valueOf(response));
                        progressDialog.dismiss();
                        try {
                            vehicleId = response.getString("VehicleID");
                            brand = response.getString("Brand");
                            model = response.getString("Model");
                            manYear = response.getString("MYear");
                            regYear = response.getString("RYear");
                            cylinders = "3";
                            fuel = response.getString("FuelType");
                            capacity = response.getString("EngineCapacity");
                            kw = "38";
                            mileage = response.getString("Mileage");

                            Log.d("brand",brand);
                            Log.d("brand",model);
                            Log.d("brand1",response.getString("Brand"));
                            SharedPreferences.Editor vehicleStore = getSharedPreferences("VehicleStore", MODE_PRIVATE).edit();
                            vehicleStore.putString("Brand", response.getString("Brand"));
                            vehicleStore.putString("Model", response.getString("Model"));
                            vehicleStore.putString("MYear", response.getString("MYear"));
                            vehicleStore.putString("RYear", response.getString("RYear"));
                            vehicleStore.putString("fuel", response.getString("FuelType"));
                            vehicleStore.putString("cylinders", "3");
                            vehicleStore.putString("EngineCapacity", response.getString("EngineCapacity"));
                            vehicleStore.putString("kw", "38");
                            vehicleStore.putString("mileage", response.getString("Mileage"));
                            //vehicleStore.clear();
                            vehicleStore.apply();

                            txtVehicle.setText(brand + " "+model);
                            VehicleId.setText(vehicleId);



                            //Log.d("testSetDate", String.valueOf(date));

                        } catch (JSONException e) {
                            Log.d("expe5",e.toString());
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

    public void GetToken() {

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
                        Log.d("PassengeToken",jsonObject.getString("Token"));
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
        Log.d("Start","Start");
        SharedPreferences userStore = getSharedPreferences("userStore",MODE_PRIVATE);
        String UID = userStore.getString("UId", null);
        String Name = userStore.getString("Name", null);

        String Token = txtToken.getText().toString(); //Driver Token

        //Request Passenger Token
        SharedPreferences tokenStore = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        String PassengerToken = tokenStore.getString("fcmtoken", null);


        Log.d("Start1","Start1");
        String tripId = txtTripId.getText().toString();
        Log.d("tripId2222",tripId);
        String title = "Ride Request";
        //emulator
        String body = "You have been requested to the pickup";
        //String token = "eSB2w-2RIB8:APA91bEdyhV30dCo5ZM_kfmjvUc02_yLPy4jkfE6mk-aODNUlkTpuUicRqV90YG1oMPGE2YBHtFXafwUvRdZl3c9UCZUyGeOuBBVqzqn3rNEMeSs6sWORM2cre71ngTh321gh5jZm9fc";
        //real device
        String token = Token;
        String reqPassenger = Name;
        String reqPassengerId = UID;
        String driverId  = txtUserId.getText().toString();
        String reqDriver = "DriverName";
        String source = pickupPoint;
        String destination = dropOffPoint;
        String passengerToken = PassengerToken;



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://plusgo-ce90f.firebaseapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> call = api.sendNotification(token,title,body,reqPassenger,reqDriver,source,destination,passengerToken,reqPassengerId,driverId,tripId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                 Toast.makeText(TripSummaryActivity.this,response.body().string(),Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    //When passenger click the join ride
    public void AddNewRequest() {
        try {

            String source = pickupPoint;
            String destination = dropOffPoint;
            SharedPreferences userStore = getSharedPreferences("userStore",MODE_PRIVATE);

            String UID = userStore.getString("UId", null);


            Log.d("tripId",tripId);
//            Log.d("passengerId",passengerId);

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tripId", txtTripId.getText().toString());
            jsonObject.put("passengerId", UID);
            jsonObject.put("driverId", txtUserId.getText().toString());
            jsonObject.put("source", source);
            jsonObject.put("destination", destination);
            final String mRequestBody = jsonObject.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_POST_NEW_REQUEST, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_VOLLEY", response);
                    Toast.makeText(TripSummaryActivity.this, "Send Request", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_VOLLEY", error.toString());
                    Toast.makeText(TripSummaryActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
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


        }catch(JSONException e){
            e.printStackTrace();
        }

    }

}
