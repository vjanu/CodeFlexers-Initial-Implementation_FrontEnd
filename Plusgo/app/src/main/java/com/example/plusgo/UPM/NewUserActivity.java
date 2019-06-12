/*
 * *
 *  * Created by Viraj Wickramasinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/24/19 7:40 PM
 *
 */

package com.example.plusgo.UPM;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.plusgo.BaseContent;
import com.example.plusgo.Login;
import com.example.plusgo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewUserActivity extends AppCompatActivity {
    private Spinner profession;
    private Button goToPreferences, update;
    private static final String KEY_EMPTY = "";
    private TextView name,email, Rname, Rphone;
    private RadioGroup radioGenderGroup;
    private RadioButton radioGenderButton;
    private CircleImageView proPic;
    private JsonArrayRequest request;
    private String id;
    private RequestQueue requestQueue;

    private BaseContent BASECONTENT = new BaseContent();
    private String JSON_URL_ADD_USER = BASECONTENT.IpAddress + "/users";
    private String JSON_URL_GET_USER = BASECONTENT.IpAddress + "/users/specific/";
    private String JSON_URL_UPDATE_USER = BASECONTENT.IpAddress + "/users/update/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        SharedPreferences user = getSharedPreferences("userStore",MODE_PRIVATE);
        id = user.getString("UId", null);

        profession = findViewById(R.id.profession);
        String[] prof = new String[]{"Driver", "Body Guard", "Security Officer","Clerical Staff", "Clerk", "Intern", "Administrative Assistant","Associate Engineer","Bank Assistant","IT Support",
                "Cashier","Network Engineer","Software Engineer","Database Administrator", "Project Manager","HR","Nurse","Lecturer","Teacher",
                "Tech Lead","Doctor","Lawyer","Professor","Senior Lecturer","Senior Lead","Senior Accountant","CEO","CIO","IT Director","Manager"};
        ArrayAdapter<String> adapterProf = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, prof);
        profession.setAdapter(adapterProf);

        name = (TextView)findViewById(R.id.fullname);
        email = (TextView)findViewById(R.id.email);
        Rname = (TextView)findViewById(R.id.guardian);
        Rphone = (TextView)findViewById(R.id.phone);
        radioGenderGroup = (RadioGroup) findViewById(R.id.gender);
        proPic = (CircleImageView)findViewById(R.id.photo);

        setValuesUser();

        goToPreferences = (Button)findViewById(R.id.btnConfirm);
        goToPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addUser();
            }
        });

        update = (Button)findViewById(R.id.btnUpdateDetails);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });


    }
    //add relevant user details
    public void addUser() {
        try {

            //check whether the fields are empty or not
            if(KEY_EMPTY.equals(name.getText().toString().trim()) || KEY_EMPTY.equals(email.getText().toString().trim()) || KEY_EMPTY.equals(Rname.getText().toString().trim())|| KEY_EMPTY.equals(Rphone.getText().toString().trim())){
                Toast.makeText(NewUserActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();

            }
            else {
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                JSONObject jsonObject = new JSONObject();

                SharedPreferences cameraStore = getSharedPreferences("camera",MODE_PRIVATE);
                String imageURL = cameraStore.getString("photo", null);

                int selectedId = radioGenderGroup.getCheckedRadioButtonId();
                radioGenderButton = (RadioButton) findViewById(selectedId);
                String gender = String.valueOf(radioGenderButton.getText());

                jsonObject.put("UserID", id);
                jsonObject.put("FullName", name.getText());
                jsonObject.put("Profession", profession.getSelectedItem().toString());
                jsonObject.put("Email", email.getText());
                jsonObject.put("Gender", gender);
                jsonObject.put("RName", Rname.getText());
                jsonObject.put("RPhone", Long.parseLong(Rphone.getText().toString()));
                jsonObject.put("img", "/images/"+imageURL);
                final String mRequestBody = jsonObject.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_ADD_USER, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_VOLLEY", response);
                        Toast.makeText(NewUserActivity.this, "Details Added", Toast.LENGTH_LONG).show();
                        finish();
                        Intent intent = new Intent(NewUserActivity.this, AddPreferenceActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_VOLLEY", error.toString());
                        Toast.makeText(NewUserActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
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

    //set values to user fields
    private void setValuesUser() {
        final byte[] byteArray;
        byteArray = getIntent().getByteArrayExtra("img");
        if ((byteArray != null) == true) {
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            proPic.setImageBitmap(bmp);
        }
        Log.e("JSONREQUEST","started");
        request = new JsonArrayRequest(JSON_URL_GET_USER+id, new Response.Listener<JSONArray>() {

            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.user2).error(R.drawable.user2);

                for(int i= 0; i<response.length(); i++){
                    try{
                        jsonObject = response.getJSONObject(i);
                        if(jsonObject.length()!=0) {
                            goToPreferences.setEnabled(false);

                            profession.setSelection(((ArrayAdapter<String>) profession.getAdapter()).getPosition(jsonObject.getString("Profession")));
                            name.setText(jsonObject.getString("FullName"));
                            email.setText(jsonObject.getString("Email"));
                            Rname.setText(jsonObject.getString("RName"));
                            Rphone.setText(jsonObject.getString("RPhone"));
//                            radioGenderButton.setText(jsonObject.getString("Gender"));

                            if ((byteArray == null) == true) {
                                //To load image using Glide
                                Glide.with(NewUserActivity.this)
                                        .load(BASECONTENT.IpAddress + jsonObject.getString("img"))
                                        .apply(requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
                                                .skipMemoryCache(true))
                                        .into(proPic);

                            }
                        }
                        else{
                            goToPreferences.setEnabled(true);
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

        requestQueue = Volley.newRequestQueue(NewUserActivity.this);
        requestQueue.add(request);

    }

    //method to update user details
    public void updateUser() {
        try {
            //check whether the fields are empty or not
            if(KEY_EMPTY.equals(name.getText().toString().trim()) || KEY_EMPTY.equals(email.getText().toString().trim()) || KEY_EMPTY.equals(Rname.getText().toString().trim())|| KEY_EMPTY.equals(Rphone.getText().toString().trim())){
                Toast.makeText(NewUserActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();

            }
            else {
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                SharedPreferences cameraStore = getSharedPreferences("camera", MODE_PRIVATE);
                String imageURL = cameraStore.getString("photo", null);

                int selectedId = radioGenderGroup.getCheckedRadioButtonId();
                radioGenderButton = (RadioButton) findViewById(selectedId);
                String gender = String.valueOf(radioGenderButton.getText());

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("UserID", id);
                jsonObject.put("FullName", name.getText());
                jsonObject.put("Profession", profession.getSelectedItem().toString());
                jsonObject.put("Email", email.getText());
                jsonObject.put("Gender", gender);
                jsonObject.put("RName", Rname.getText());
                jsonObject.put("RPhone", Long.parseLong(Rphone.getText().toString()));
                jsonObject.put("img", "/images/"+imageURL);
                final String mRequestBody = jsonObject.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.PUT, JSON_URL_UPDATE_USER + id, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_VOLLEY", response);
                        Toast.makeText(NewUserActivity.this, "User Details Updated", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_VOLLEY", error.toString());
                        Toast.makeText(NewUserActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
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

        }catch(JSONException e){
            e.printStackTrace();
        }

    }


    //method to open the camera
    public void openCamera(View v){
        Intent intent = new Intent(NewUserActivity.this, CameraActivity.class);
        startActivity(intent);
    }
    //method to destroy current session
    private void logout(){
        finish();
        Intent login = new Intent(NewUserActivity.this, Login.class);
        startActivity(login);

    }
    @Override
    public void onBackPressed() {
        logout();
    }
}
