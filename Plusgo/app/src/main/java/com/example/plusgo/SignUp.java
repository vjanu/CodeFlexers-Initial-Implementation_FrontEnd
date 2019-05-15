/*
 * *
 *  * Created by Viraj Wickramasinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/15/19 5:03 PM
 *
 */

package com.example.plusgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class SignUp extends AppCompatActivity {

    BaseContent BASECONTENT = new BaseContent();
    private JsonArrayRequest request;
    private RequestQueue requestQueue;

    Button signupbtn;
    EditText fullname,username,pwd;
    private RadioGroup radioRoleGroup;
    private RadioButton radioRoleButton;
    TextView usernameAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fullname = (EditText) findViewById(R.id.signUpFullName);
        username = (EditText) findViewById(R.id.signUpUsername);
        usernameAlert = (TextView) findViewById(R.id.usernameTaken);
        username.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus) {
                    String usrname = String.valueOf(username.getText());
                    //Check if the username is already existing
                    isUserExists(usrname);
                }
            }
        });

        pwd = (EditText) findViewById(R.id.signUpPassword);

        signupbtn = (Button) findViewById(R.id.btnSignUp);
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fulname = String.valueOf(fullname.getText());
                String usrname = String.valueOf(username.getText());
                String passw = String.valueOf(pwd.getText());

                if((fulname.equals(""))||(usrname.equals(""))||(passw.equals(""))){
                    Toast.makeText(getApplicationContext(),"Please Fill All Fields",Toast.LENGTH_SHORT).show();
                }else{
                    Log.e("fname", fulname);
                    Log.e("uname",usrname);
                    Log.e("pwd",passw);
                    // Add new user
                    addNewUser(usrname,passw,fulname);
                    // Go to Login page at success
                    finish();
                    Intent signUp;
                    signUp = new Intent(SignUp.this, Login.class);
                    startActivity(signUp);
                }
            }
        });
    }

    //method to check whether the username has already registered
    private void isUserExists(String userName) {
        final String JSON_URL = BASECONTENT.IpAddress+"/users/validate/"+userName;
        Log.e("JSONREQUEST","isUserExists");
        request = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                Log.e("JSON_URL",JSON_URL);
                JSONObject jsonObject = null;
                if(response.length() > 0){
                    try{
                        jsonObject = response.getJSONObject(0);
                        usernameAlert.setVisibility(View.VISIBLE);
                        signupbtn.setEnabled(false);
                    }catch (JSONException e){
                        e.printStackTrace();
                        Log.e("JSONREQUEST","ERROR");
                    }
                }else{
                    usernameAlert.setVisibility(View.INVISIBLE);
                    signupbtn.setEnabled(true);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(SignUp.this);
        requestQueue.add(request);
    }

    //insert new user into the database
    public void addNewUser(String Username,String Password,String Name) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
            String URL = BASECONTENT.IpAddress+"/users";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("Username", Username);
            jsonBody.put("Password", Password);
            jsonBody.put("Name", Name);
            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(SignUp.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                    Log.i("LOG_VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SignUp.this, "Registration Failed", Toast.LENGTH_SHORT).show();

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
    }
}
