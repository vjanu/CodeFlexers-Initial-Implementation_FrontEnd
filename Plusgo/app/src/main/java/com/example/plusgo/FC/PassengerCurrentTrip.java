package com.example.plusgo.FC;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.Timer;
import java.util.TimerTask;

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

    private String JSON_URL_POST_MIGRATE_DATA = BASECONTENT.IpAddress + "/trip/migrate/currentPassengers";

    private final String JSON_PUT_UPDATE_OFFER_RIDE_START = BASECONTENT.IpAddress+"/trip/offerRide/start/";
    private final String JSON_PUT_UPDATE_OFFER_RIDE_END = BASECONTENT.IpAddress+"/trip/offerRide/end/";

    private final String JSON_GET_CURRENT_PASSENGER = BASECONTENT.IpAddress+"/trip/";
    private final String JSON_DELETE_CURRENT_PASSENGERS = BASECONTENT.IpAddress+"/trip/delete/currentPassengers/";

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
        driverId = user.getString("UId", null);

        //Timer Stop which is started MapCurrentPassengerActivity
//        MapCurrentPassengerActivity mapCurrentPassengerActivity = new MapCurrentPassengerActivity();
//        mapCurrentPassengerActivity.timer.cancel();

        //Get Values from the Intend
        Intent intent = getIntent();
        String TripId = intent.getStringExtra("TripId");
        String PassengerId = intent.getStringExtra("PassengerId");
        String Name = intent.getStringExtra("Name");
        String startpoint = intent.getStringExtra("Source");
        String endpoint = intent.getStringExtra("Destination");
        String Token = intent.getStringExtra("Token");
        String Status = intent.getStringExtra("Status");
        String userImage = intent.getStringExtra("userImage");


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

        //Value Set for the initiallize variables
        txtHiddenTripId.setText(TripId);
        txtHiddenPassengerId.setText(PassengerId);
        txtPassengerName.setText(Name);
        txtStartPoint.setText(startpoint);
        txtEndPoint.setText(endpoint);
        txtHiddenToken.setText(Token);
        txtTripStatus.setText(Status);
        //Need To user Image to the imgLogo
        txtHiddenCurrentMileage.setText("4099");

        Log.d("status",Status);

        GetCurrentPassengers();
        GetDetailsOfSpecificCurrentUser();

        //Shares Preference for Rating
        SharedPreferences.Editor ratingStore = getSharedPreferences("ratingStore", MODE_PRIVATE).edit();
        ratingStore.putString("passengerId", passengerId);
        ratingStore.putString("vehicleId", "V1561391202510");
        ratingStore.putString("tripId", txtHiddenTripId.getText().toString());
        ratingStore.putString("driverId", driverId);
        ratingStore.apply();

        SharedPreferences getRatingDetails = getSharedPreferences("ratingStore",MODE_PRIVATE);
        String driversId = getRatingDetails.getString("driverId", null);
        String passengerId = getRatingDetails.getString("passengerId", null);
        String tripId = getRatingDetails.getString("tripId", null);
        String vehicleId = getRatingDetails.getString("vehicleId", null);

        Log.d("Test","Test");
        Log.d("#@#driverId",driversId);
        Log.d("#@#passengerId",passengerId);
        Log.d("#@#tripId",tripId);
        Log.d("#@#vehicleId",vehicleId);


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
                txtTripStatus.setText("Trip Started");
                if(currentPassengers == 0)
                {
                    UpdateStatusWhenDriverStartTrip();
                }

                Intent i = new Intent(PassengerCurrentTrip.this, MapCurrentPassengerActivity.class);
                view.getContext().startActivity(i);

                Log.d("Click BtnStart","execute");
            }
        });

        btnEndTrip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                double currentPassengers = Double.parseDouble(txtHiddenCurrentPassengers.getText().toString());

                Log.d("Click BtnStart","check");
                //GetDetailsOfSpecificCurrentUser();

                UpdateStatusWhenTripEnd();
                UpdateFareCalculation();
                UpdateFareForGetOffUser();
               // PriceNotification();

                if(currentPassengers == 1){
                    UpdateStatusWhenDriverEndTrip();
                    //migrateDataToTripHistory();//TODO::Tempory comment need to check several condition before execute this method
                }

                btnEndTrip.setVisibility(View.GONE);
                btnStartTrip.setVisibility(View.GONE);
                PriceOfThePAssenger();

                lblPrice.setVisibility(View.VISIBLE);
                txtPrice.setVisibility(View.VISIBLE);
                txtTripStatus.setText("Trip Ended");
                PriceNotification();

