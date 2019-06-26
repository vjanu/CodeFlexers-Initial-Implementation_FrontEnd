package com.example.plusgo.FC;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.example.plusgo.BaseContent;
import com.example.plusgo.DVPRM.DummyActivity;
import com.example.plusgo.DVPRM.RatingDriverActivity;
import com.example.plusgo.FC.Adapters.CurrentPassengerAdapter;
import com.example.plusgo.Notification.API;
import com.example.plusgo.Notification.OpenNotification;
import com.example.plusgo.R;
import com.example.plusgo.UPM.NewUserActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PassengerCurrentTrip extends AppCompatActivity {

    BaseContent BASECONTENT = new BaseContent();
    private final String JSON_GET_STRAT_MILEAGE = BASECONTENT.IpAddress+"/trip/specific/";
    private final String JSON_GET_PRICE = BASECONTENT.IpAddress+"/trip/price/";
    private final String JSON_PUT_UPDATE_START_TRIP = BASECONTENT.IpAddress+"/trip/update/startRide/";
    private final String JSON_PUT_UPDATE_END_TRIP = BASECONTENT.IpAddress+"/trip/update/endRide/";
    private final String JSON_PUT_UPDATE_FARE_CALCULATION = BASECONTENT.IpAddress+"/trip/update/fare/";
    private final String JSON_PUT_UPDATE_DROP_USER_DETAILS = BASECONTENT.IpAddress+"/trip/update/fare/dropoff/";
    private String JSON_URL_CURRENT_PASSENGER_COUNT = BASECONTENT.IpAddress + "/tripsummary/currentPassenger/";
    private String JSON_URL_GET_TRIP_STARTED_USERS = BASECONTENT.IpAddress + "/trip/getDetails/";
    private String JSON_URL_GET_DROPOFF_USER_DETAILS = BASECONTENT.IpAddress + "/trip/update/fare/dropoff/";
    private String JSON_URL_POST_NEW_REQUEST = BASECONTENT.IpAddress + "/trip/newRequest";
    private TextView txtPassengerName,txtStartPoint,txtEndPoint,txtHiddenTripId,txtHiddenPassengerId,txtHiddenToken,txtTripStatus,
            txtHiddenCurrentMileage,txtHiddenCurrentPassengers,txtHiddenStartMileage,txtHiddenPrice,txtPrice,lblPrice;
    public Button btnEndTrip,btnStartTrip;
    RequestQueue requestQueue ;
    private JsonArrayRequest request;
    private ImageView imgLogo;
    private String id;
    private int passengercount;
    private String tripId,passengerId,driverId;
    double price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_current_trip);
        requestQueue = Volley.newRequestQueue(this);
        //Get User Id from the Shared Preferences
        SharedPreferences user = getSharedPreferences("userStore",MODE_PRIVATE);
        id = user.getString("UId", null);

        Intent intent = getIntent();

        String TripId = intent.getStringExtra("TripId");
        String PassengerId = intent.getStringExtra("PassengerId");
        String Name = intent.getStringExtra("Name");
        String startpoint = intent.getStringExtra("Source");
        String endpoint = intent.getStringExtra("Destination");
        String Token = intent.getStringExtra("Token");
        String Status = intent.getStringExtra("Status");
        String userImage = intent.getStringExtra("userImage");
        //String startButtonVisibility = intent.getStringExtra("btnStartifTripNotStartVisibility");



        //Variable assign With TextView Which is used in the layout
        txtPassengerName = (TextView)findViewById(R.id.txtPassengerName);
        txtStartPoint = (TextView)findViewById(R.id.txtStartPoint);
        txtEndPoint = (TextView)findViewById(R.id.txtEndPoint);
        txtHiddenTripId = (TextView)findViewById(R.id.txtHiddenTripId);
        txtHiddenPassengerId = (TextView)findViewById(R.id.txtHiddenPassengerId);
        txtHiddenToken = (TextView)findViewById(R.id.txtHiddenToken);
        txtTripStatus = (TextView)findViewById(R.id.txtTripStatus);
        txtHiddenCurrentMileage = (TextView)findViewById(R.id.txtHiddenCurrentMileage);
        txtHiddenCurrentPassengers = (TextView)findViewById(R.id.txtHiddenCurrentPassengers);
        txtHiddenStartMileage = (TextView)findViewById(R.id.txtHiddenStartMileage);
        txtHiddenPrice = (TextView)findViewById(R.id.txtHiddenPrice);
        txtPrice = (TextView)findViewById(R.id.txtPrice);
        lblPrice = (TextView)findViewById(R.id.lblPrice);
        btnStartTrip = (Button)findViewById(R.id.btnStartTrip);
        btnEndTrip = (Button) findViewById(R.id.btnEndTrip);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);

        txtHiddenTripId.setText(TripId);
        txtHiddenPassengerId.setText(PassengerId);
        txtPassengerName.setText(Name);
        txtStartPoint.setText(startpoint);
        txtEndPoint.setText(endpoint);
        txtHiddenToken.setText(Token);
        txtTripStatus.setText(Status);
        //Need To user Image to the imgLogo
        txtHiddenCurrentMileage.setText("4000");

        Log.d("status",Status);

        GetCurrentPassengers();
        //GetUserDetails();
        GetDetailsOfSpecificCurrentUser();


        if(Status.equals("1")){
            btnStartTrip.setVisibility(View.GONE);
            btnEndTrip.setVisibility(View.VISIBLE);
        }

        else if(Status.equals("0")){
            btnEndTrip.setVisibility(View.GONE);
            btnStartTrip.setVisibility(View.VISIBLE);
        }
        else{
            //Trip Ended
            btnEndTrip.setVisibility(View.GONE);
            btnStartTrip.setVisibility(View.GONE);
        }


        btnStartTrip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d("Click BtnStart","check");
                double currentPassengers = Double.parseDouble(txtHiddenCurrentPassengers.getText().toString());

                    UpdateFareCalculation();

                //UpdateFareCalculation();//: Comment for a moment
                //---calculatePrice();
                UpdateStatusWhenTripStart();//sucess
                btnStartTrip.setVisibility(View.GONE);
                btnEndTrip.setVisibility(View.VISIBLE);
                txtTripStatus.setText("Trip Ended");

                Log.d("Click BtnStart","execute");
            }
        });

        btnEndTrip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d("Click BtnStart","check");
                //GetDetailsOfSpecificCurrentUser();

                UpdateStatusWhenTripEnd();
                UpdateFareCalculation();
                UpdateFareForGetOffUser();

                btnEndTrip.setVisibility(View.GONE);
                btnStartTrip.setVisibility(View.GONE);
                PriceOfThePAssenger();
                AcceptRideNotification();
                lblPrice.setVisibility(View.VISIBLE);
                txtPrice.setVisibility(View.VISIBLE);
                txtTripStatus.setText("Trip Ended");

                Intent verify = new Intent(PassengerCurrentTrip.this, RatingDriverActivity.class);
                startActivity(verify);
                Log.d("Click BtnStart","execute");
            }
        });

        //Shares Preference for Rating
        SharedPreferences.Editor ratingStore = getSharedPreferences("ratingStore", MODE_PRIVATE).edit();
        ratingStore.putString("passengerId", passengerId);
        ratingStore.putString("driverId", driverId);
        ratingStore.apply();


    }

    //When Start the trip passenger trip status will be changed and vehicle current mileage set to the current_passenger Table
    public void UpdateStatusWhenTripStart() {
        try {

            String tripId = txtHiddenTripId.getText().toString();
            String passengerId = txtHiddenPassengerId.getText().toString();
            String driverId = "U1558711443502";


            Log.d("tripId",tripId);
            Log.d("passengerId",passengerId);

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("trip_status", 1);
                jsonObject.put("startMileage", txtHiddenCurrentMileage.getText().toString());
                final String mRequestBody = jsonObject.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.PUT, JSON_PUT_UPDATE_START_TRIP+tripId+"/"+passengerId+"/"+driverId, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_VOLLEY", response);
                        Toast.makeText(PassengerCurrentTrip.this, "Trip Started", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_VOLLEY", error.toString());
                        Toast.makeText(PassengerCurrentTrip.this, "Error Occurred", Toast.LENGTH_SHORT).show();
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


    public void UpdateStatusWhenTripEnd() {
        try {


            tripId = txtHiddenTripId.getText().toString();
            passengerId = txtHiddenPassengerId.getText().toString();
           // price = Double.parseDouble(txtHiddenPrice.getText().toString());
            String driverId = "U1558711443502";


            Log.d("tripId",tripId);
            Log.d("passengerId",passengerId);

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("trip_status", 2);

            final String mRequestBody = jsonObject.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.PUT, JSON_PUT_UPDATE_END_TRIP+tripId+"/"+passengerId+"/"+driverId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_VOLLEY", response);
                    Toast.makeText(PassengerCurrentTrip.this, "Trip Ended", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_VOLLEY", error.toString());
                    Toast.makeText(PassengerCurrentTrip.this, "Error Occurred", Toast.LENGTH_SHORT).show();
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

    //Get Current Passengers
    public void GetCurrentPassengers() { //sucess
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Current Passengers");
        progressDialog.show();

        tripId = txtHiddenTripId.getText().toString();
        Log.d("@@@@@tripId",tripId);
        passengerId = txtHiddenPassengerId.getText().toString();



        Log.d("Check Get passen", tripId);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL_CURRENT_PASSENGER_COUNT+tripId,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("sdss", String.valueOf(response));
                          progressDialog.dismiss();
                        try {
                            //count = response.getString("Count");
//                            passengercount = Integer.parseInt(response.getString("Count"));
                            txtHiddenCurrentPassengers.setText(response.getString("Count"));
                        } catch (JSONException e) {
                            Log.d("expe",e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"GetCurrentPassengers"+error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }


    //To get Start Mileage
//    public void GetDetailsOfSpecificCurrentUser() {
////        final ProgressDialog progressDialog = new ProgressDialog(this);
////        progressDialog.setMessage("Loading Data...");
////        progressDialog.show();
//
//        tripId = txtHiddenTripId.getText().toString();
//        passengerId = txtHiddenPassengerId.getText().toString();
//        String driverId = "U1558711443512";
//
//       // String oid = txtHiddenTripId.getText().toString();;
//        //Log.d("Check Get passen", oid);
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_GET_STRAT_MILEAGE+tripId+"/"+passengerId+"/"+driverId,null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        Log.d("RESPONCE1", String.valueOf(response));
////                        progressDialog.dismiss();
//                        try {
//                            txtHiddenStartMileage.setText(response.getString("startMileage"));
//                            Log.d("RESPONCE3",txtHiddenStartMileage.toString());
//                        } catch (JSONException e) {
//                            Log.d("RESPONCE2",e.toString());
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(),"GetDetailsOfSpecificCurrentUser"+error.getMessage(),Toast.LENGTH_LONG).show();
//                    }
//                });
//
//        requestQueue.add(jsonObjectRequest);
//    }


    /*
    Get Vehicle Details New Implementation

     */
    public void GetDetailsOfSpecificCurrentUser() {


        tripId = txtHiddenTripId.getText().toString();
        passengerId = txtHiddenPassengerId.getText().toString();
        String driverId = "U1558711443502";

//        String oid = txtHiddenTripId.getText().toString();;
        Log.d("tripIdtripId", tripId);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Start Mileage...");
        progressDialog.show();
        Log.d("TripIDD",tripId);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_GET_STRAT_MILEAGE+tripId+"/"+passengerId+"/"+driverId,
                //StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_GET_CURRENT_PASSENGER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array =jsonObject.getJSONArray("startMileage");
                            Log.d("array.length()", String.valueOf(array.length()));
                            for(int i=0;i<array.length();i++){
                                Log.d("444","bxxxx");
                                JSONObject o = array.getJSONObject(i);
                                Current_Passenger items = new Current_Passenger(
                                        o.getString("startMileage")

                                );
                                txtHiddenStartMileage.setText(o.getString("startMileage"));

                            }

                        } catch (JSONException e) {
                            Log.d("kachal",e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("12435",error.getMessage());

                        Toast.makeText(PassengerCurrentTrip.this,"HohOOO"+error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(PassengerCurrentTrip.this);
        requestQueue.add(stringRequest);


    }

    /*
    END Get Vehicle Details New Implementation

     */
    public void calculatePrice(){

        //Get Start Mileage
        double StartMileage = Double.parseDouble(txtHiddenStartMileage.getText().toString());
        double currentMileage = Double.parseDouble(txtHiddenCurrentMileage.getText().toString());
        double passengercount = Double.parseDouble(txtHiddenCurrentPassengers.getText().toString());
        Log.d("@#StartMileage", String.valueOf(StartMileage));
        double distance = currentMileage - StartMileage;
        Log.d("@#currentMileage", String.valueOf(currentMileage));
        Log.d("@#Pdistance", String.valueOf(distance));
        double calculatePrice = distance*(15)/(passengercount+1);
        Log.d("@#passengercount", String.valueOf(passengercount));
        Log.d("@#PcalculatePrice", String.valueOf(calculatePrice));
        txtHiddenPrice.setText(String.valueOf(calculatePrice));
    }

    public void UpdateFareCalculation() {
        Log.d("GetUserDetailsEx" , "Check");
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading Data...");
//        progressDialog.show();
        tripId = txtHiddenTripId.getText().toString();
        String driverId = "U1558711443502";

        //   Log.e("JSON_URL",JSON_URL+username+"/"+password);
        request = new JsonArrayRequest(JSON_URL_GET_TRIP_STARTED_USERS+tripId+"/"+driverId, new Response.Listener<JSONArray>() {

            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;
                if(response.length()>0){
                for (int i = 0; i < response.length(); i++) {
//                    progressDialog.dismiss();
                    try {


                        jsonObject = response.getJSONObject(i);
//                        Log.d("passengerId", jsonObject.getString("passengerId").toString());
//                        Log.d("startMileage", jsonObject.getString("startMileage").toString());
//                        Log.d("price", jsonObject.getString("price").toString());

                        tripId = txtHiddenTripId.getText().toString();
                        String driverId = "U1558711443502";
                        String passengerId = jsonObject.getString("passengerId");

                        double currentMileage = Double.parseDouble(txtHiddenCurrentMileage.getText().toString());
                        double startMileage = Double.parseDouble(jsonObject.getString("startMileage").toString());
                        double currentPrice = Double.parseDouble(jsonObject.getString("price").toString());
                        double passengercount = Double.parseDouble(txtHiddenCurrentPassengers.getText().toString());
                        double distance = currentMileage - startMileage ;
                        double calculatePrice = distance * (15) / (passengercount);
                       Log.d("@#PcalculatePrice", String.valueOf(calculatePrice));
                       Log.d("startMileageDouble", String.valueOf(startMileage));

                        double updatePrice = currentPrice + calculatePrice;

                        //Put Response Start
                        //RequestQueue requestQueue = Volley.newRequestQueue(this);
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("price", updatePrice);
                        jsonObject1.put("startMileage", txtHiddenCurrentMileage.getText().toString());
                        Log.d("@@updatePrice@@", String.valueOf(updatePrice));
                        Log.d("@@startMileage@@", String.valueOf(txtHiddenCurrentMileage.getText().toString()));

                        final String mRequestBody = jsonObject1.toString();

                        StringRequest stringRequest = new StringRequest(Request.Method.PUT, JSON_PUT_UPDATE_FARE_CALCULATION + tripId + "/" + passengerId + "/" + driverId, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("LOG_VOLLEY", response);
                                Toast.makeText(PassengerCurrentTrip.this, "Trip Ended", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("LOG_VOLLEY", error.toString());
                                Toast.makeText(PassengerCurrentTrip.this, "Error Occurred", Toast.LENGTH_SHORT).show();
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


                        //Put Response End

                    } catch (JSONException e) {

                        e.printStackTrace();
                        Log.d("JSONREQUEST", "ERROR");
                        //Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_LONG).show();
                    }

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


    //to Update Fare for the get off passenger
    public void UpdateFareForGetOffUser() {
        Log.d("GetUserDetailsEx" , "Check");
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading Data...");
//        progressDialog.show();


        //   Log.e("JSON_URL",JSON_URL+username+"/"+password);
        request = new JsonArrayRequest(JSON_URL_GET_DROPOFF_USER_DETAILS+tripId+"/"+passengerId+"/"+driverId, new Response.Listener<JSONArray>() {

            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;
                if(response.length()>0){
                    for (int i = 0; i < response.length(); i++) {
//                        progressDialog.dismiss();
                        try {


                            jsonObject = response.getJSONObject(i);
                            Log.d("@@@passengerId", jsonObject.getString("passengerId").toString());
                            Log.d("@@@startMileage", jsonObject.getString("startMileage").toString());
                            Log.d("@@@price", jsonObject.getString("price").toString());

                            tripId = txtHiddenTripId.getText().toString();
                            String driverId = "U1558711443502";
                            String passengerId = jsonObject.getString("passengerId");

                            double currentMileage = Double.parseDouble(txtHiddenCurrentMileage.getText().toString());
                            double startMileage = Double.parseDouble(jsonObject.getString("startMileage").toString());
                            double currentPrice = Double.parseDouble(jsonObject.getString("price").toString());
                            double passengercount = Double.parseDouble(txtHiddenCurrentPassengers.getText().toString());
                            double distance = currentMileage - startMileage;
                            double calculatePrice = distance * (15) / (passengercount);
                            Log.d("@@@distance", String.valueOf(distance));
                            Log.d("@@@startMileage", String.valueOf(startMileage));
                            Log.d("@@@PcalculatePrice", String.valueOf(calculatePrice));
                            Log.d("@@@startMileageDouble", String.valueOf(startMileage));

                            double updatePrice = currentPrice + calculatePrice;

                            //Put Response Start
                            //RequestQueue requestQueue = Volley.newRequestQueue(this);
                            JSONObject jsonObject1 = new JSONObject();
                            jsonObject1.put("price", updatePrice);
                            jsonObject1.put("startMileage", txtHiddenCurrentMileage.getText().toString());

                            final String mRequestBody = jsonObject1.toString();

                            StringRequest stringRequest = new StringRequest(Request.Method.PUT, JSON_PUT_UPDATE_DROP_USER_DETAILS + tripId + "/" + passengerId + "/" + driverId, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i("LOG_VOLLEY", response);
                                    Toast.makeText(PassengerCurrentTrip.this, "Trip Ended", Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("LOG_VOLLEY", error.toString());
                                    Toast.makeText(PassengerCurrentTrip.this, "Error Occurred", Toast.LENGTH_SHORT).show();
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


                            //Put Response End

                        } catch (JSONException e) {

                            e.printStackTrace();
                            Log.d("JSONREQUEST", "ERROR");
                            //Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_LONG).show();
                        }

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


    public void PriceOfThePAssenger() {


        tripId = txtHiddenTripId.getText().toString();
        passengerId = txtHiddenPassengerId.getText().toString();
        String driverId = "U1558711443502";



//        String oid = txtHiddenTripId.getText().toString();;
        Log.d("tripIdtripId", tripId);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Start Mileage...");
        progressDialog.show();
        Log.d("TripIDD",tripId);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_GET_PRICE+tripId+"/"+passengerId+"/"+driverId,
                //StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_GET_CURRENT_PASSENGER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array =jsonObject.getJSONArray("price");
                            Log.d("array.length()", String.valueOf(array.length()));
                            for(int i=0;i<array.length();i++){
                                Log.d("444","bxxxx");
                                JSONObject o = array.getJSONObject(i);
                                Current_Passenger items = new Current_Passenger(
                                        o.getString("price")

                                );
                                txtPrice.setText(o.getString("price"));

                            }

                        } catch (JSONException e) {
                            Log.d("kachal",e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("12435",error.getMessage());

//                        Toast.makeText(PassengerCurrentTrip.this,"HohOOO"+error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(PassengerCurrentTrip.this);
        requestQueue.add(stringRequest);


    }



    //Trip End Notification
    private void AcceptRideNotification(){



        tripId = txtHiddenTripId.getText().toString();
        passengerId = txtHiddenPassengerId.getText().toString();
        String vehicleId = "v1558711443502";

        String title = "Fare";
        String body = "Your Trip Amount is "+  txtPrice.getText().toString();
        //String passengerToken = "eSB2w-2RIB8:APA91bEdyhV30dCo5ZM_kfmjvUc02_yLPy4jkfE6mk-aODNUlkTpuUicRqV90YG1oMPGE2YBHtFXafwUvRdZl3c9UCZUyGeOuBBVqzqn3rNEMeSs6sWORM2cre71ngTh321gh5jZm9fc";
        String passengerToken = "eSB2w-2RIB8:APA91bEdyhV30dCo5ZM_kfmjvUc02_yLPy4jkfE6mk-aODNUlkTpuUicRqV90YG1oMPGE2YBHtFXafwUvRdZl3c9UCZUyGeOuBBVqzqn3rNEMeSs6sWORM2cre71ngTh321gh5jZm9fc";


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://plusgo-ce90f.firebaseapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> call = api.PriceNotification(passengerToken,title,body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                 // Toast.makeText(PassengerCurrentTrip.this,response.body().string(),Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


}
