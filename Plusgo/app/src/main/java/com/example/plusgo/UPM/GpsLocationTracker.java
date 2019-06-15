/*
 * *
 *  * Created by Viraj Wickramasinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 6/11/19 10:39 PM
 *
 */

package com.example.plusgo.UPM;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ContextThemeWrapper;

import com.example.plusgo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *         Gps location tracker class
 *         to get users location and other information related to location
 */
public class GpsLocationTracker extends Activity implements LocationListener
{
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    /**
     * context of calling class
     */
    private Context mContext;

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


    /**
     * @param mContext constructor of the class
     */
    public GpsLocationTracker(Context mContext) {

        this.mContext = mContext;

        getLocation();
    }

    private void checkAndAddPermission() {
        List<String> permissionsNeeded = new ArrayList<>();

        final List<String> permissionsList = new ArrayList<>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");

        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("Coarse");

//        if (permissionsList.size() > 0) {
//            if (permissionsNeeded.size() > 0) {
//                // Need Rationale
//                String message = "You need to grant access to " + permissionsNeeded.get(0);
//                for (int i = 1; i < permissionsNeeded.size(); i++)
//                    message = message + ", " + permissionsNeeded.get(i);
//
//                ContextCompat.requestPermissions(mContext, permissionsList.toArray(new String[permissionsList.size()]),
//                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
//            } else {
//                ActivityCompat.requestPermissions(MainActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
//                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
//            }
//        }
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
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 255) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // We now have permission to use the location
//            }
//        }
//    }

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * call this function to stop using gps in your application
     */
    public void stopUsingGps() {

        if (mLocationManager != null) {

            mLocationManager.removeUpdates(GpsLocationTracker.this);

        }
    }

    /**
     * @return latitude
     *         <p/>
     *         function to get latitude
     */
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

        AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.GPS));

        mAlertDialog.setTitle("Gps Disabled");

        mAlertDialog.setMessage("gps is not enabled . do you want to enable ?");

        mAlertDialog.setPositiveButton("settings", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Intent mIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(mIntent);
            }
        });

        mAlertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.cancel();

            }
        });

        final AlertDialog mcreateDialog = mAlertDialog.create();
        mcreateDialog.show();
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
}