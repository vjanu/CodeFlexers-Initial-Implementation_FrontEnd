/*
 * *
 *  * Created by Ashane Edirisinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/18/19 4:33 PM
 *
 */

package com.example.plusgo.DVPRM;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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

public class RatingDriverActivity extends AppCompatActivity {

    StringRequest stringRequest;
    RequestQueue requestQueue;
    BaseContent BASECONTENT = new BaseContent();
    SharedPreferences sharedpreferences;
    SharedPreferences sharedpreferences2;
    SharedPreferences sharedpreferences3;
    RatingBar ratingbarD;
    CardView cardview,cardview2;
    LinearLayout layoutSubmitD;
    Button btnOther,keywd1,keywd2,keywd3,keywd4,keywd5;
    EditText sentimentByDriver;

    Button btnsubmitdriver;

    private JsonArrayRequest request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_driver);

        sharedpreferences2 = getSharedPreferences("userstore", Context.MODE_PRIVATE);
        String tempRatedBy = sharedpreferences2.getString("UId", "U0000000030");

        sharedpreferences3 = getSharedPreferences("ratingStore", Context.MODE_PRIVATE);
        String tempPassengerID = sharedpreferences2.getString("passengerId", "U0000000050");
        String tempTripID = sharedpreferences2.getString("tripId", "T0000000050");

        sharedpreferences = getSharedPreferences("rating_preference", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        //Setting driver ID
        editor.putString("RatedBy", tempRatedBy);
        editor.putString("UserID", tempPassengerID);
        editor.putString("TripId", tempTripID);
        editor.commit();

        ratingbarD = (RatingBar)findViewById(R.id.ratingBarD);
        cardview =(CardView)findViewById(R.id.cardViewForDriver);
        cardview2 =(CardView)findViewById(R.id.cardViewForDriver2);
        layoutSubmitD=(LinearLayout) findViewById(R.id.layoutsubmitD);
        ratingbarD.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                editor.putString("GivenRating", String.valueOf(rating));
                editor.apply();
//                Toast.makeText(getApplicationContext(),"Your Selected Ratings  : " + String.valueOf(rating),Toast.LENGTH_LONG).show();
                if(rating == 5.0){
                    cardview.setVisibility(View.GONE);
                    layoutSubmitD.setVisibility(View.VISIBLE);
                    cardview2.setVisibility(View.GONE);
                    editor.putString("selectedKeyDriver", "none");

                }else{
                    cardview.setVisibility(View.VISIBLE);
                    layoutSubmitD.setVisibility(View.GONE);
                    cardview2.setVisibility(View.GONE);
                }

            }
        });

        btnOther =(Button)findViewById(R.id.keywd6);
        btnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardview.setVisibility(View.GONE);
                layoutSubmitD.setVisibility(View.VISIBLE);
                cardview2.setVisibility(View.VISIBLE);
            }
        });

        sentimentByDriver=(EditText) findViewById(R.id.sentimentByDriver);

        btnsubmitdriver =(Button)findViewById(R.id.btnsubmitdriver);
        btnsubmitdriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String senti = String.valueOf(sentimentByDriver.getText());
                try {
                    if (!senti.equals("")) {
                        jsonrequestforsentimentcheck(senti);
                        Log.e("BUTTON SUBMIT", "has sentiment");
                    } else {
                        setParmsToSend("");
                        Log.e("BUTTON SUBMIT", "no sentiment");
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();

                }finally {
                    editor.clear().commit();
//                    finish();
                }
            }
        });

        keywd1 =(Button)findViewById(R.id.keywd1);
        keywd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("selectedKeyDriver", keywd1.getText().toString());
                editor.commit();
                setParmsToSend("");
            }
        });

        keywd2 =(Button)findViewById(R.id.keywd2);
        keywd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("selectedKeyDriver", keywd2.getText().toString());
                editor.commit();
                setParmsToSend("");
            }
        });

        keywd3 =(Button)findViewById(R.id.keywd3);
        keywd3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("selectedKeyDriver", keywd3.getText().toString());
                editor.commit();
                setParmsToSend("");
            }
        });

        keywd4 =(Button)findViewById(R.id.keywd4);
        keywd4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("selectedKeyDriver", keywd4.getText().toString());
                editor.commit();
                setParmsToSend("");
            }
        });

        keywd5 =(Button)findViewById(R.id.keywd5);
        keywd5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("selectedKeyDriver", keywd5.getText().toString());
                editor.commit();
                setParmsToSend("");
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        sharedpreferences = getSharedPreferences("rating_preference", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear().apply();
    }

    //To set correct parameters to enter rating to the DB
    public void setParmsToSend(String Sentiment){

        sharedpreferences = getSharedPreferences("rating_preference", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();

        String UserType = (sharedpreferences.getString("selectedRateTab", "passenger"));
        String TripId = (sharedpreferences.getString("TripId", "00001"));
        String UserID = (sharedpreferences.getString("UserID", "U000001"));//PASSENGER ID
        String RatedBy = (sharedpreferences.getString("RatedBy", "U0000002"));//DRIVER ID
//        String GivenRating = (sharedpreferences.getString("GivenRating", "5"));
        String GivenRating = String.valueOf(ratingbarD.getRating());
        String CalRating = (sharedpreferences.getString("CalRating", GivenRating));
        String Dissatis = (sharedpreferences.getString("selectedKeyDriver", "none"));

        ratingdriverpostrequest(TripId, UserID, UserType, RatedBy, GivenRating, CalRating, Dissatis, Sentiment);
    }

    //insert ratings related to driver or co-passenger into the database
    public void ratingdriverpostrequest(String TripId,String UserID,String UserType,String RatedBy,
                                          String GivenRating,String CalRating,String Dissatis,String Sentiment) {
        try {
            String URL = null;
            RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());

            URL = BASECONTENT.IpAddress +"/ratings/passenger-rating";
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

            Log.e("VOLLEY", TripId+":"+UserID+":"+RatedBy+":"+GivenRating+":"+CalRating);
            stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("LOG_VOLLEY", response);
                    Toast.makeText(getBaseContext(), "Your rating on passenger is added", Toast.LENGTH_LONG).show();
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
        } finally {
            sharedpreferences = getSharedPreferences("rating_preference", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear().apply();
            finish();
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

                        Log.e("JSON_URL", sharedpreferences.getString("GivenRating", "5"));
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
        requestQueue = Volley.newRequestQueue(RatingDriverActivity.this);
        requestQueue.add(request);
    }
}
