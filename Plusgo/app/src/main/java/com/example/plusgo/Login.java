
/*
 * *
 *  * Created by Viraj Wickramasinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/15/19 8:01 PM
 *
 */

package com.example.plusgo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.plusgo.UPM.NewUserActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via username/password.
 */
public class Login extends AppCompatActivity {

    private static final String KEY_EMPTY = "";
    private EditText etUsername;
    private EditText etPassword;
    private String username;
    private String password;
    private ProgressDialog pDialog;
    BaseContent BASECONTENT = new BaseContent();
    private String JSON_URL = BASECONTENT.IpAddress + "/login/specific/";


    private JsonArrayRequest request;
    private List<User> users;
    private RequestQueue requestQueue;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        users = new ArrayList<>();
        user = new User();

        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);

        Button login = findViewById(R.id.btnLog);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etUsername.getText().toString().trim();
                password = etPassword.getText().toString().trim();

//                finish();
//                startActivity(new Intent(Login.this, AddPreferenceActivity.class));

                if (validateInputs()) {
                    login(username, password);
                }
//                else {
//                    Toast.makeText(getApplicationContext(),
//                            "Fields Cannot be Empty", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }


    //method to display the progress loader
    private void displayLoader() {
        pDialog = new ProgressDialog(Login.this);
        pDialog.setMessage("Logging In.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    //method to authenticate the user
    private void login(String username, String password) {
        displayLoader();
        Log.e("JSON_URL",JSON_URL+username+"/"+password);
        request = new JsonArrayRequest(JSON_URL+username+"/"+password, new Response.Listener<JSONArray>() {

            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;
                for(int i= 0; i<response.length(); i++){
                    try{
                        jsonObject = response.getJSONObject(i);
                        user.setUsername(jsonObject.getString("Username"));
                        user.setPassword(jsonObject.getString("Password"));
                        user.setuID(jsonObject.getString("UserID"));
                        user.setName(jsonObject.getString("Name"));
                        users.add(user);
                        finish();

                    }catch (JSONException e){

                        e.printStackTrace();
                        Log.d("JSONREQUEST","ERROR");
                        Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_LONG).show();
                    }

                }
                if(users.size() == 1){
                    pDialog.dismiss();

                        SharedPreferences.Editor userStore = getSharedPreferences("userStore", MODE_PRIVATE).edit();
                        userStore.putString("UId", user.getuID());
                        userStore.putString("Name", user.getUsername());
                        userStore.apply();

                        finish();
                        Intent intent = new Intent(Login.this, NewUserActivity.class);
                        startActivity(intent);
                }
                else{
                    pDialog.dismiss();
                    Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Log.d("xxx", error.toString());
                Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_LONG).show();

            }
        });

        requestQueue = Volley.newRequestQueue(Login.this);
        requestQueue.add(request);

    }

    //validate user inputs
    private boolean validateInputs() {
        if(KEY_EMPTY.equals(username)){
            etUsername.setError("Username cannot be empty");
            etUsername.requestFocus();
            return false;
        }
        if(KEY_EMPTY.equals(password)){
            etPassword.setError("Password cannot be empty");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }

    public void signUp(View v){
        Log.d("lo","12");
        finish();
        Intent intent = new Intent(Login.this, SignUp.class);
        startActivity(intent);
        Log.d("yo","122");

    }
}

