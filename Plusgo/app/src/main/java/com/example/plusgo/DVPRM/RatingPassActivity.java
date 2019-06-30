/*
 * *
 *  * Created by Ashane Edirisinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/6/19 11:59 PM
 *
 */

package com.example.plusgo.DVPRM;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.example.plusgo.BaseContent;
import com.example.plusgo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.GRAY;

public class RatingPassActivity extends AppCompatActivity {
    StringRequest stringRequest;
    RequestQueue requestQueue;
    BaseContent BASECONTENT = new BaseContent();
    SharedPreferences sharedpreferences;
    LinearLayout layoutrew,layoutReport;
    EditText writtenSentimnt;
    Button btnother,btnRpt;
    Button back,rating_passDone;
    Button keyw1,keyw2,keyw3,keyw4,keyw5,keyw6;
    String vehiclekey[]={"Air Condition","Comfortability","Cleanliness","Noise","Breaks","Vehicle Quality"};
    String driverkey[]={"Bad Navigation","Professionalism","Cleanliness","Service","Music","Reckless Driving"};
    String copassengerkey[]={"Behaviour","Professionalism","Cleanliness","Attitude","Timeliness","Disturbance"};

    private JsonArrayRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_pass);

        DisplayMetrics dm1 = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm1);

        int width = dm1.widthPixels;
        int height = dm1.heightPixels;

        getWindow().setLayout((int)(width*.95),(int)(height*.70));

        layoutrew = (LinearLayout)findViewById(R.id.layoutownrew);
        layoutReport= (LinearLayout)findViewById(R.id.layoutrep);
        btnRpt =(Button)findViewById(R.id.btnRpt);
        btnRpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedpreferences = getSharedPreferences("rating_preference", Context.MODE_PRIVATE);

                String UserID = (sharedpreferences.getString("UserID", "U00000111"));
                String RatedBy = (sharedpreferences.getString("RatedBy", "U00000050"));
                blockdriver(RatedBy,UserID);
                finish();
            }
        });
        btnother =(Button)findViewById(R.id.btnother);
        btnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyw1.setTextColor(BLACK);
                keyw2.setTextColor(BLACK);
                keyw3.setTextColor(BLACK);
                keyw4.setTextColor(BLACK);
                keyw5.setTextColor(BLACK);
                keyw6.setTextColor(BLACK);
                layoutrew.setVisibility(View.VISIBLE);
                layoutReport.setVisibility(View.GONE);
            }
        });

        back =(Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        keyw1 =(Button)findViewById(R.id.keyw1);
        keyw2 =(Button)findViewById(R.id.keyw2);
        keyw3 =(Button)findViewById(R.id.keyw3);
        keyw4 =(Button)findViewById(R.id.keyw4);
        keyw5 =(Button)findViewById(R.id.keyw5);
        keyw6 =(Button)findViewById(R.id.keyw6);

        sharedpreferences = getSharedPreferences("rating_preference", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        String selectedTab = (sharedpreferences.getString("selectedRateTab", "vehicle"));

        if(selectedTab.equals("vehicle")) {
            keyw1.setText(vehiclekey[0]);
            keyw2.setText(vehiclekey[1]);
            keyw3.setText(vehiclekey[2]);
            keyw4.setText(vehiclekey[3]);
            keyw5.setText(vehiclekey[4]);
            keyw6.setText(vehiclekey[5]);

        }else if(selectedTab.equals("driver")){
            keyw1.setText(driverkey[0]);
            keyw2.setText(driverkey[1]);
            keyw3.setText(driverkey[2]);
            keyw4.setText(driverkey[3]);
            keyw5.setText(driverkey[4]);
            keyw6.setText(driverkey[5]);
            layoutReport.setVisibility(View.VISIBLE);
        }else {
            keyw1.setText(copassengerkey[0]);
            keyw2.setText(copassengerkey[1]);
            keyw3.setText(copassengerkey[2]);
            keyw4.setText(copassengerkey[3]);
            keyw5.setText(copassengerkey[4]);
            keyw6.setText(copassengerkey[5]);

            Intent intent = getIntent();
            String selected_copassenger_id = intent.getStringExtra("selected_copassenger_id");
            editor.putString("UserID", selected_copassenger_id);
            editor.apply();

//            Toast.makeText(getBaseContext(), "Copassenger ID"+selected_copassenger_id, Toast.LENGTH_LONG).show();
        }

        keyw1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutrew.setVisibility(View.GONE);
                ((Button)v).setTextColor(GRAY);
                keyw2.setTextColor(BLACK);
                keyw3.setTextColor(BLACK);
                keyw4.setTextColor(BLACK);
                keyw5.setTextColor(BLACK);
                keyw6.setTextColor(BLACK);
                editor.putString("selectedKey", keyw1.getText().toString());
                editor.commit();
            }
        });

        keyw2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutrew.setVisibility(View.GONE);
                ((Button)v).setTextColor(GRAY);
                keyw1.setTextColor(BLACK);
                keyw3.setTextColor(BLACK);
                keyw4.setTextColor(BLACK);
                keyw5.setTextColor(BLACK);
                keyw6.setTextColor(BLACK);
                editor.putString("selectedKey", keyw2.getText().toString());
                editor.commit();
            }
        });
        keyw3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutrew.setVisibility(View.GONE);
                ((Button)v).setTextColor(GRAY);
                keyw1.setTextColor(BLACK);
                keyw2.setTextColor(BLACK);
                keyw4.setTextColor(BLACK);
                keyw5.setTextColor(BLACK);
                keyw6.setTextColor(BLACK);
                editor.putString("selectedKey", keyw3.getText().toString());
                editor.commit();
            }
        });
        keyw4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutrew.setVisibility(View.GONE);
                ((Button)v).setTextColor(GRAY);
                keyw1.setTextColor(BLACK);
                keyw2.setTextColor(BLACK);
                keyw3.setTextColor(BLACK);
                keyw5.setTextColor(BLACK);
                keyw6.setTextColor(BLACK);
                editor.putString("selectedKey", keyw4.getText().toString());
                editor.commit();
            }
        });
        keyw5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutrew.setVisibility(View.GONE);
                ((Button)v).setTextColor(GRAY);
                keyw1.setTextColor(BLACK);
                keyw2.setTextColor(BLACK);
                keyw3.setTextColor(BLACK);
                keyw4.setTextColor(BLACK);
                keyw6.setTextColor(BLACK);
                editor.putString("selectedKey", keyw5.getText().toString());
                editor.commit();
            }
        });
        keyw6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutrew.setVisibility(View.GONE);
                ((Button)v).setTextColor(GRAY);
                keyw1.setTextColor(BLACK);
                keyw2.setTextColor(BLACK);
                keyw3.setTextColor(BLACK);
                keyw4.setTextColor(BLACK);
                keyw5.setTextColor(BLACK);
                editor.putString("selectedKey", keyw6.getText().toString());
                editor.commit();
            }
        });
        writtenSentimnt=(EditText) findViewById(R.id.writtenSentimnt);

        rating_passDone =(Button)findViewById(R.id.rating_passDone);
        rating_passDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String senti = String.valueOf(writtenSentimnt.getText());
                try {
                    if (!senti.equals("")) {
                        jsonrequestforsentimentcheck(senti);
                    } else {
                        setParmsToSend(senti);
                    }
                }catch (Exception e){

                }

                finish();
            }
        });
    }

    //To set correct parameters to enter rating to the DB
    public void setParmsToSend(String Sentiment){

        sharedpreferences = getSharedPreferences("rating_preference", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        String UserType = (sharedpreferences.getString("selectedRateTab", "vehicle"));
        String TripId = (sharedpreferences.getString("TripId", "00001"));
        String UserID = (sharedpreferences.getString("UserID", "U000001"));
        String RatedBy = (sharedpreferences.getString("RatedBy", "U0000002"));
        String GivenRating = (sharedpreferences.getString("GivenRating", "5"));
        String CalRating = (sharedpreferences.getString("CalRating", GivenRating));
        String Dissatis = (sharedpreferences.getString("selectedKey", "none"));

        String VehicleId = (sharedpreferences.getString("vehicleId", "V0000000045"));

        if(UserType.equals("vehicle")) {
            ratingvehiclepostrequest(TripId,VehicleId, RatedBy, GivenRating, CalRating, Dissatis, Sentiment);
            editor.putString("done_ratevehicle", "YES");
            editor.commit();
        }else{
            ratingpersonalpostrequest(TripId, UserID, UserType, RatedBy, GivenRating, CalRating, Dissatis, Sentiment);

            if(UserType.equals("driver")) {
                editor.putString("done_ratedriver", "YES");
                editor.commit();
            }else{
                editor.putString("done_copassenger", "YES");
                editor.commit();
            }
        }
    }

    //Block drivers
    public void blockdriver(String PUID,String DUID) {
        try {
            String URL = null;
            requestQueue = Volley.newRequestQueue(this.getApplicationContext());
                URL = BASECONTENT.IpAddress +"/ratings/reportDrivers";

            Log.e("URL", URL);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("PUID", PUID);
            jsonBody.put("DUID", DUID);

            final String mRequestBody = jsonBody.toString();

            Log.e("VOLLEY", PUID+":"+DUID);

            stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("LOG_VOLLEY", response);
                    Toast.makeText(getBaseContext(), "Driver is added to block list", Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_VOLLEY", error.toString());
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

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //insert ratings related to driver or co-passenger into the database
    public void ratingpersonalpostrequest(String TripId,String UserID,String UserType,String RatedBy,
                                  String GivenRating,String CalRating,String Dissatis,String Sentiment) {
        try {
            String URL = null;
            requestQueue = Volley.newRequestQueue(this.getApplicationContext());
            if(UserType.equals("driver")) {
                URL = BASECONTENT.IpAddress +"/ratings/driver-rating";
            }else{
                URL = BASECONTENT.IpAddress+"/ratings/copassenger-rating";
            }
            Log.e("URL", URL);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("TripId", TripId);
            jsonBody.put("UserID", UserID);
//            jsonBody.put("UserType", UserType);
            jsonBody.put("RatedBy", RatedBy);
            jsonBody.put("GivenRating", GivenRating);
            jsonBody.put("CalculatedRating", CalRating);
            jsonBody.put("Dissatisfaction", Dissatis);
            jsonBody.put("Sentiment", Sentiment);
            final String mRequestBody = jsonBody.toString();

            Log.e("VOLLEY", TripId+":"+UserID+":"+UserType+":"+RatedBy+":"+GivenRating+":"+CalRating);

            stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("LOG_VOLLEY", response);
                    Toast.makeText(getBaseContext(), "Your rating on person is added", Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_VOLLEY", error.toString());
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

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //insert ratings related to driver or co-passenger into the database
    public void ratingvehiclepostrequest(String tripId,String vehicleId,String RatedBy,
                                          String GivenRating,String CalRating,String Dissatis,String Sentiment) {
        try {
            String URL = null;
            RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());

            URL = BASECONTENT.IpAddress +"/ratings/vehicle-rating";
            Log.e("URL", URL);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("tripId", tripId);
            jsonBody.put("vehicleId", vehicleId);
            jsonBody.put("RatedBy", RatedBy);
            jsonBody.put("GivenRating", GivenRating);
            jsonBody.put("CalculatedRating", CalRating);
            jsonBody.put("Dissatisfaction", Dissatis);
            jsonBody.put("Sentiment", Sentiment);
            final String mRequestBody = jsonBody.toString();

            Log.e("VOLLEY", tripId+":"+vehicleId+":"+RatedBy+":"+GivenRating+":"+CalRating);
            stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("LOG_VOLLEY", response);
                    Toast.makeText(getBaseContext(), "Your rating on vehicle is added", Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_VOLLEY", error.toString());
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

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    //Retrieve sentiment rating
    private void jsonrequestforsentimentcheck(String sentiment) {

        String JSON_URL = BASECONTENT.DVPRMBASEIPROUTE +":8090/sentiment/"+sentiment;

        final String senti = sentiment;
        final String finalJSON_URL = JSON_URL;
        request = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                Log.e("JSON_URL", finalJSON_URL);
                JSONObject jsonObject = null;
                if(response.length() > 0){
                    try{
                        jsonObject = response.getJSONObject(0);

                        String ResultRating = jsonObject.getString("ResultRating");
                        String ResultRatingType= jsonObject.getString("Type");

                        SharedPreferences.Editor editor = getSharedPreferences("rating_preference", MODE_PRIVATE).edit();
                        editor.putString("CalRating", ResultRating);
                        editor.putString("CalRatingType", ResultRatingType);
                        editor.commit();

                        Toast.makeText(getBaseContext(), "CalculatedRating: "+ResultRating+" type: "+ResultRatingType, Toast.LENGTH_LONG).show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setParmsToSend(senti);
                            }
                        }, 100);
//                        setParmsToSend(senti);

                    }catch (JSONException e){

                        e.printStackTrace();
                        Log.e("JSONREQUEST","ERROR");
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("JSONREQUEST_ERROR",error.toString());
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue = Volley.newRequestQueue(RatingPassActivity.this);
        requestQueue.add(request);
    }
}
