/*
 * *
 *  * Created by Viraj Wickramasinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/15/19 8:12 PM
 *
 */

package com.example.plusgo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.example.plusgo.UPM.VerifyMobilePhoneActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class SignUp extends AppCompatActivity {

    BaseContent BASECONTENT = new BaseContent();
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    public static String NotificationBodyCatcher;
    Button signupbtn;
    EditText email,username,pwd;
    private RadioGroup radioRoleGroup;
    private RadioButton radioRoleButton;
    TextView usernameAlert;
    private FirebaseAuth mAuth;
    public static final String NODE_USERS = "users";
    /*Added by Surath
     * For the Notification
     */
    public static final String CHANNEL_ID = "plus_go";
    private static final String CHANNEL_NAME = "Plus Go";
    private static final String CHANNEL_DESC = "Plus Go Notification";
    // MY_PREFS_NAME - a static String variable like:
    public static final String MY_PREFS_NAME = "MyPrefsFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.email);
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

        //To Subscription
        FirebaseMessaging.getInstance().subscribeToTopic("Updates");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }

        pwd = (EditText) findViewById(R.id.signUpPassword);

        signupbtn = (Button) findViewById(R.id.btnSignUp);
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtEmail = String.valueOf(email.getText());
                String usrname = String.valueOf(username.getText());
                String passw = String.valueOf(pwd.getText());

                if((txtEmail.equals(""))||(usrname.equals(""))||(passw.equals(""))){
                    Toast.makeText(getApplicationContext(),"Please Fill All Fields",Toast.LENGTH_SHORT).show();
                }else{

                    mAuth.createUserWithEmailAndPassword(txtEmail,passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //If Firebase Authentication Succeful Redirect to the Profile Activity
                            if(task.isSuccessful()){
                                //startProfileActivity();
                            }else{
                                if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                    String Aemail = String.valueOf(email.getText());
                                    String Apass = String.valueOf(pwd.getText());
                                   userLogin(Aemail,Apass);
                                }else{
                                   // progressbar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(SignUp.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                }

                            }

                        }
                    });

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

//                                    Log.d("qw2:", token);
//                            Toast.makeText(ProfileActivity.this,token,Toast.LENGTH_LONG).show();
//                            textView.setText("Token" + token);
                                }else{
//                            textView.setText("Token is Not Generated");
                                }
                            }
                        });
                    Log.e("email", txtEmail);
                    Log.e("uname",usrname);
                    Log.e("pwd",passw);
                    // Add new user
                    addNewUser(usrname,passw,txtEmail);
                    // Go to Login page at success
//                    finish();
//                    Intent signUp;
//                    signUp = new Intent(SignUp.this, VerifyMobilePhoneActivity.class);
//                    startActivity(signUp);
                }
            }
        });
    }

    //method to check whether the username has already registered
    private void isUserExists(String userName) {
        final String JSON_URL = BASECONTENT.IpAddress+"/login/validate/"+userName;
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
    public void addNewUser(String Username,String Password,String Email) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
            String URL = BASECONTENT.IpAddress+"/login";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("Username", Username);
            jsonBody.put("Password", Password);
            jsonBody.put("Email", Email);
            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(SignUp.this, "Confirm Your Mobile Number" , Toast.LENGTH_LONG).show();
                    finish();
                    Intent directToLogin = new Intent(SignUp.this, VerifyMobilePhoneActivity.class);
                    startActivity(directToLogin);
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

    private void userLogin(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(SignUp.this,"userLoginffff",Toast.LENGTH_LONG).show();
                        }else{
                            // progressbar.setVisibility(View.INVISIBLE);
                            Toast.makeText(SignUp.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }


    private void saveToken(String token)
    {
        //Create Firebase References
        DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference(NODE_USERS);

        //FirebaseUser mCurrentUser = mAuth.getCurrentUser();
//        if (mCurrentUser != null) {
//            dbUsers=FirebaseDatabase.getInstance().getReference().child(NODE_USERS)
//                    .child(mCurrentUser.getUid());
//        }


       String email = mAuth.getCurrentUser().getEmail();
       User user = new User(email,token);



        //get Unique id from get current user
        dbUsers.child(mAuth.getCurrentUser().getUid())
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(SignUp.this,"Token Saved",Toast.LENGTH_LONG).show();

                }
            }
        });
    }


}