//                Intent verify = new Intent(PassengerCurrentTrip.this, RatingDriverActivity.class);
//                startActivity(verify);
                Log.d("Click BtnStart","execute");
            }
        });



    }

//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(PassengerCurrentTrip.this,MapCurrentPassengerActivity.class);
//        startActivity(intent);
//        finish();
//    }

    //When Start the trip passenger trip status will be changed and vehicle current mileage set to the current_passenger Table
    public void UpdateStatusWhenTripStart() {
        try {

            String tripId = txtHiddenTripId.getText().toString();
            String passengerId = txtHiddenPassengerId.getText().toString();
            String dId = driverId;
            //String driverId = "U1558711443513";


            Log.d("$$$$$$$$$$$$DriverID",driverId);
            Log.d("tripId",tripId);
            Log.d("passengerId",passengerId);

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("trip_status", 1);
                jsonObject.put("startMileage", txtHiddenCurrentMileage.getText().toString());
                final String mRequestBody = jsonObject.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.PUT, JSON_PUT_UPDATE_START_TRIP+tripId+"/"+passengerId+"/"+dId, new Response.Listener<String>() {
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
            String driverId = "U1558711443513";


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


    public void GetDetailsOfSpecificCurrentUser() {


        tripId = txtHiddenTripId.getText().toString();
        passengerId = txtHiddenPassengerId.getText().toString();
        String driverId = "U1558711443513";

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
        tripId = txtHiddenTripId.getText().toString();
        String driverId = "U1558711443513";

        //   Log.e("JSON_URL",JSON_URL+username+"/"+password);
        request = new JsonArrayRequest(JSON_URL_GET_TRIP_STARTED_USERS+tripId+"/"+driverId, new Response.Listener<JSONArray>() {

            public void onResponse(JSONArray response) {
                Log.d("response", String.valueOf(response));
                JSONObject jsonObject = null;
                if(response.length()>=0){
                for (int i = 0; i <= response.length(); i++) {
//                    progressDialog.dismiss();
                    try {


                        jsonObject = response.getJSONObject(i);

                        Log.d("jsonObjectjsonObject", jsonObject.toString());
                        Log.d("123passengerId", jsonObject.getString("passengerId").toString());
                        Log.d("123startMileage", jsonObject.getString("startMileage").toString());
                        Log.d("123price", jsonObject.getString("price").toString());

                        tripId = txtHiddenTripId.getText().toString();
                        String driverId = "U1558711443513";
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

                        Log.d("123", "!@###");
                        //Put Response Start
                        //RequestQueue requestQueue = Volley.newRequestQueue(this);
                        JSONObject jsonObject1 = new JSONObject();
                        Log.d("231", "!@###");
                        jsonObject1.put("price", updatePrice);
                        jsonObject1.put("startMileage", txtHiddenCurrentMileage.getText().toString());
                        Log.d("@@updatePrice@@", String.valueOf(updatePrice));
                        Log.d("@@startMileage@@", String.valueOf(txtHiddenCurrentMileage.getText().toString()));

                        final String mRequestBody = jsonObject1.toString();
                        Log.d("321", "!@###");
                        StringRequest stringRequest = new StringRequest(Request.Method.PUT, JSON_PUT_UPDATE_FARE_CALCULATION + tripId + "/" + passengerId + "/" + driverId, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                Log.d("3211", "!@###");
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
        Log.d("@@#^&*driverId" , driverId);
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading Data...");
//        progressDialog.show();


        //   Log.e("JSON_URL",JSON_URL+username+"/"+password);
        request = new JsonArrayRequest(JSON_URL_GET_DROPOFF_USER_DETAILS+tripId+"/"+passengerId+"/"+driverId, new Response.Listener<JSONArray>() {

            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;
                if(response.length()>0){
                    for (int i = 0; i <= response.length(); i++) {
//                        progressDialog.dismiss();
                        try {


                            jsonObject = response.getJSONObject(i);
                            Log.d("@@@passengerId", jsonObject.getString("passengerId").toString());
                            Log.d("@@@startMileage", jsonObject.getString("startMileage").toString());
                            Log.d("@@@price", jsonObject.getString("price").toString());

                            tripId = txtHiddenTripId.getText().toString();
                            String driverId = "U1558711443513";
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
        Log.d("PassengerId1111",passengerId );
        String driverId = "U1558711443513";



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
                                txtPrice.setText("Rs." +o.getString("price"));

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

    //Update Status as a one when Driver Start the ride
    public void UpdateStatusWhenDriverStartTrip() {
        try {


            tripId = txtHiddenTripId.getText().toString();

            Log.d("tripId",tripId);


            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tripStatus", 1);

            final String mRequestBody = jsonObject.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.PUT, JSON_PUT_UPDATE_OFFER_RIDE_START+tripId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_VOLLEY", response);
                    Toast.makeText(PassengerCurrentTrip.this, "---Trip Started---", Toast.LENGTH_SHORT).show();
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

    //Update Status as a one when Driver End the ride
    public void UpdateStatusWhenDriverEndTrip() {
        try {


            tripId = txtHiddenTripId.getText().toString();

            Log.d("tripId",tripId);


            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tripStatus", 2);

            final String mRequestBody = jsonObject.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.PUT, JSON_PUT_UPDATE_OFFER_RIDE_END+tripId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_VOLLEY", response);
                    Toast.makeText(PassengerCurrentTrip.this, "---Trip Ended---", Toast.LENGTH_SHORT).show();
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


    public void migrateDataToTripHistory() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();

        tripId = txtHiddenTripId.getText().toString();
        driverId = "U1558711443513"; // TODO:Change as a User ID

        Log.d("TripIDD",txtHiddenTripId.getText().toString());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_GET_CURRENT_PASSENGER+tripId,
                //StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_GET_CURRENT_PASSENGER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("sdss",response);
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array =jsonObject.getJSONArray("currentUsers");
                            Log.d("array.length()", String.valueOf(array.length()));
                            for(int i=0;i<array.length();i++){
                                Log.d("444","bxxxx");
                                JSONObject o = array.getJSONObject(i);
                                Current_Passenger items = new Current_Passenger(
                                        o.getString("FullName"),
                                        o.getString("passengerId"),
                                        o.getString("source"),
                                        o.getString("destination"),
                                        o.getString("trip_status"),
                                        o.getDouble("price")
                                        );
                                 //Current Passenger data add to the trip History Table
                                RequestQueue requestQueue = Volley.newRequestQueue(PassengerCurrentTrip.this);
                                JSONObject jsonObject1 = new JSONObject();
                                jsonObject1.put("tripId", tripId);
                                jsonObject1.put("passengerId", o.getString("passengerId"));
                                jsonObject1.put("driverId", driverId);
                                jsonObject1.put("source", o.getString("source"));
                                jsonObject1.put("destination", o.getString("destination"));
                                jsonObject1.put("status",o.getString("trip_status"));
                                jsonObject1.put("price", o.getDouble("price"));
                                final String mRequestBody = jsonObject1.toString();

                                StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_POST_MIGRATE_DATA, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.i("LOG_VOLLEY", response);
                                        Toast.makeText(PassengerCurrentTrip.this, "Send Request", Toast.LENGTH_SHORT).show();
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


                                Log.d("for888",items.toString());
                            }

                            //To Delete current Passengers of the relevent Trip Id
                            deleteCurrentPassengers();


                        } catch (JSONException e) {
                            Log.d("kachal",e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("12435",error.getMessage());

                        Toast.makeText(PassengerCurrentTrip.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(PassengerCurrentTrip.this);
        requestQueue.add(stringRequest);
    }

    public void deleteCurrentPassengers() {
        try {


            tripId = txtHiddenTripId.getText().toString();

            Log.d("tripId",tripId);


            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tripId", tripId);

            final String mRequestBody = jsonObject.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, JSON_DELETE_CURRENT_PASSENGERS+tripId, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_VOLLEY", response);
                    Toast.makeText(PassengerCurrentTrip.this, "---------", Toast.LENGTH_SHORT).show();
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

    //Trip End Notification
    private void PriceNotification(){

        tripId = txtHiddenTripId.getText().toString();
        passengerId = txtHiddenPassengerId.getText().toString();

        //String vehicleId = "v1558711443502";

        String title = "Fare";
        String body = "Your Trip Amount is "+  txtPrice.getText().toString();
        //String passengerToken = "eSB2w-2RIB8:APA91bEdyhV30dCo5ZM_kfmjvUc02_yLPy4jkfE6mk-aODNUlkTpuUicRqV90YG1oMPGE2YBHtFXafwUvRdZl3c9UCZUyGeOuBBVqzqn3rNEMeSs6sWORM2cre71ngTh321gh5jZm9fc";
        String passengerToken = "c3OpPkFkbvI:APA91bFgtVj-0GaKmGSLXb38NkmJgQk35TotbEP1XhrMWnrM3wJ7NzOmfYtMmHstxu_FINB3vSi8l8h5JpTncxqA_RUc53QHH1SgnS3skpsiNLhHXzH2YnxatXl6jFlFJCDEdtPYooae";


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://plusgo-ce90f.firebaseapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> call = api.PriceNotification(passengerToken,title,body,driverId,tripId);

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
