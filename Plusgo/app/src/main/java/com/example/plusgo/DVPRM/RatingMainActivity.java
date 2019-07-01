/*
 * *
 *  * Created by Ashane Edirisinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/3/19 9:49 PM
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.example.plusgo.BaseContent;
import com.example.plusgo.Notification.MyFirebaseMessagingService;
import com.example.plusgo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.GRAY;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

public class RatingMainActivity extends AppCompatActivity {

    //Service to write average rating to CSV
    private String TAG = "RatingMainActivity";
    IResult mResultCallback = null;
    VolleyServiceForCSV mVolleyService;

    BaseContent BASECONTENT = new BaseContent();
    String VEHICLERETRIEVE_URL = BASECONTENT.IpAddress + "/vehicle/specificVID/";
    StringRequest stringRequest;
    RequestQueue requestQueue;
    SharedPreferences sharedpreferences;
    SharedPreferences sharedpreferences2;
    RatingBar ratingbar;
    LinearLayout layoutcomplement;
    LinearLayout layoutdissatis;
    LinearLayout layoutsubmit;
    Button vehiclebtn, driverbtn, copassengerbtn, submit_rating, submit_complement;
    EditText drivercompliment;
    ImageView vehi, dri, coop;
    JsonArrayRequest vehiclerequest;
    RequestQueue vehiclerequestQueue;
    String notificationBody;
    String driverId;
    String tripId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_main);

        //Service to write average rating to CSV
        initVolleyCallback();
        mVolleyService = new VolleyServiceForCSV(mResultCallback, this);

        try {
            notificationBody = MyFirebaseMessagingService.NotificationBodyCatcher;
            Log.d("Check333", notificationBody);
            driverId = notificationBody.split("\n")[7];
            tripId = notificationBody.split("\n")[8];
        } catch (Exception e) {
            notificationBody = "4545465465465465456464645645645645646565";//TODO:comment
            Log.d("Check333", notificationBody);
            driverId = "U1558711443507";//TODO:comment
            tripId = "O1558711443513";//TODO:comment
        }
        vehiclejsonrequest(driverId);

        //Retrieve UID and setting into another share preference
        sharedpreferences2 = getSharedPreferences("userstore", Context.MODE_PRIVATE);
        String tempRatedBy = sharedpreferences2.getString("UId", "U0000000020");

        sharedpreferences = getSharedPreferences("rating_preference", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        //Setting passenger ID
        editor.putString("RatedBy", tempRatedBy);
        editor.putString("TripId", tripId);
        editor.putString("UserID", driverId);
        editor.commit();

        submit_rating = (Button) findViewById(R.id.submit_rating);
        submit_complement = (Button) findViewById(R.id.submit_complement);
        drivercompliment = (EditText) findViewById(R.id.drivercompliment);

        ratingbar = (RatingBar) findViewById(R.id.ratingBar);
        layoutcomplement = (LinearLayout) findViewById(R.id.layoutcomplement);
        layoutdissatis = (LinearLayout) findViewById(R.id.layoutdissatis);
        layoutsubmit = (LinearLayout) findViewById(R.id.layoutsubmit);

        vehi = (ImageView) findViewById(R.id.img);
        dri = (ImageView) findViewById(R.id.img1);
        coop = (ImageView) findViewById(R.id.img2);

        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

//                Toast.makeText(getApplicationContext(),"Your Selected Ratings  : " + String.valueOf(rating),Toast.LENGTH_LONG).show();
                if (rating == 5.0) {
                    layoutcomplement.setVisibility(View.VISIBLE);
                    layoutdissatis.setVisibility(View.GONE);
                    layoutsubmit.setVisibility(View.VISIBLE);
                    submit_complement.setVisibility(View.VISIBLE);
                    submit_rating.setVisibility(View.GONE);
                } else {
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

        vehiclebtn = (Button) findViewById(R.id.selectedVehicle);
        vehiclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("selectedRateTab", "vehicle");
                editor.commit();
                Intent verify = new Intent(RatingMainActivity.this, RatingPassActivity.class);
                startActivity(verify);
            }
        });

        driverbtn = (Button) findViewById(R.id.selectedDriver);
        driverbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("UserID", driverId);
                editor.putString("selectedRateTab", "driver");
                editor.commit();
                Intent verify = new Intent(RatingMainActivity.this, RatingPassActivity.class);
                startActivity(verify);
            }
        });

        copassengerbtn = (Button) findViewById(R.id.selectedCoPass);
        copassengerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("selectedRateTab", "copassenger");
                editor.commit();
                Intent verify = new Intent(RatingMainActivity.this, CopassengerListActivity.class);
                startActivity(verify);
            }
        });

        submit_rating = (Button) findViewById(R.id.submit_rating);
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
    public void onResume() {
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
        }, 2000);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedpreferences = getSharedPreferences("rating_preference", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear().apply();
    }

    public void lockButtons() {
        Date date = new Date();
        long timeMilli = date.getTime();
        sharedpreferences = getSharedPreferences("rating_preference", Context.MODE_PRIVATE);

        String DONEdriver = (sharedpreferences.getString("done_ratedriver", "NO"));
        String DONEcopassenger = (sharedpreferences.getString("done_copassenger", "NO"));
        String DONEvehicle = (sharedpreferences.getString("done_ratevehicle", "NO"));

        if (DONEvehicle.equals("YES")) {
//            vehiclebtn.setEnabled(false);
            vehiclebtn.setTextColor(GRAY);
            vehi.setImageResource(R.drawable.star);
        }
        if (DONEdriver.equals("YES")) {
            driverbtn.setTextColor(GRAY);
            dri.setImageResource(R.drawable.star);
        }
        if (DONEcopassenger.equals("YES")) {
            copassengerbtn.setTextColor(GRAY);
            coop.setImageResource(R.drawable.star);
        }
    }

    //To set correct parameters to enter rating to the DB
    public void setParmsToSendCompliment(String Compliment) {
        sharedpreferences = getSharedPreferences("rating_preference", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        String UserType = "driver";
        String TripId = (sharedpreferences.getString("TripId", "00001"));
        String UserID = (sharedpreferences.getString("UserID", "U000001"));//TODO:INSERT DRIVER ID
        String RatedBy = (sharedpreferences.getString("RatedBy", "U0000002"));
        String GivenRating = "5.0";
        String CalRating = "5.0";

        drivercomplimentpostrequest(TripId, UserID, UserType, RatedBy, GivenRating, CalRating, Compliment);
        final Handler handler = new Handler();
        final String Userid = UserID;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mVolleyService.jsonrequestforRating(Userid);
            }
        }, 100);
//        mVolleyService.jsonrequestforRating(UserID);
    }

    //insert ratings related to driver or co-passenger into the database
    public void drivercomplimentpostrequest(String TripId, String UserID, String UserType, String RatedBy,
                                            String GivenRating, String CalRating, String Compliment) {
        try {
            String URL = null;
            requestQueue = Volley.newRequestQueue(this.getApplicationContext());

            URL = BASECONTENT.IpAddress + "/ratings/driver-rating";

            Log.e("URL", URL);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("TripId", TripId);
            jsonBody.put("UserID", UserID);
            jsonBody.put("RatedBy", RatedBy);
            jsonBody.put("GivenRating", GivenRating);
            jsonBody.put("CalculatedRating", CalRating);
            jsonBody.put("Compliment", Compliment);

            final String mRequestBody = jsonBody.toString();

            Log.e("VOLLEY", TripId + ":" + UserID + ":" + UserType + ":" + RatedBy + ":" + GivenRating + ":" + CalRating);

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

    //Retrieve vehicleID
    private void vehiclejsonrequest(String UserID) {
        String JSON_URL = VEHICLERETRIEVE_URL + UserID;

        Log.e("JSONREQUEST", "started");
        Log.e("JSON_URL_FIRST", JSON_URL);
        final String finalJSON_URL = JSON_URL;
        vehiclerequest = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                Log.e("JSON_URL", finalJSON_URL);
                JSONObject jsonObject = null;
                Log.e("VehicleID11111111111", String.valueOf(response));
                if (response.length() > 0) {
                    try {
                        jsonObject = response.getJSONObject(0);

                        String vehicleID = jsonObject.getString("VehicleID");
                        Log.e("VehicleID", vehicleID);

                        if (jsonObject.getString("VehicleID") != null) {

                            SharedPreferences.Editor editor = getSharedPreferences("rating_preference", MODE_PRIVATE).edit();
                            editor.putString("vehicleId", vehicleID);
                            editor.apply();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("JSONREQUEST", "ERROR");
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("JSONREQUEST_ERROR", error.toString());
            }
        });
        vehiclerequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        vehiclerequestQueue = Volley.newRequestQueue(RatingMainActivity.this);
        vehiclerequestQueue.add(vehiclerequest);
    }

    void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + response);
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + "That didn't work!");
            }
        };
    }
}
