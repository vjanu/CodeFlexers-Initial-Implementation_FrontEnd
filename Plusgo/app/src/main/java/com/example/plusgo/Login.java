
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
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.plusgo.UPM.ReportedDriverListActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

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
    private EditText etEmail;
    private String username;
    private String password;
    private ProgressDialog pDialog;
    private FirebaseAuth mAuth;
    public static final String NODE_USERS = "users";
    BaseContent BASECONTENT = new BaseContent();
    private String JSON_URL = BASECONTENT.IpAddress + "/login/specific/";
    private String JSON_GET_EMAIL = BASECONTENT.IpAddress + "/login/validate/";
    public static final String MY_PREFS_NAME = "MyPrefsFile";


    private JsonArrayRequest request;
    private List<User> users;
    private RequestQueue requestQueue;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        users = new ArrayList<>();
        user = new User();

        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        etEmail = findViewById(R.id.hiddenEmail);

        Button login = findViewById(R.id.btnLog);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etUsername.getText().toString().trim();
                password = etPassword.getText().toString().trim();

//                finish();
//                startActivity(new Intent(Login.this, AddPreferenceActivity.class));

                if (validateInputs()) {
                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if(task.isSuccessful()){
                                        String token = task.getResult().getToken();

                                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                        editor.putString("fcmtoken", token);
                                        editor.apply();
                                        try {
                                            saveToken(token);
                                        }catch(Exception e){

                                        }
                                    }else{
//                            textView.setText("Token is Not Generated");
                                    }
                                }
                            });
                    login(username, password);
                }
//                else {
//                    Toast.makeText(getApplicationContext(),
//                            "Fields Cannot be Empty", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable e) {
                try {
                    getEmail();
                }catch (Exception ex){

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nothing needed here...
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //nothing needed here...
            }
        });

        etUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {

//                getEmail();
                String txtEmail = etEmail.getText().toString();
                //String usrname = String.valueOf(username.getText());
                String passw = "123456";

                if (!hasFocus) {
                    try{
                        mAuth.createUserWithEmailAndPassword(txtEmail,passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //If Firebase Authentication Succeful Redirect to the Profile Activity
                                if(!task.isSuccessful()){
                                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                        String Aemail = etEmail.getText().toString();
                                        String Apass = "123456";

                                        userLogin(Aemail,Apass);

                                    }
                                }
                            }
                        });
                    }catch (Exception ex){

                    }
                }else{
                    //  Toast.makeText(this, "Get Focus", Toast.LENGTH_SHORT).show();
                }

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
                        user.setName(jsonObject.getString("Email"));
                        user.setStatus(jsonObject.getString("Status"));
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
                    userStore.putString("Email", user.getEmail());
                    userStore.putString("Status", user.getStatus());
                    userStore.putBoolean("Islogin", true);
                    userStore.apply();

                    SharedPreferences user = getSharedPreferences("userStore",MODE_PRIVATE);
                    boolean uid = user.getBoolean("Islogin", false);
                    String status = user.getString("Status", "P");
                    Log.d("login1:", String.valueOf(uid));


                    //checking whether the user is spouse or normal passenger

                    if(status.equalsIgnoreCase("S")){
                        finish();
                        Intent intent = new Intent(Login.this, ReportedDriverListActivity.class);
                        startActivity(intent);
                    }
                    else {
                        finish();
                        Intent intent = new Intent(Login.this, NewUserActivity.class);
                        startActivity(intent);
                    }
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


    private void userLogin(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                           // Toast.makeText(Login.this,"userLoginffff",Toast.LENGTH_LONG).show();
                        }else{
                            // progressbar.setVisibility(View.INVISIBLE);
                            Toast.makeText(Login.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }




    private void saveToken(String token)
    {
        //Create Firebase References
        DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference(NODE_USERS);

        String email = mAuth.getCurrentUser().getEmail();
        User user = new User(email,token);

        //get Unique id from get current user
        dbUsers.child(mAuth.getCurrentUser().getUid())
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Login.this,"Token Saved",Toast.LENGTH_LONG).show();

                }
            }
        });
    }



    public void getEmail() {
    String username = etUsername.getText().toString();

        request = new JsonArrayRequest(JSON_GET_EMAIL + username, new Response.Listener<JSONArray>() {

            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
//                        String useremail = jsonObject.getString("Email");

//                        SharedPreferences.Editor emailStore = getSharedPreferences("emailStore", MODE_PRIVATE).edit();
//                        emailStore.putString("email", jsonObject.getString("Email"));
//                        emailStore.apply();
//
//                        Log.d("--Test--","--Test--");
//
//                        SharedPreferences emailStore1 = getSharedPreferences("emailStore", MODE_PRIVATE);
//                        getEmail = emailStore1.getString("email", null);
//                        Log.d("getEmail",getEmail);

                        etEmail.setText(jsonObject.getString("Email"));


                    } catch (JSONException e) {

                        e.printStackTrace();
                        Log.d("JSONREQUEST", "ERROR");
                        //Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_LONG).show();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //pDialog.dismiss();
                Log.d("xxx", error.toString());
                //Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_LONG).show();

            }
        });

        requestQueue = Volley.newRequestQueue(Login.this);
        requestQueue.add(request);
    }



}

