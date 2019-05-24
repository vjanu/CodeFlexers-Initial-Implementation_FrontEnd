/*
 * *
 *  * Created by Viraj Wickramasinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/8/19 10:15 PM
 *
 */

package com.example.plusgo.UPM;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.plusgo.BaseContent;
import com.example.plusgo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class AddPreferenceActivity extends AppCompatActivity {

    private Button btnConfirm, btnUpdate;
    private String id;
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private RadioGroup genderPref, langSpoken, smoking, musicLove, motionSick;
    private RadioButton genderP, langP, smoke, music, sick;

    private BaseContent BASECONTENT = new BaseContent();
    private String JSON_URL_ADD_PREF = BASECONTENT.IpAddress + "/preference";
    private String JSON_URL_UPDATE_PREF = BASECONTENT.IpAddress + "/preference/update/";
    private String JSON_URL_GET_PREF = BASECONTENT.IpAddress + "/preference/specific/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_preference);

        SharedPreferences user = getSharedPreferences("userStore",MODE_PRIVATE);
        id = user.getString("UId", null);

        genderPref = (RadioGroup) findViewById(R.id.gPref);
        langSpoken = (RadioGroup) findViewById(R.id.lang);
        smoking = (RadioGroup) findViewById(R.id.smo);
        musicLove = (RadioGroup) findViewById(R.id.musi);
        motionSick = (RadioGroup) findViewById(R.id.sic);

        btnConfirm = (Button)findViewById(R.id.btnConfirm);
        btnUpdate = (Button)findViewById(R.id.btnUpdatePreferences);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPreference();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePreference();
            }
        });
    }

    //add relevant user Preference
    public void addPreference() {
        try {

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                JSONObject jsonObject = new JSONObject();

                int gId = genderPref.getCheckedRadioButtonId();
                genderP = (RadioButton) findViewById(gId);
                String genderPre = String.valueOf(genderP.getText());

                int lId = langSpoken.getCheckedRadioButtonId();
                langP = (RadioButton) findViewById(lId);
                String langPre = String.valueOf(langP.getText());

                int sId = smoking.getCheckedRadioButtonId();
                smoke = (RadioButton) findViewById(sId);
                String smokePre = String.valueOf(smoke.getText());

                int mId = musicLove.getCheckedRadioButtonId();
                music = (RadioButton) findViewById(mId);
                String musicPre = String.valueOf(music.getText());

                int sickId = motionSick.getCheckedRadioButtonId();
                sick = (RadioButton) findViewById(sickId);
                String motionSick = String.valueOf(sick.getText());

                jsonObject.put("UserID", id);
                jsonObject.put("GenderP", genderPre);
                jsonObject.put("LanguageS", langPre);
                jsonObject.put("Smoking", smokePre);
                jsonObject.put("MusicLover", musicPre);
                jsonObject.put("MotionSickness", motionSick);
                final String mRequestBody = jsonObject.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_ADD_PREF, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_VOLLEY", response);
                        Toast.makeText(AddPreferenceActivity.this, "Preferences Added", Toast.LENGTH_LONG).show();
                        finish();
                        Intent intent = new Intent(AddPreferenceActivity.this, VehicleActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_VOLLEY", error.toString());
                        Toast.makeText(AddPreferenceActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
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

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //update relevant user Preference
    public void updatePreference() {
        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonObject = new JSONObject();

            int gId = genderPref.getCheckedRadioButtonId();
            genderP = (RadioButton) findViewById(gId);
            String genderPre = String.valueOf(genderP.getText());

            int lId = langSpoken.getCheckedRadioButtonId();
            langP = (RadioButton) findViewById(lId);
            String langPre = String.valueOf(langP.getText());

            int sId = smoking.getCheckedRadioButtonId();
            smoke = (RadioButton) findViewById(sId);
            String smokePre = String.valueOf(smoke.getText());

            int mId = musicLove.getCheckedRadioButtonId();
            music = (RadioButton) findViewById(mId);
            String musicPre = String.valueOf(music.getText());

            int sickId = motionSick.getCheckedRadioButtonId();
            sick = (RadioButton) findViewById(sickId);
            String motionSick = String.valueOf(sick.getText());

            jsonObject.put("UserID", id);
            jsonObject.put("GenderP", genderPre);
            jsonObject.put("LanguageS", langPre);
            jsonObject.put("Smoking", smokePre);
            jsonObject.put("MusicLover", musicPre);
            jsonObject.put("MotionSickness", motionSick);
            final String mRequestBody = jsonObject.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.PUT, JSON_URL_UPDATE_PREF+id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_VOLLEY", response);
                    Toast.makeText(AddPreferenceActivity.this, "Preference Details Updated", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_VOLLEY", error.toString());
                    Toast.makeText(AddPreferenceActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
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

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //set values to user preferences
    private void setUserPreferences() {

        Log.e("JSONREQUEST","started");
        request = new JsonArrayRequest(JSON_URL_GET_PREF+id, new Response.Listener<JSONArray>() {

            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for(int i= 0; i<response.length(); i++){
                    try{
                        jsonObject = response.getJSONObject(i);
                        if(jsonObject.length()!=0) {
                            btnConfirm.setEnabled(false);
//                            name.setText(jsonObject.getString("Name"));
//                            profession.setSelection(((ArrayAdapter<String>) profession.getAdapter()).getPosition(jsonObject.getString("Profession")));
//                            name.setText(jsonObject.getString("FullName"));
//                            email.setText(jsonObject.getString("Email"));
//                            Rname.setText(jsonObject.getString("RName"));
//                            Rphone.setText(jsonObject.getString("RPhone"));
//                            radioGenderButton.setText(jsonObject.getString("Gender"));


                        }
                        else{
                            btnConfirm.setEnabled(true);
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue = Volley.newRequestQueue(AddPreferenceActivity.this);
        requestQueue.add(request);

    }
}
