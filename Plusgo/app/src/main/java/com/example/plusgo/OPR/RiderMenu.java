/*
 * *
 *  * Created by Athrie Nathasha
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/9/19 11:50 AM
 *
 */

package com.example.plusgo.OPR;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
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
import java.util.Calendar;

public class RiderMenu extends AppCompatActivity implements View.OnClickListener {


    StringRequest stringRequest;
    RequestQueue requestQueue;
    EditText start_date, start_time,source,destination,waitingtime;
    private int mYear, mMonth, mDay, mHour, mMinute, mAmPm;
    private static final String KEY_EMPTY = "";
    private String id;
    Button submit;
    BaseContent BASECONTENT = new BaseContent();
    private String JSON_URL_ADD_OFFER_RIDE = BASECONTENT.OPRBASEIPROUTE + ":8083/addofferride";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_settings){
            Intent intent=new Intent(this,TrafficJam.class);
            startActivity(intent);
            return true;

        }

        if(item.getItemId()==R.id.action_settings2){
            Intent intent1=new Intent(this,Accident.class);
            startActivity(intent1);
            return true;

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_menu);

        //add tool bar
        Toolbar mToolbar =(Toolbar) findViewById(R.id.toolbar2) ;
        setSupportActionBar(mToolbar);


        SharedPreferences user = getSharedPreferences("userStore",MODE_PRIVATE);
        id = user.getString("UId", "0012000");

        source=findViewById(R.id.input_source);
        destination=findViewById(R.id.input_destination);
        start_date = findViewById(R.id.start_date);
        start_time = findViewById(R.id.start_time);
        waitingtime=findViewById(R.id.waiting_time);
        submit=(Button)findViewById(R.id.btnoffer);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOfferRide();
            }
        });

        start_date.setOnClickListener(this);
        start_date.setShowSoftInputOnFocus(false);

        start_time.setOnClickListener(this);
        start_time.setShowSoftInputOnFocus(false);
    }

    @Override
    public void onClick(View v) {

        if (v == start_date) {

            start_date.setShowSoftInputOnFocus(false);
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            start_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == start_time) {

            start_time.setShowSoftInputOnFocus(false);
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR);
            mMinute = c.get(Calendar.MINUTE);
            mAmPm = c.get(Calendar.AM_PM);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            start_time.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

    public void addOfferRide1() {
        try {

            //check whether the fields are empty or not
//            if(KEY_EMPTY.equals(source.getText().toString().trim()) || KEY_EMPTY.equals(destination.getText().toString().trim()) || KEY_EMPTY.equals(start_date.getText().toString().trim())|| KEY_EMPTY.equals(start_time.getText().toString().trim())||KEY_EMPTY.equals(waitingtime.getText().toString().trim())){
//                Toast.makeText(RiderMenu.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
//
//            }
//            else {
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("UserID",id);
                jsonObject.put("Source", source.getText());
                jsonObject.put("Destination", destination.getText());
                jsonObject.put("StartDate", start_date.getText());
                jsonObject.put("StartTime", start_time.getText());
                jsonObject.put("WaitingTime", waitingtime.getText());

                final String mRequestBody = jsonObject.toString();
                Log.e("VOLLEY", id+":"+source.getText()+":"+destination.getText()+":"+start_date.getText()+":"+start_time.getText()+":"+waitingtime.getText());


                StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_ADD_OFFER_RIDE, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_VOLLEY", response);
                        Toast.makeText(RiderMenu.this, "Details Added", Toast.LENGTH_LONG).show();
                        finish();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_VOLLEY", error.toString());
                        Toast.makeText(RiderMenu.this, "Error Occurred", Toast.LENGTH_SHORT).show();
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
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //insert ratings related to driver or co-passenger into the database
    public void addOfferRide() {
        try {
            String URL = null;
            requestQueue = Volley.newRequestQueue(this.getApplicationContext());

            URL = BASECONTENT.OPRBASEIPROUTE +":8083/addofferride";

            Log.e("URL", URL);

//            jsonObject.put("UserID",id);
//            jsonObject.put("Source", source.getText());
//            jsonObject.put("Destination", destination.getText());
//            jsonObject.put("StartDate", start_date.getText());
//            jsonObject.put("StartTime", start_time.getText());
//            jsonObject.put("WaitingTime", waitingtime.getText());

            String aa = String.valueOf(source.getText());
            String bb = String.valueOf(destination.getText());
            String cc = String.valueOf(start_date.getText());
            String dd = String.valueOf(start_time.getText());
            String ee = String.valueOf(waitingtime.getText());

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("UserID", id);
            jsonBody.put("Source", aa);
            jsonBody.put("Destination", bb);
            jsonBody.put("StartDate", cc);
            jsonBody.put("StartTime", dd);
            jsonBody.put("WaitingTime", ee);

            final String mRequestBody = jsonBody.toString();

            Log.e("VOLLEY", id+":"+aa+":"+bb+":"+cc+":"+dd+":"+ee);

            stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("LOG_VOLLEY", response);
                    Toast.makeText(getBaseContext(), "Your ride is added", Toast.LENGTH_LONG).show();
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
