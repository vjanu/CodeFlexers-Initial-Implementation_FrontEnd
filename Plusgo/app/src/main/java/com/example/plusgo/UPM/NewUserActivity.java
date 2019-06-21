/*
 * *
 *  * Created by Viraj Wickramasinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/24/19 7:40 PM
 *
 */

package com.example.plusgo.UPM;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
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
import com.example.plusgo.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewUserActivity extends AppCompatActivity {
    private Spinner profession;
    private Button goToPreferences, update,p;
    private static final String KEY_EMPTY = "";
    private TextView name,email, Rname, Rphone, dob,toke,token1;
    private RadioGroup radioGenderGroup;
    private RadioButton radioGenderButton;
    private CircleImageView proPic;
    private JsonArrayRequest request;
    private String id;
    private RequestQueue requestQueue;
    public static String NotificationToken;

    private BaseContent BASECONTENT = new BaseContent();
    private String JSON_URL_ADD_USER = BASECONTENT.IpAddress + "/users";
    private String JSON_URL_GET_USER = BASECONTENT.IpAddress + "/users/specific/";
    private String JSON_URL_UPDATE_USER = BASECONTENT.IpAddress + "/users/update/";

    /*Added by Surath
     * For the Notification
     */
    public static final String CHANNEL_ID = "plus_go";
    private static final String CHANNEL_NAME = "Plus Go";
    private static final String CHANNEL_DESC = "Plus Go Notification";

    private FirebaseAuth mAuth; //Firebase Authentication
    public String token;
    public String password;
    String userEmail = "";// Just check
    public static final String NODE_USERS = "users";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        /*
        Added By Surath
         */
        mAuth = FirebaseAuth.getInstance();
        //To Subscription
        FirebaseMessaging.getInstance().subscribeToTopic("Updates");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }

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
        dob = (TextView)findViewById(R.id.dob);
        toke = (TextView)findViewById(R.id.hidden);


        password = "123456"; // Testing Purpose Added by Surath
        //userEmail = email.getText().toString().trim();
        //userEmail = "surathruwan@gmail.com";
//        userEmail = email.getText().toString();
        Log.d("emails", userEmail);
        setValuesUser();

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus

                    /*
                     * Firebase Authentication with Email with Password
                     * */

                    userEmail = email.getText().toString();
                    Log.d("sss" , password );
                    Log.d("sss1" , userEmail);
                    mAuth.createUserWithEmailAndPassword(userEmail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //If Firebase Authentication Succeful Redirect to the Profile Activity
                            if(task.isSuccessful()){
                                //startProfileActivity();
                            }else{
                                if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                    userLogin(userEmail,password);
                                }else{
                                    // progressbar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(NewUserActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                }

                            }

                        }
                    });

                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if(task.isSuccessful()){
                                        NotificationToken = task.getResult().getToken();
                                        toke.setText(NotificationToken);
                                        Log.d("qw1:", NotificationToken);
                                        saveToken(NotificationToken);

                                    }else{
//                                      textView.setText("Token is Not Generated");
                                    }
                                }
                            });
                }
            }
        });

        goToPreferences = (Button)findViewById(R.id.btnConfirm);
        p = (Button)findViewById(R.id.pi);
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
        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(NewUserActivity.this, AddPreferenceActivity.class));

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

//                /*
//                 * Firebase Authentication with Email with Password
//                 * */
//
//                userEmail = email.getText().toString();
//                Log.d("sss" , password );
//                Log.d("sss1" , userEmail);
//                mAuth.createUserWithEmailAndPassword(userEmail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        //If Firebase Authentication Succeful Redirect to the Profile Activity
//                        if(task.isSuccessful()){
//                            //startProfileActivity();
//                        }else{
//                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
//                                userLogin(userEmail,password);
//                            }else{
//                                // progressbar.setVisibility(View.INVISIBLE);
//                                Toast.makeText(NewUserActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
//                            }
//
//                        }
//
//                    }
//                });


//                FirebaseInstanceId.getInstance().getInstanceId()
//                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                                if(task.isSuccessful()){
//                                    String NotificationToken = task.getResult().getToken();
//                                    token1.setText(NotificationToken);
//                                    Log.d("qw1:", NotificationToken);
//
//                                    saveToken(token);
//                                    Log.d("qw2:", token);
////                            Toast.makeText(ProfileActivity.this,token,Toast.LENGTH_LONG).show();
////                            textView.setText("Token" + token);
//                                }else{
////                            textView.setText("Token is Not Generated");
//                                }
//                            }
//                        });

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
                Log.d("token",NotificationToken.toString());
                jsonObject.put("Age", dob.getText());
                jsonObject.put("Token", toke.getText().toString());
                Log.d("token",NotificationToken.toString());
                final String mRequestBody = jsonObject.toString();

                SharedPreferences.Editor selfData = getSharedPreferences("self", MODE_PRIVATE).edit();
                selfData.putString("UID", id);
                selfData.putString("Profession", profession.getSelectedItem().toString());
                selfData.putInt("Age", Integer.parseInt(dob.getText().toString()));
                selfData.putInt("Profession_Category", professionCategory(profession.getSelectedItem().toString()));
                selfData.apply();

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
    private void setValuesUser() { //todo
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
                            dob.setText(jsonObject.getString("Age"));
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
                jsonObject.put("Age", Integer.parseInt(dob.getText().toString()));
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

    private int professionCategory(String profession){
        int category = 0;
        switch(profession){
            case "Driver":
                category = 25;
                break;
            case "Body Guard":
                category = 20;
                break;
            case "Security Officer":
                category = 17;
                break;
            case "Clerical Staff":
                category = 15;
                break;
            case "Intern":
                category = 32;
                break;
            case "Administrative Assistant":
                category = 42;
                break;
            case "Associate Engineer":
                category = 42;
                break;
            case "Bank Assistant":
                category = 38;
                break;
            case "IT Support":
                category = 40;
                break;
            case "Cashier":
                category = 35;
                break;
            case "Network Engineer":
                category = 55;
                break;
            case "Software Engineer":
                category = 55;
                break;
            case "Database Administrator":
                category = 60;
                break;
            case "Project Manager":
                category = 68;
                break;
            case "HR":
                category = 63;
                break;
            case "Nurse":
                category = 80;
                break;
            case "Lecturer":
                category = 80;
                break;
            case "Teacher":
                category = 75;
                break;
            case "Doctor":
                category = 98;
                break;
            case "Lawyer":
                category = 95;
                break;
            case "Professor":
                category = 105;
                break;
            case "Senior Lecturer":
                category = 100;
                break;
            case "Senior Lead":
                category = 97;
                break;
            case "Senior Accountant":
                category = 92;
                break;
            case "CEO":
                category = 105;
                break;
            case "CIO":
                category = 105;
                break;
            case "IT Director":
                category = 95;
                break;
            case "Manager":
                category = 91;
                break;
        }

        return category;
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

    private int getAge(String dob){
        return 0;
    }
    @Override
    public void onBackPressed() {
        logout();
    }


    /*
    Added by Surath
     */
    private void userLogin(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(NewUserActivity.this,"userLoginffff",Toast.LENGTH_LONG).show();
                        }else{
                            // progressbar.setVisibility(View.INVISIBLE);
                            Toast.makeText(NewUserActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private void saveToken(String token)
    {
        String email = mAuth.getCurrentUser().getEmail();
        Log.d("email",email);
        User user = new User(email,token);

        //Create Firebase References

        DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference(NODE_USERS);

        //get Unique id from get current user
        dbUsers.child(mAuth.getCurrentUser().getUid())
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(NewUserActivity.this,"Token Saved",Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}
