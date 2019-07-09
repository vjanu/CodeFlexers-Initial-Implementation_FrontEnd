package com.example.plusgo.OPR;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.plusgo.BaseContent;
import com.example.plusgo.BuildConfig;
import com.example.plusgo.FC.TripHistory.TripHistoryView;
import com.example.plusgo.Login;
import com.example.plusgo.R;
import com.example.plusgo.UPM.AddPreferenceActivity;
import com.example.plusgo.UPM.DriverListActivity;
import com.example.plusgo.UPM.GpsLocationTracker;
import com.example.plusgo.UPM.NewUserActivity;
import com.example.plusgo.UPM.PaymentActivity;
import com.example.plusgo.UPM.TempGPSActivity;
import com.example.plusgo.UPM.VehicleActivity;
import com.example.plusgo.Utility.Driver;
import com.example.plusgo.Utility.FirebaseSuccessListener;
import com.example.plusgo.Utility.LocationBean;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.DelayedMapListener;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

//import com.google.android.gms.location.places.Place;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar = null;
    private static final int MULTIPLE_PERMISSION_REQUEST_CODE = 4;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    RequestQueue rQueue;
    //  String URL = "http://192.168.1.4:8083/map";
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private MapView mapView;
    private EditText source_location, source_destination;
    private Button find_my_driver, accept_route;
    private String TAG = "RequestRide";
    private double source_lat, source_long, destination_lat, destination_long;
    private boolean source, destination;
    TempGPSActivity tempGPSActivity;
    private final long EVERY_FIVE_SECOND = 5000;
    private boolean exists = false;
    private Handler handler;
    private Runnable runnable;
    private List<LocationBean> location;
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
    private String JSON_URL_ACCEPT_ROUTE = BASECONTENT.OPRBASEIPROUTE + ":8083/map";
    private String JSON_URL_REPORTED_DRIVERS = BASECONTENT.IpAddress + "/ratings/reportDrivers/";
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        location = new ArrayList<>();
        selectedList = new ArrayList<>();
        id = new ArrayList<>();
        FirebaseApp.initializeApp(this);
        db = FirebaseDatabase.getInstance().getReference("drivers");
        checkPermissionsState();
//        checkAndAddPermission();
        //Executing the handler
        SharedPreferences user = getSharedPreferences("userStore", MODE_PRIVATE);
        uid = user.getString("UId", null);
        executeHandler();



//        cllick = (Button)findViewById(R.id.i);
//        cllick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//                startActivity(new Intent(TempGPSActivity.this, DriverListActivity.class));
//            }
//        });

        Places.initialize(getApplicationContext(), "AIzaSyD_gxGkVFP_a2v0_VopWLlvFZe-u46TV7M"); //Please put application code in a secure place

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        find_my_driver = findViewById(R.id.btnfinddriver);
        accept_route = findViewById(R.id.btnacceptroute);

        source_location = findViewById(R.id.source_location);
        source_destination = findViewById(R.id.source_destination);

        Log.d("Source" , source_location.getText().toString());

//        SharedPreferences.Editor location = getSharedPreferences("LOCATION", MODE_PRIVATE).edit();
//        location.putString("source", source_location.getText().toString());
//        location.putString("destination", source_destination.getText().toString());
//        location.apply();

        source_location.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                source_location.setPressed(true);
                source_location.setSelection(source_location.getText().length());
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    source = true;
                    destination = false;
                    drawAutocompleteIntent();
                }
                source_destination.getText().clear();
