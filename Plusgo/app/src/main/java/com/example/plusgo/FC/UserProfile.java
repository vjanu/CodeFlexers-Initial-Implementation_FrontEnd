/*
 * *
 *  * Created by Viraj Wickramasinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/9/19 10:25 AM
 *
 */

package com.example.plusgo.FC;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.plusgo.BaseContent;
import com.example.plusgo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserProfile extends AppCompatActivity {
    private TextView txtuserName, txtVehicleName, totalRideCount,txtUserRating,txtVehicleid,txtVehicleRating,txtLanguages;
    private TextView txtSmoke,txtMusicLover,txtMotionSickness,txtQuite;
    private RatingBar userRatingBar,vehicleRatingBar;
    BaseContent BASECONTENT = new BaseContent();
    private String JSON_URL_GET_PERSONAL_RATINGS = BASECONTENT.IpAddress + "/ratings/personalsObject/";
    private String JSON_URL_GET_VEHICLE_RATINGS = BASECONTENT.IpAddress + "/ratings/vehiclesObject/";
    private String JSON_URL_GET_DRIVER_PREFERENCES = BASECONTENT.IpAddress + "/preference/specific/";
    RequestQueue requestQueue ;
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        myDialog = new Dialog(this);

        requestQueue = Volley.newRequestQueue(this);

        txtuserName = (TextView) findViewById(R.id.txtuserName);
        txtVehicleid = (TextView) findViewById(R.id.txtVehicleid);
        txtVehicleName = (TextView) findViewById(R.id.txtVehicleName);
        userRatingBar = (RatingBar) findViewById(R.id.userRatingBar);
        vehicleRatingBar = (RatingBar) findViewById(R.id.vehicleRatingBar);
        txtUserRating = (TextView) findViewById(R.id.txtUserRating);
        txtVehicleRating = (TextView) findViewById(R.id.txtVehicleRating);
        txtLanguages = (TextView) findViewById(R.id.txtLanguages);

        //Preferences
        txtSmoke = (TextView) findViewById(R.id.txtSmoke);
        txtMusicLover = (TextView) findViewById(R.id.txtMusicLover);
        txtMotionSickness = (TextView) findViewById(R.id.txtMotionSickness);
        txtQuite = (TextView) findViewById(R.id.txtQuite);

        Intent intent = getIntent();

        String driverName = intent.getStringExtra("driverName");
        String txtVehicle = intent.getStringExtra("txtVehicle");
        String vehicleId = intent.getStringExtra("vehicleId");

        txtuserName.setText(driverName);
        txtVehicleid.setText(vehicleId);
        txtVehicleName.setText(txtVehicle);
//        Log.d("vehicleId",vehicleId);

        getPersonalRating();
        getVehicleRating();
        getUserPreferences();

    }

    public void ShowPopUp(View v) {
        TextView txtClose;
        myDialog.setContentView(R.layout.custompopup);
        txtClose = (TextView) myDialog.findViewById(R.id.txtClose);
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.show();

    }


    public void getPersonalRating() {


        String driverId = "U1558711443513"; //TODO:ADD CORRECT USER ID  / Get from user store

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Personnal Rating...");
        progressDialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL_GET_PERSONAL_RATINGS+driverId,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("sdss", String.valueOf(response));
                        progressDialog.dismiss();
                        try {

                            String Rating = response.getString("Rating");


                        Log.d("testDate", String.valueOf(Rating));

                            userRatingBar.setRating((float) ((Float.parseFloat(Rating))/5.0));
                            txtUserRating.setText(String.valueOf(Rating) + " / 5.0");

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

    public void getVehicleRating() {

        String vehicleId = "V1560496428978"; //TODO:ADD CORRECT VEHICLE ID  // vehicleId.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Vehicle Rating...");
        progressDialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL_GET_VEHICLE_RATINGS+vehicleId,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("sdss", String.valueOf(response));
                        progressDialog.dismiss();
                        try {

                            String vehiclerating = response.getString("vehiclerating");


                            Log.d("testDate", String.valueOf(response.getString("vehiclerating")));

                          //  vehicleRatingBar.setRating((float) (Float.parseFloat(response.getString("vehiclerating"))/5.0));
                            vehicleRatingBar.setRating((float) ((Float.parseFloat(vehiclerating))/5.0));
                            txtVehicleRating.setText(String.valueOf(response.getString("vehiclerating")) + " / 5.0");

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


    public void getUserPreferences() {

        String driverId = "U1561546144874"; //TODO:ADD CORRECT VEHICLE ID  //

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Preference Details...");
        progressDialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL_GET_DRIVER_PREFERENCES+driverId,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("sdss", String.valueOf(response));
                        progressDialog.dismiss();
                        try {

                            String smoking = response.getString("Smoking");
                            String MusicLover = response.getString("MusicLover");
                            String MotionSickness = response.getString("MotionSickness");
                            String LikeQuietness = response.getString("LikeQuietness");
                            String Languages = response.getString("LanguageS");


                            if(smoking.equals("Yes"))
                            {
                                txtSmoke.setText("Smoking");
                            }else
                            {
                                txtSmoke.setText("");
                                txtSmoke.setVisibility(View.GONE);
                            }
                            if(MusicLover.equals("Yes"))
                            {
                                txtMusicLover.setText("Music Lover");
                            }else{
                                txtMusicLover.setText("");
                                txtMusicLover.setVisibility(View.GONE);
                            }
                            if(MotionSickness.equals("Yes"))
                            {
                                txtMotionSickness.setText("Motion Sickness");
                            }else{
                                txtMotionSickness.setText("");
                                txtMotionSickness.setVisibility(View.GONE);
                            }
                            if(LikeQuietness.equals("Yes"))
                            {
                                txtQuite.setText("Like Quietness");
                            }else{
                                txtQuite.setText("");
                                txtQuite.setVisibility(View.GONE);
                            }

                            txtLanguages.setText(Languages);
//                            Log.d("testDate", String.valueOf(Rating));
//
//                            vehicleRatingBar.setRating((float) ((Float.parseFloat(Rating))/5.0));
//                            txtVehicleRating.setText(String.valueOf(Rating) + " / 5.0");

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

}
