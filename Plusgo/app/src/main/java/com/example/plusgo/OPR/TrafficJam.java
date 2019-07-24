/*
 * *
 *  * Created by Athrie Nathasha
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/9/19 7:12 PM
 *
 */

package com.example.plusgo.OPR;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.plusgo.BaseContent;
import com.example.plusgo.FC.MapCurrentPassengerActivity;
import com.example.plusgo.OPR.helpers.CroudSourcingNotificationHelper;
import com.example.plusgo.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TrafficJam extends AppCompatActivity {
/*
    ImageView xImageview;
    Button addimage;


    private static final int IMAGE_PICK_CODE =1000;
    private static final int PERMISSION_CODE =1001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_traffic_jam);
        xImageview=findViewById(R.id.imageViewaddimg);
        addimage=findViewById(R.id.add_img_button);

        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                {

                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String [] permissions={Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions,PERMISSION_CODE);
                    }

                    else{
                        pickImageFromGallery();
                    }
                }
                else{
                    pickImageFromGallery();
                }
            }
        });

    }


    private void pickImageFromGallery(){

        Intent intent =new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery();
                }
                else{
                    Toast.makeText(this,"Permission denied !",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK && requestCode ==IMAGE_PICK_CODE){
            //set image to image view
            xImageview.setImageURI(data.getData());
        }
    }


*/
private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2,AUTOCOMPLETE_REQUEST_CODE = 3;
    ImageView image_view;
    Button btnlater;
    Button btnreport;
    Button btn_upload_traffic;
    RadioButton radio_standstill;
    RadioButton radio_moderate;
    RadioButton radio_heavy;
    //String URL = "http://192.168.8.100:8083/roadcause/";
    private BaseContent BASECONTENT = new BaseContent();
    private String JSON_URL_TRAFFIC_DATA = BASECONTENT.OPRBASEIPROUTE + ":8083/roadcause/";
    RequestQueue rQueue;
    private String STATUS_ACTIVE = "ACTIVE", STATUS_INACTIVE = "INACTIVE", TYPE = "TRAFIC", SITUATION; // 1-> standstill, 2-> moderate, 3-> heavy
    private Bitmap bitmap;
    private File destination = null;
    private InputStream inputStreamImg;
    private String imgPath = null;


    private String TAG = "TrafficJam";

    EditText input_location;

    private GoogleApiClient mGoogleApiClient;
    private double source_lat, source_long;
    private String place_str;
    private CroudSourcingNotificationHelper croudSourcingNotificationHelper = new CroudSourcingNotificationHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_jam);
        Places.initialize(this, "AIzaSyD_gxGkVFP_a2v0_VopWLlvFZe-u46TV7M"); //Please put application code in a secure place


        image_view = findViewById(R.id.image_view_traffic);
        btnlater = findViewById(R.id.btnlater);
        btnreport = findViewById(R.id.btnreport);
        input_location = findViewById(R.id.input_location);
        radio_standstill = findViewById(R.id.radio_standstill);
        radio_moderate = findViewById(R.id.radio_moderate);
        radio_heavy = findViewById(R.id.radio_heavy);

        btn_upload_traffic = findViewById(R.id.btn_upload_traffic);
        btn_upload_traffic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btnreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertRoadCause(STATUS_ACTIVE);
            }
        });

        btnlater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertRoadCause(STATUS_INACTIVE);
            }
        });

        final boolean flag_standstill = false, flag_moderate = false, flag_heavy = false;

        input_location.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                input_location.setPressed(true);
                input_location.setSelection(input_location.getText().length());
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    drawAutocompleteIntent();
                }
                return false;
            }
        });



        radio_standstill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radio_standstill.isChecked()) {
                    SITUATION = "1";
                }
            }
        });

        radio_moderate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radio_moderate.isChecked()) {
                    SITUATION = "2";
                }
            }
        });

        radio_heavy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radio_heavy.isChecked()) {
                    SITUATION = "3";
                }
            }
        });

    }


    // Select image from camera and gallery
    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
            } else {
                if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                    final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setTitle("Select Option");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals("Take Photo")) {
                                dialog.dismiss();
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, PICK_IMAGE_CAMERA);
                            } else if (options[item].equals("Choose From Gallery")) {
                                dialog.dismiss();
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                            } else if (options[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
                } else
                    Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inputStreamImg = null;
        if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                Uri selectedImage = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                Log.e("Activity", "Pick from Camera::>>> ");

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(Environment.getExternalStorageDirectory() + "/" +
                        getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imgPath = destination.getAbsolutePath();
                image_view.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                Log.e("Activity", "Pick from Gallery::>>> ");

                imgPath = getRealPathFromURI(selectedImage);
                destination = new File(imgPath.toString());
                image_view.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());

                if (place != null) {
                    LatLng latLng = place.getLatLng();
                    source_lat = latLng.latitude;
                    source_long = latLng.longitude;
                    place_str = place.getName();
                    input_location.setText(place.getName());
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

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void insertRoadCause(final String status) {

        final Dialog dialog = new Dialog(TrafficJam.this);
        dialog.setContentView(R.layout.spinner);
        dialog.setTitle("Uploading ...");
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        // set the custom dialog components - text, image and button

//        ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.Circle);
//        Sprite animation = new FoldingCube();
//       progressBar.setIndeterminateDrawable(animation);

        dialog.show();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        final String encoded_image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        StringRequest sr = new StringRequest(Request.Method.POST, JSON_URL_TRAFFIC_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                rQueue.getCache().clear();
                dialog.cancel();
                Toast.makeText(getApplicationContext(), "Your report is added", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancel();
                rQueue.getCache().clear();
                Log.e("error", error.toString());
                Toast.makeText(getApplicationContext(), "Upload Failed", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("type", TYPE);
                params.put("username", "U0100");
                params.put("destination", input_location.getText().toString());
                params.put("situation", SITUATION);
                params.put("latitude", "34343");
                params.put("longitude", "43434");
                params.put("image", encoded_image);
                params.put("status", status);
                return params;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        rQueue = Volley.newRequestQueue(TrafficJam.this);
        rQueue.add(sr);

        popUpNotification();

    }
//    @Override
//    public void onLocationChanged(Location location) {
//
//    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void drawAutocompleteIntent() {
        // Set the fields to specify which types of place data to
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    public void popUpNotification() {
        DatabaseReference refOnlineDrivers = croudSourcingNotificationHelper.getOnlineDriversDatabaseReference();
        refOnlineDrivers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                float[] dist = new float[1];

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Location.distanceBetween(source_lat, source_long, ds.child("latitude").getValue(Double.class), ds.child("longitude").getValue(Double.class), dist);
                    if(dist[0]/1000 <= 1) //within 1Km radius area
                        showNotification("Traffic", "You will facing a huge traffic near " + place_str  );
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void showNotification(String title, String content) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("CHANNEL_DISCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(content)// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), MapCurrentPassengerActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }

}