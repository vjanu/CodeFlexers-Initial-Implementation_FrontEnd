/*
 * *
 *  * Created by Ashane Edirisinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/3/19 9:49 PM
 *
 */

package com.example.plusgo.DVPRM;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.plusgo.BaseContent;
import com.example.plusgo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.GRAY;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

public class RatingMainActivity extends AppCompatActivity {

    BaseContent BASECONTENT = new BaseContent();
    StringRequest stringRequest;
    RequestQueue requestQueue;
    SharedPreferences sharedpreferences;
    RatingBar ratingbar;
    LinearLayout layoutcomplement;
    LinearLayout layoutdissatis;
    LinearLayout layoutsubmit;
    Button vehiclebtn,driverbtn,copassengerbtn,submit_rating,submit_complement;
    EditText drivercompliment;
    ImageView vehi,dri,coop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_main);

        sharedpreferences = getSharedPreferences("rating_preference", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();

        submit_rating=(Button)findViewById(R.id.submit_rating);
        submit_complement=(Button)findViewById(R.id.submit_complement);
        drivercompliment=(EditText) findViewById(R.id.drivercompliment);

        ratingbar = (RatingBar)findViewById(R.id.ratingBar);
        layoutcomplement = (LinearLayout)findViewById(R.id.layoutcomplement);
        layoutdissatis = (LinearLayout)findViewById(R.id.layoutdissatis);
        layoutsubmit = (LinearLayout)findViewById(R.id.layoutsubmit);

        vehi = (ImageView)findViewById(R.id.img);
        dri = (ImageView)findViewById(R.id.img1);
        coop = (ImageView)findViewById(R.id.img2);

        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

//                Toast.makeText(getApplicationContext(),"Your Selected Ratings  : " + String.valueOf(rating),Toast.LENGTH_LONG).show();
                if(rating == 5.0){
                    layoutcomplement.setVisibility(View.VISIBLE);
                    layoutdissatis.setVisibility(View.GONE);
                    layoutsubmit.setVisibility(View.VISIBLE);
                    submit_complement.setVisibility(View.VISIBLE);
                    submit_rating.setVisibility(View.GONE);
                }else{
                    layoutcomplement.setVisibility(View.GONE);
                    layoutdissatis.setVisibility(View.VISIBLE);
                    layoutsubmit.setVisibility(View.VISIBLE);
                    submit_rating.setVisibility(View.VISIBLE);
                    submit_complement.setVisibility(View.GONE);
                }
                editor.putString("GivenRating", String.valueOf(rating));
                editor.commit();
            }
        });

        vehiclebtn=(Button)findViewById(R.id.selectedVehicle);
        vehiclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("selectedRateTab", "vehicle");
                editor.commit();
                Intent verify = new Intent(RatingMainActivity.this, RatingPassActivity.class);
                startActivity(verify);
            }
        });

        driverbtn=(Button)findViewById(R.id.selectedDriver);
        driverbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO : INSERT driverId as userId into a shared preference
                editor.putString("selectedRateTab", "driver");
                editor.commit();
                Intent verify = new Intent(RatingMainActivity.this, RatingPassActivity.class);
                startActivity(verify);
            }
        });

        copassengerbtn=(Button)findViewById(R.id.selectedCoPass);
        copassengerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("selectedRateTab", "copassenger");
                editor.commit();
                Intent verify = new Intent(RatingMainActivity.this, CopassengerListActivity.class);
                startActivity(verify);
            }
        });

        submit_rating=(Button)findViewById(R.id.submit_rating);
        submit_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear().commit();
                finish();
            }
        });

        submit_complement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Compliment = String.valueOf(drivercompliment.getText());
                setParmsToSendCompliment(Compliment);
                editor.clear().commit();
                finish();
            }
        });
        lockButtons();

    }
    @Override
    public void onResume(){
        super.onResume();
        lockButtons();
        sharedpreferences = getSharedPreferences("rating_preference", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove("selectedKey").apply();
        editor.remove("CalRating").apply();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lockButtons();
            }
        }, 1000);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        sharedpreferences = getSharedPreferences("rating_preference", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear().apply();
    }

    public void lockButtons(){
        Date date = new Date();
        long timeMilli = date.getTime();
        sharedpreferences = getSharedPreferences("rating_preference", Context.MODE_PRIVATE);

        String DONEdriver = (sharedpreferences.getString("done_ratedriver", "NO"));
        String DONEcopassenger = (sharedpreferences.getString("done_copassenger", "NO"));
        String DONEvehicle = (sharedpreferences.getString("done_ratevehicle", "NO"));

        if(DONEvehicle.equals("YES")){
//            vehiclebtn.setEnabled(false);
            vehiclebtn.setTextColor(GRAY);
            vehi.setImageResource(R.drawable.star);
        }
        if(DONEdriver.equals("YES")){
            driverbtn.setTextColor(GRAY);
            dri.setImageResource(R.drawable.star);
        }
        if(DONEcopassenger.equals("YES")){
            copassengerbtn.setTextColor(GRAY);
            coop.setImageResource(R.drawable.star);
        }
    }

    //To set correct parameters to enter rating to the DB
    public void setParmsToSendCompliment(String Compliment){
        sharedpreferences = getSharedPreferences("rating_preference", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        String UserType = "driver";
        String TripId = (sharedpreferences.getString("TripId", "00001"));
        String UserID = (sharedpreferences.getString("UserID", "U000001"));//TODO:INSERT DRIVER ID
        String RatedBy = (sharedpreferences.getString("RatedBy", "U0000002"));
        String GivenRating = "5.0";
        String CalRating = "5.0";

        drivercomplimentpostrequest(TripId, UserID, UserType, RatedBy, GivenRating, CalRating, Compliment);
    }

    //insert ratings related to driver or co-passenger into the database
    public void drivercomplimentpostrequest(String TripId,String UserID,String UserType,String RatedBy,
                                          String GivenRating,String CalRating,String Compliment) {
        try {
            String URL = null;
            requestQueue = Volley.newRequestQueue(this.getApplicationContext());

                URL = BASECONTENT.IpAddress +"/ratings/driver-rating";

            Log.e("URL", URL);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("TripId", TripId);
            jsonBody.put("UserID", UserID);
            jsonBody.put("RatedBy", RatedBy);
            jsonBody.put("GivenRating", GivenRating);
            jsonBody.put("CalculatedRating", CalRating);
            jsonBody.put("Compliment", Compliment);

            final String mRequestBody = jsonBody.toString();

            Log.e("VOLLEY", TripId+":"+UserID+":"+UserType+":"+RatedBy+":"+GivenRating+":"+CalRating);

            stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("LOG_VOLLEY", response);
                    Toast.makeText(getBaseContext(), "Your compliment is added", Toast.LENGTH_LONG).show();
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
}
