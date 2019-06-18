/*
 * *
 *  * Created by Viraj Wickramasinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 4/17/19 2:19 PM
 *
 */

package com.example.plusgo.UPM;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.plusgo.BaseContent;
import com.example.plusgo.Login;
import com.example.plusgo.R;
import com.example.plusgo.Utility.Telephone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class VerifyMobilePhoneActivity extends AppCompatActivity {
    private static final String KEY_EMPTY = "";
    private TextView number, account;
    private String tp;
    private BaseContent BASECONTENT = new BaseContent();
    private String JSON_URL_ADD_TP = BASECONTENT.IpAddress + "/telephone";
    private String JSON_URL_CHECK = BASECONTENT.IpAddress + "/telephone/specific/";
    private boolean result = false;
    private RequestQueue requestQueue;
    List<Telephone> tpN;
    static int x;
//    private Telephone telephone = new Telephone();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_mobile_phone);

        tpN = new ArrayList<>();

        number = (TextView) findViewById(R.id.phoneNumber);
        account = (TextView) findViewById(R.id.account);

        //Calling Verification class
        Button btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tp = number.getText().toString().trim();
                if (validateInputs()) {
                    isExists();

                    Log.d("ttt", String.valueOf(x));
                    addTP();
                }
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(VerifyMobilePhoneActivity.this, Login.class);
                startActivity(intent);
            }
        });
    }

    //add relevant user details
    public void addTP() {
        try {

            if(x > 0){
                number.setError("Telephone Number already exists");
                number.requestFocus();
            }
            else {
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("Telephone", number.getText());
                final String mRequestBody = jsonObject.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_ADD_TP, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_VOLLEY", response);
                        sendSMS();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_VOLLEY", error.toString());
                        Toast.makeText(VerifyMobilePhoneActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void isExists() {

        Log.d("www1","qqqqqq");
        JsonArrayRequest request = new JsonArrayRequest(JSON_URL_CHECK+tp, new Response.Listener<JSONArray>() {

            public void onResponse(JSONArray response) {
            x = response.length();
                Log.d("size1", String.valueOf(x));
//                JSONObject jsonObject = null;
//                for(int i= 0; i<response.length(); i++){
//                    Log.d("www2",String.valueOf(response.length()));
//                    try{
//                        Telephone telephone = new Telephone();
//                        jsonObject = response.getJSONObject(i);
//                        telephone.setTelephoneNumber(jsonObject.getString("Telephone"));
//                        Log.d("zzz",jsonObject.getString("Telephone"));
//                        tpN.add(telephone);
//
//                    }catch (Exception e){
//
//                        e.printStackTrace();
//                        Log.d("JSONREQUEST","ERROR");
//                        Toast.makeText(VerifyMobilePhoneActivity.this, "Error Occured", Toast.LENGTH_LONG).show();
//                    }
//
//                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("xxx", error.toString());
                Toast.makeText(VerifyMobilePhoneActivity.this, "Error Occured", Toast.LENGTH_LONG).show();

            }
        });

        requestQueue = Volley.newRequestQueue(VerifyMobilePhoneActivity.this);
        requestQueue.add(request);

    }

    public void sendSMS(){
        Toast.makeText(VerifyMobilePhoneActivity.this, "Code Sent!", Toast.LENGTH_LONG).show();
        finish();
        Intent intent = new Intent(VerifyMobilePhoneActivity.this, VerifyCodeActivity.class);
        startActivity(intent);
    }
    private boolean validateInputs() {
        if (KEY_EMPTY.equals(tp)) {
            number.setError("Telephone Number cannot be empty");
            number.requestFocus();
            return false;
        }
        return true;
    }
}