//                mapView.getOverlays().clear();
                return false;
            }
        });


        source_destination.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                source_destination.setPressed(true);
                source_destination.setSelection(source_destination.getText().length());
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    source = false;
                    destination = true;
                    drawAutocompleteIntent();
                }
                return false;
            }
        });

        accept_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertMapData();
            }
        });

        checkPermissionsState();


        find_my_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                filterRelevantDrivers();
                finish();
                startActivity(new Intent(MainActivity.this, DriverListActivity.class));
            }
        });


    }

    private void drawAutocompleteIntent() {
        // Set the fields to specify which types of place data to
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(MainActivity.this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());

                if (place != null && source) {
                    LatLng latLng = place.getLatLng();
                    source_lat = latLng.latitude;
                    source_long = latLng.longitude;
                    source_location.setText(place.getName());

                }

                if (place != null && destination) {
                    LatLng latLng = place.getLatLng();
                    destination_lat = latLng.latitude;
                    destination_long = latLng.longitude;
                    source_destination.setText(place.getName());
                    filterRelevantDrivers();
                    drawMap();

                    SharedPreferences.Editor location = getSharedPreferences("LOCATION", MODE_PRIVATE).edit();
                    location.putString("source", source_location.getText().toString());
                    location.putString("destination", source_destination.getText().toString());
                    location.apply();
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean somePermissionWasDenied = false;
                    for (int result : grantResults) {
                        if (result == PackageManager.PERMISSION_DENIED) {
                            somePermissionWasDenied = true;
                        }
                    }
                    if (somePermissionWasDenied) {
                        Toast.makeText(this, "Cant load maps without all the permissions granted", Toast.LENGTH_SHORT).show();
                    } else {
                        setupMap();
                        getLocation();
                    }
                } else {
                    Toast.makeText(this, "Cant load maps without all the permissions granted", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_locate) {
            setCenterInMyCurrentLocation();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
    }

    private void checkPermissionsState() {
        int internetPermissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);

        int networkStatePermissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE);

        int writeExternalStoragePermissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int coarseLocationPermissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        int fineLocationPermissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        int wifiStatePermissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_WIFI_STATE);

        if (internetPermissionCheck == PackageManager.PERMISSION_GRANTED &&
                networkStatePermissionCheck == PackageManager.PERMISSION_GRANTED &&
                writeExternalStoragePermissionCheck == PackageManager.PERMISSION_GRANTED &&
                coarseLocationPermissionCheck == PackageManager.PERMISSION_GRANTED &&
                fineLocationPermissionCheck == PackageManager.PERMISSION_GRANTED &&
                wifiStatePermissionCheck == PackageManager.PERMISSION_GRANTED) {

            setupMap();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_WIFI_STATE},
                    MULTIPLE_PERMISSION_REQUEST_CODE);
        }
    }

    private void setupMap() {

        final Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        mapView = (MapView) findViewById(R.id.mapview);
        //setContentView(mapView); //displaying the MapView

        mapView.getController().setZoom(20); //set initial zoom-level, depends on your need
        //mapView.getController().setCenter(ONCATIVO);
        //mapView.setUseDataConnection(false); //keeps the mapView from loading online tiles using network connection.
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(false);
        mapView.setMultiTouchControls(true);

        MyLocationNewOverlay oMapLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx), mapView);
        mapView.getOverlays().add(oMapLocationOverlay);
        oMapLocationOverlay.enableFollowLocation();
        oMapLocationOverlay.enableMyLocation();
        oMapLocationOverlay.enableFollowLocation();

