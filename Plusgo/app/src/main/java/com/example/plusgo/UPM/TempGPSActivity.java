/*
 * *
 *  * Created by Viraj Wickramasinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 6/11/19 11:08 PM
 *
 */

package com.example.plusgo.UPM;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.plusgo.BaseContent;
import com.example.plusgo.Login;
import com.example.plusgo.R;
import com.example.plusgo.Utility.Driver;
import com.example.plusgo.Utility.FirebaseSuccessListener;
import com.example.plusgo.Utility.LocationBean;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TempGPSActivity extends Activity implements LocationListener {
    private final long EVERY_FIVE_SECOND = 5000;
    private boolean exists = false;
    private Handler handler;
    private Runnable runnable;
    private  List<LocationBean> location;
    LocationBean locationBean;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    List<String> id;
    List<LocationBean> selectedList;
    private boolean isWithin1km = true;
    double pLatitude = 0;
    double pLongitude = 0;
    float[] results = new float[1];
    private String uid;
    private BaseContent BASECONTENT = new BaseContent();
    private String PYTHON_URL_POST_DATA_AVAILABLE = BASECONTENT.pythonIpAddress + "/available";
    /**
     * context of calling class
     */
    private Context mContext;
    private Button cllick;
    /**
     * flag for gps status
     */
    private boolean isGpsEnabled = false;

    /**
     * flag for network status
     */
    private boolean isNetworkEnabled = false;

    /**
     * flag for gps
     */
    private boolean canGetLocation = false;

    /**
     * location
     */
    private Location mLocation;

    /**
     * latitude
     */
    private double mLatitude;

    /**
     * longitude
     */
    private double mLongitude;

    /**
     * min distance change to get location update
     */
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATE = 0;

    /**
     * min time for location update
     * 60000 = 1min
     */
    private static final long MIN_TIME_FOR_UPDATE = 5000;

    /**
     * location manager
     */
    private LocationManager mLocationManager;
    DatabaseReference db;

    public TempGPSActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tempgps);
        location = new ArrayList<>();
        selectedList = new ArrayList<>();
        id = new ArrayList<>();
        FirebaseApp.initializeApp(this);
        db = FirebaseDatabase.getInstance().getReference("drivers");
        checkAndAddPermission();
        //Executing the handler
        executeHandler();
        filterRelevantDrivers();

        SharedPreferences user = getSharedPreferences("userStore",MODE_PRIVATE);
        uid = user.getString("UId", null);

        cllick = (Button)findViewById(R.id.i);
        cllick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(TempGPSActivity.this, DriverListActivity.class));
            }
        });
    }

   // @Override
    protected List<String> Start() {

        db.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                id.clear();
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if (data.child("uid").exists()) {
                            id.add(String.valueOf(data.child("uid").getValue()));

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }

        });
        return id;
    }

    private void checkAndAddPermission() {
        List<String> permissionsNeeded = new ArrayList<>();

        final List<String> permissionsList = new ArrayList<>();
        if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");

        if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("Coarse");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);

                ActivityCompat.requestPermissions(TempGPSActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            } else {
                ActivityCompat.requestPermissions(TempGPSActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
        }
    }

    public double getLatitude() {

        if (mLocation != null) {

            mLatitude = mLocation.getLatitude();
        }
        return mLatitude;
    }

    /**
     * @return longitude
     *         function to get longitude
     */
    public double getLongitude() {

        if (mLocation != null) {

            mLongitude = mLocation.getLongitude();

        }

        return mLongitude;
    }

    /**
     * @return to check gps or wifi is enabled or not
     */
    public boolean canGetLocation() {

        return this.canGetLocation;
    }

    /**
     * function to prompt user to open
     * settings to enable gps
     */
    public void showSettingsAlert() {

        AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AppTheme));

        mAlertDialog.setTitle("Gps Disabled");

        mAlertDialog.setMessage("gps is not enabled . do you want to enable ?");

        mAlertDialog.setPositiveButton("settings", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Intent mIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(mIntent);
            }
        });

        mAlertDialog.setNegativeButton("cancle", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.cancel();

            }
        });

        final AlertDialog mcreateDialog = mAlertDialog.create();
        mcreateDialog.show();
    }



    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                perms.put(android.Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    // All Permissions Granted
                    getLocation();
                } else {

                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    /**
     * @return location
     */
    public Location getLocation() {

        try {

            mLocationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            /*getting status of the gps*/
            isGpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            /*getting status of network provider*/
            isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGpsEnabled && !isNetworkEnabled) {

                /*no location provider enabled*/
            } else {

                this.canGetLocation = true;

                /*getting location from network provider*/
                if (isNetworkEnabled) {
                    try {
                        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATE, this);

                        if (mLocationManager != null) {

                            mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                            if (mLocation != null) {

                                mLatitude = mLocation.getLatitude();

                                mLongitude = mLocation.getLongitude();
                            }
                        }
                    } catch (SecurityException e) {
                        Log.d("err2: ", String.valueOf(e));
                    }
                }

                /*if gps is enabled then get location using gps*/
                if (isGpsEnabled) {

                    if (mLocation == null) {
                        try {
//
                            if (mLocationManager != null) {

                                mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                                if (mLocation != null) {

                                    mLatitude = mLocation.getLatitude();
                                    mLongitude = mLocation.getLongitude();

                                }

                            }
                        } catch (SecurityException e) {
                            Log.d("err4: ", String.valueOf(e));
                        }
                    }
                }
            }


        }catch (Exception e) {

            e.printStackTrace();
        }



        return mLocation;
    }

    public void executeHandler(){
        //If the handler and runnable are null we create it the first time.
        if(handler == null && runnable == null){
            handler = new Handler();

            runnable = new Runnable() {
                @Override
                public void run() {
                    //Updating firebase store
                    addDrivers();

                    //And we execute it again
                    handler.postDelayed(this, EVERY_FIVE_SECOND);
                }
            };
        }
        //If the handler and runnable are not null, we execute it again when the app is resumed.
        else{
            handler.postDelayed(runnable, EVERY_FIVE_SECOND);
        }
    }

    private void addDrivers(){
//        Log.i("111", "111");
        double latitude = 0.0;
        double longitude = 0.0;
        GpsLocationTracker mGpsLocationTracker = new GpsLocationTracker(TempGPSActivity.this);

        /**
         * Set GPS Location fetched address
         */
        if (mGpsLocationTracker.canGetLocation())
        {
            latitude = mGpsLocationTracker.getLatitude();
            longitude = mGpsLocationTracker.getLongitude();

        }
        else
        {
            mGpsLocationTracker.showSettingsAlert();
        }



        Random random = new Random();
        Log.i("size ", String.valueOf(Start().size()));

        SharedPreferences selfStore = getSharedPreferences("userStore",MODE_PRIVATE);
        String uid = selfStore.getString("UId", null);

        if(Start().size() > 0) {
            for (int j = 0; j < Start().size(); j++) {
//                Log.d("111x", String.valueOf(Start().get(j)));
                if (Start().get(j).equalsIgnoreCase(uid)) {
                    Log.i("replica", "sssss"); //update
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("drivers").child(uid);
                    Driver driver = new Driver(uid, longitude, latitude);
                    databaseReference.setValue(driver);
                    break;
//            }
                }
                else {
//                    Log.i("added1", "qqqq");

                    Driver driver = new Driver(uid, longitude, latitude);
                    String id = db.push().getKey();
                    db.child(uid).setValue(driver);
                    break;
                }
            }
        }
//
            else{
                Log.i("added2", "aaaaa");
                Driver driver = new Driver(uid, longitude, latitude);
                String id = db.push().getKey();
                db.child(uid).setValue(driver);
            }
        }