//        CompassOverlay compassOverlay = new CompassOverlay(this, mapView);
//        compassOverlay.enableCompass();
//        mapView.getOverlays().add(compassOverlay);

        mapView.setMapListener(new DelayedMapListener(new MapListener() {
            public boolean onZoom(final ZoomEvent e) {
                MapView mapView = (MapView) findViewById(R.id.mapview);

                String latitudeStr = "" + mapView.getMapCenter().getLatitude();
                String longitudeStr = "" + mapView.getMapCenter().getLongitude();

                String latitudeFormattedStr = latitudeStr.substring(0, Math.min(latitudeStr.length(), 7));
                String longitudeFormattedStr = longitudeStr.substring(0, Math.min(longitudeStr.length(), 7));

                Log.i("zoom", "" + mapView.getMapCenter().getLatitude() + ", " + mapView.getMapCenter().getLongitude());
//                TextView latLongTv = (TextView) findViewById(R.id.textView);
//                latLongTv.setText("" + latitudeFormattedStr + ", " + longitudeFormattedStr);
                return true;
            }

            public boolean onScroll(final ScrollEvent e) {
                MapView mapView = (MapView) findViewById(R.id.mapview);

                String latitudeStr = "" + mapView.getMapCenter().getLatitude();
                String longitudeStr = "" + mapView.getMapCenter().getLongitude();

                String latitudeFormattedStr = latitudeStr.substring(0, Math.min(latitudeStr.length(), 7));
                String longitudeFormattedStr = longitudeStr.substring(0, Math.min(longitudeStr.length(), 7));

                Log.i("scroll", "" + mapView.getMapCenter().getLatitude() + ", " + mapView.getMapCenter().getLongitude());
//                TextView latLongTv = (TextView) findViewById(R.id.textView);
//                latLongTv.setText("" + latitudeFormattedStr + ", " + longitudeFormattedStr);
                return true;
            }
        }, 1000));


    }

    private void drawMap() {
        RoadManager roadManager = new OSRMRoadManager(this);

        ArrayList<GeoPoint> waypoints = new ArrayList<>();
        GeoPoint startPoint = new GeoPoint(source_lat, source_long);
        waypoints.add(startPoint);

        Marker startMarker = new Marker(mapView);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setIcon(getResources().getDrawable(R.drawable.ic_pin));

        GeoPoint endPoint = new GeoPoint(destination_lat, destination_long);
        waypoints.add(endPoint);

        Marker endMarker = new Marker(mapView);
        endMarker.setPosition(endPoint);
        endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        endMarker.setIcon(getResources().getDrawable(R.drawable.ic_pin));

        Road road = roadManager.getRoad(waypoints);
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
        mapView.getOverlays().add(startMarker);
        mapView.getOverlays().add(endMarker);
        mapView.getOverlays().add(roadOverlay);

//        BoundingBox boundingBox = new BoundingBox();
//        boundingBox.set(startPoint.getLatitude(), startPoint.getLatitude(), endPoint.getLatitude(), endPoint.getLongitude());
//        mapView.zoomToBoundingBox(boundingBox, true);

        mapView.invalidate();
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void setCenterInMyCurrentLocation() {
        if (mLastLocation != null) {
            mapView.getController().setCenter(new GeoPoint(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Getting current location", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertMapData() {

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.spinner);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        dialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("UserID", uid);
            jsonObject.put("StartingLat", source_lat);
            jsonObject.put("StartingLong", source_long);
            jsonObject.put("DestinationLat", destination_lat);
            jsonObject.put("DestinationLong", destination_long);

            final String mRequestBody = jsonObject.toString();


            StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_ACCEPT_ROUTE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dialog.cancel();
                    Log.i("LOG_VOLLEY", response);
                    Toast.makeText(MainActivity.this, "Route Saved", Toast.LENGTH_LONG).show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_VOLLEY", error.toString());
                    Toast.makeText(MainActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    protected List<String> Start() {

        db.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                id.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
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

//    private void checkAndAddPermission() {
//        List<String> permissionsNeeded = new ArrayList<>();
//
//        final List<String> permissionsList = new ArrayList<>();
//        if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_FINE_LOCATION))
//            permissionsNeeded.add("GPS");
//
//        if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_COARSE_LOCATION))
//            permissionsNeeded.add("Coarse");
//
//        if (permissionsList.size() > 0) {
//            if (permissionsNeeded.size() > 0) {
//                // Need Rationale
//                String message = "You need to grant access to " + permissionsNeeded.get(0);
//                for (int i = 1; i < permissionsNeeded.size(); i++)
//                    message = message + ", " + permissionsNeeded.get(i);
//
//                ActivityCompat.requestPermissions(MainActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
//                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
//            } else {
//                ActivityCompat.requestPermissions(MainActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
//                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
//            }
//        }
//    }

    public double getLatitude() {

        if (mLocation != null) {

            mLatitude = mLocation.getLatitude();
        }
        return mLatitude;
    }

    /**
     * @return longitude
     * function to get longitude
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
                        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATE, (android.location.LocationListener) this);

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


        } catch (Exception e) {

            e.printStackTrace();
        }


        return mLocation;
    }

    public void executeHandler() {
        //If the handler and runnable are null we create it the first time.
        if (handler == null && runnable == null) {
            handler = new Handler();

            runnable = new Runnable() {
                @Override
                public void run() {
                    //Updating firebase store
                  //  addDrivers();

                    //And we execute it again
                    handler.postDelayed(this, EVERY_FIVE_SECOND);
                }
            };
        }
        //If the handler and runnable are not null, we execute it again when the app is resumed.
        else {
            handler.postDelayed(runnable, EVERY_FIVE_SECOND);
        }
    }

    private void addDrivers() {
//        Log.i("111", "111");
        double latitude = 0.0;
        double longitude = 0.0;
        GpsLocationTracker mGpsLocationTracker = new GpsLocationTracker(MainActivity.this);

        /**
         * Set GPS Location fetched address
         */
        if (mGpsLocationTracker.canGetLocation()) {
            latitude = mGpsLocationTracker.getLatitude();
            longitude = mGpsLocationTracker.getLongitude();

        } else {
            mGpsLocationTracker.showSettingsAlert();
        }


        Random random = new Random();
        Log.i("size ", String.valueOf(Start().size()));

        SharedPreferences selfStore = getSharedPreferences("userStore", MODE_PRIVATE);
        String uid = selfStore.getString("UId", null);

        if (Start().size() > 0) {
            for (int j = 0; j < Start().size(); j++) {
//                Log.d("111x", String.valueOf(Start().get(j)));
                if (Start().get(j).equalsIgnoreCase(uid)) {
                    Log.i("replica", "sssss"); //update
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("drivers").child(uid);
                    Driver driver = new Driver(uid, longitude, latitude);
                    databaseReference.setValue(driver);
                    break;
//            }
                } else {
//                    Log.i("added1", "qqqq");

                    Driver driver = new Driver(uid, longitude, latitude);
                    String id = db.push().getKey();
                    db.child(uid).setValue(driver);
                    break;
                }
            }
        }
//
        else {
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
//        filterRelevantDrivers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //we remove the callback
        handler.removeCallbacks(runnable);
        //and we set the status to offline.
        //updateStatusToOffline();
    }

    protected void getUserLocations(final FirebaseSuccessListener firebaseSuccessListener) {
        Log.d("getUserLocations()", "getUserLocations()");
        db.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                location.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
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
                Log.d("loc1", String.valueOf(location.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }

        });
        Log.d("loc", String.valueOf(location.size()));
        //return location;
    }

    //    String c = "U1558711443502";
    public void filterRelevantDrivers() {
    Log.d("filterRelevantDrivers", "filterRelevantDrivers");
        getUserLocations(new FirebaseSuccessListener() {
            @Override
            public void locationBean(List<LocationBean> locations) {
                if (locations.size() > 0) {
                    Log.d("ccc", String.valueOf(locations.size()));
                    for (int i = 0; i < locations.size(); i++) { //get passenger location //todo get it from search
                        if (locations.get(i).getUid().equals(uid)) {
                            pLatitude = source_lat;
//                            pLatitude = locations.get(i).getLatitude();
                            pLongitude = source_long;
//                            pLongitude = locations.get(i).getLongitude();
                            //locations.remove(locations.get(i));
                            Log.d("size1", String.valueOf(locations.size()));
                            break;
                        }
                    }

                    v(locations);


                    //return selectedList;
                } else {
                    Log.d("diu", String.valueOf(locations.size()));
                }
                //return selectedList;
            }
        });

    }

    public void v(List<LocationBean> locations) {

        for (int i = 0; i < locations.size(); i++) {
            Log.d("size2", String.valueOf(locations.size()));
            double dLatitude = locations.get(i).getLatitude();
            double dLongitude = locations.get(i).getLongitude();
            Location.distanceBetween(pLatitude, pLongitude, dLatitude, dLongitude, results);
            float distanceInMeters = results[0];
//                        Log.d("dist", String.valueOf(distanceInMeters));
            isWithin1km = distanceInMeters < 5000; //todo 5km added
            if (isWithin1km) {
                Log.d("isWithin1km", String.valueOf(isWithin1km));
                LocationBean selectedUsers = new LocationBean();
                selectedUsers.setUid(locations.get(i).getUid());
//                selectedUsers.setLatitude(locations.get(i).getLatitude());
//                selectedUsers.setLongitude(locations.get(i).getLongitude());
                selectedList.add(selectedUsers);

            }
        }
        LocationBean selectedUsers1 = new LocationBean();
        selectedUsers1.setUid(uid);
        selectedList.add(selectedUsers1);
//        for(int i=0; i< selectedList.size();i++){
//            Log.d("qq1", String.valueOf(selectedList.get(1)));
//            Log.d("qq2", String.valueOf(selectedList.get(i).getLatitude()));
//            Log.d("qq3", String.valueOf(selectedList.get(i).getLongitude()));
//        }
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONArray jArrayInput = new JSONArray();
        JSONArray jArrayInput1 = new JSONArray();
        JSONObject jObjectInput = new JSONObject();

        Map<String, String> driverUID = new HashMap<>();
        List<JSONObject> newList = new ArrayList();
        for (int i = 0; i < selectedList.size(); i++) {
            try {
                JSONObject jObjectInput1 = new JSONObject();
                jObjectInput1.put("UID", selectedList.get(i).getUid());
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

        Log.d("json", String.valueOf(jObjectInput));
//        jArrayInput.put(jObjectInput);
//        Log.d("lol", String.valueOf(    jArrayInput    ));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, PYTHON_URL_POST_DATA_AVAILABLE, jObjectInput, new Response.Listener<JSONObject>() {
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //here is the main place where we need to work on.
        int id = item.getItemId();
        switch (id) {

            case R.id.nav_person:
                navigationView.setCheckedItem(R.id.nav_person);
                finish();
                Intent h = new Intent(MainActivity.this, NewUserActivity.class);
                startActivity(h);
                break;
            case R.id.nav_preferences:
                navigationView.setCheckedItem(R.id.nav_preferences);
                finish();
                Intent i = new Intent(MainActivity.this, AddPreferenceActivity.class);
                startActivity(i);
                break;
            case R.id.nav_vehicle:
                finish();
                navigationView.setCheckedItem(R.id.nav_vehicle);
                Intent g = new Intent(MainActivity.this, VehicleActivity.class);
                startActivity(g);
                break;
            case R.id.nav_payment:
                navigationView.setCheckedItem(R.id.nav_payment);
                finish();
                Intent s = new Intent(MainActivity.this, PaymentActivity.class);
                startActivity(s);
                break;
            case R.id.nav_trips:
                navigationView.setCheckedItem(R.id.nav_trips);
                finish();
                Intent q = new Intent(MainActivity.this, TripHistoryView.class);
                startActivity(q);
                break;

            case R.id.signout:
                navigationView.setCheckedItem(R.id.signout);
                finish();
                Intent t = new Intent(MainActivity.this, Login.class);
                startActivity(t);
                break;

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


//    private void getReportedDrivers() {
//        JsonArrayRequest request = new JsonArrayRequest(JSON_URL_REPORTED_DRIVERS+uid, new Response.Listener<JSONArray>() {
//
//            public void onResponse(JSONArray response) {
//                JSONObject jsonObject = null;
//
//                for(int i= 0; i<response.length(); i++){
//                    try{
//                        jsonObject = response.getJSONObject(i);
//                        if(jsonObject.length()!=0) {
//
//
//                        }
//                        else{
//
//                        }
//                    }catch (JSONException e){
//                        e.printStackTrace();
//                    }
//                }
//
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
//        requestQueue.add(request);
//
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            Intent map = new Intent(MainActivity.this, UserMain.class);
            startActivity(map);
        }
    }
}