//    }


    @Override
    protected void onResume() {
        super.onResume();
        //execute the handler again.
        executeHandler();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //we remove the callback
        handler.removeCallbacks(runnable);
        //and we set the status to offline.
        //updateStatusToOffline();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    protected void getUserLocations(final FirebaseSuccessListener firebaseSuccessListener) {
        Log.d("getUserLocations()", "getUserLocations()");
        db.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                location.clear();
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if (data.child("uid").exists()) {

                        locationBean = new LocationBean();
                        locationBean.setUid(String.valueOf(data.child("uid").getValue()));
                        locationBean.setLatitude(Double.parseDouble(String.valueOf(data.child("latitude").getValue())));
                        locationBean.setLongitude(Double.parseDouble(String.valueOf(data.child("longitude").getValue())));
                        location.add(locationBean);
//                        Log.d("loc", String.valueOf(locationBean.getUid()));
//                        Log.d("location", String.valueOf(location.size()));
                    }
                }
                firebaseSuccessListener.locationBean(location);
                Log.d("redda2", String.valueOf(location.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }

        });
        Log.d("redda3", String.valueOf(location.size()));
        //return location;
    }
//    String c = "U1558711443502";
    public void filterRelevantDrivers(){

        getUserLocations(new FirebaseSuccessListener() {
            @Override
            public void locationBean(List<LocationBean> locations) {
                if(locations.size() > 0){
                    Log.d("ccc", String.valueOf(locations.size()));
                    for(int i=0; i <locations.size(); i++){ //get passenger location
                        if(locations.get(i).getUid().equals(uid)){
                            pLatitude = locations.get(i).getLatitude();
                            pLongitude = locations.get(i).getLongitude();
                            //locations.remove(locations.get(i));
                            Log.d("size1", String.valueOf(locations.size()));
                            break;
                        }
                    }

                    v(locations);


                    //return selectedList;
                }
                else{
                    Log.d("diu", String.valueOf(locations.size()));
                }
                //return selectedList;
            }
        });

    }

    public void v(List<LocationBean> locations){

        for(int i=0; i <locations.size(); i++) {
            Log.d("size2", String.valueOf(locations.size()));
            double dLatitude = locations.get(i).getLatitude();
            double dLongitude = locations.get(i).getLongitude();
            Location.distanceBetween(pLatitude, pLongitude, dLatitude, dLongitude, results);
            float distanceInMeters = results[0];
//                        Log.d("dist", String.valueOf(distanceInMeters));
            isWithin1km = distanceInMeters < 1000;
            if(isWithin1km){
                Log.d("isWithin1km", String.valueOf(isWithin1km));
                LocationBean selectedUsers = new LocationBean();
                selectedUsers.setUid(locations.get(i).getUid());
//                selectedUsers.setLatitude(locations.get(i).getLatitude());
//                selectedUsers.setLongitude(locations.get(i).getLongitude());
                selectedList.add(selectedUsers);

            }
        }
//        for(int i=0; i< selectedList.size();i++){
//            Log.d("qq1", String.valueOf(selectedList.get(1)));
//            Log.d("qq2", String.valueOf(selectedList.get(i).getLatitude()));
//            Log.d("qq3", String.valueOf(selectedList.get(i).getLongitude()));
//        }
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONArray jArrayInput=new JSONArray();
        JSONArray jArrayInput1=new JSONArray();
        JSONObject jObjectInput=new JSONObject();

        Map<String, String> driverUID = new HashMap<>();
        List<JSONObject> newList = new ArrayList();
        for(int i=0; i< selectedList.size();i++) {
            try {
                JSONObject jObjectInput1=new JSONObject();
                jObjectInput1.put("UID",selectedList.get(i).getUid());
                jArrayInput1.put(jObjectInput1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        try {
            jObjectInput.put("uid", jArrayInput1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("json", String.valueOf(    jObjectInput    ));
//        jArrayInput.put(jObjectInput);
//        Log.d("lol", String.valueOf(    jArrayInput    ));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, PYTHON_URL_POST_DATA_AVAILABLE, jObjectInput , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("e1", String.valueOf(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("e2", String.valueOf(error));


            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);

    }
//
}