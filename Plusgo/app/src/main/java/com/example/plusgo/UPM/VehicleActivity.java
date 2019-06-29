/*
 * *
 *  * Created by Viraj Wickramasinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/24/19 7:40 PM
 *
 */

package com.example.plusgo.UPM;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.example.plusgo.OPR.MainActivity;
import com.example.plusgo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

public class VehicleActivity extends Activity {
    private Spinner brand, model, manYear, regYear, fuelType, tType, capacity;
    private int pos;
    private String id;
    private ImageButton frontView;
    private static final String KEY_EMPTY = "";
    private TextView vNo, mileage;
    public static final int PICK_IMAGE = 1;
    static final int PERMISSION_CODE = 1000;
    private Button btnAddVehicle;
    private RequestQueue requestQueue;
    BaseContent BASECONTENT = new BaseContent();
    private String JSON_URL_IMAGE = BASECONTENT.IpAddress + "/upload";
    private String JSON_URL_ADD_VEHICLE = BASECONTENT.IpAddress + "/vehicle/";
    private String JSON_URL_GET_VEHICLE = BASECONTENT.IpAddress + "/vehicle/specific/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);

        SharedPreferences user = getSharedPreferences("userStore",MODE_PRIVATE);
        id = user.getString("UId", null);

        setValuesVehicle();

        frontView = (ImageButton) findViewById(R.id.btnFront);

        brand = findViewById(R.id.brand);
        String[] brandV = new String[]{"Alto", "Suzuki","Toyota"};
        ArrayAdapter<String> adapterBrand = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, brandV);
        brand.setAdapter(adapterBrand);

        manYear = findViewById(R.id.mYear);
        String[] manYearV = new String[]{"1999","2000","2001","2002","2003","2004","2005","2006","2007","2008","2009","2010","2011","2012"};
        ArrayAdapter<String> adapterManYear = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, manYearV);
        manYear.setAdapter(adapterManYear);

        regYear = findViewById(R.id.rYear);
        String[] regYearV = new String[]{"1999","2000","2001","2002","2003","2004","2005","2006","2007","2008","2009","2010","2011","2012"};
        ArrayAdapter<String> adapterRegYear = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, regYearV);
        regYear.setAdapter(adapterRegYear);

        fuelType = findViewById(R.id.fuel);
        String[] fuelTypeV = new String[]{"Petrol", "Diesel", "Hybrid", "Electric"};
        ArrayAdapter<String> adapterFuelTypeV = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, fuelTypeV);
        fuelType.setAdapter(adapterFuelTypeV);

        tType = findViewById(R.id.transmission);
        String[] tTypeV = new String[]{"Auto", "Manual"};
        ArrayAdapter<String> adapterTTypeV = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tTypeV);
        tType.setAdapter(adapterTTypeV);

        capacity = findViewById(R.id.engine);
        String[] capacityV = new String[]{"900","1000", "1100", "1200", "1300"};
        ArrayAdapter<String> adapterCapacity = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, capacityV);
        capacity.setAdapter(adapterCapacity);

        vNo = (TextView)findViewById(R.id.vehicleno);
        mileage = (TextView)findViewById(R.id.mileage);


        brand.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                pos = arg2;
                populateModel();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        frontView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });

        btnAddVehicle = (Button)findViewById(R.id.btnAddVehicle);
        btnAddVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addVehicle();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                Log.d("q1", selectedImage.toString());
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                Log.d("q2", String.valueOf(columnIndex));

                String picturePath = cursor.getString(columnIndex);
                Log.d("q3", picturePath); //todo check fr files
                cursor.close();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                    PackageManager.PERMISSION_DENIED) {
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);

                    } else {
                        Log.d("qqq", "front");
                        frontView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                        uploadBitmap(BitmapFactory.decodeFile(picturePath));
                    }

                }
            }
        }
    }

    private void populateModel() {
        switch(pos)
        {
            case 0:
                model = findViewById(R.id.model);
                String[] modelV = new String[]{"Maruti Alto 800","Dzire", "Ertiga", "Vitara"};
                ArrayAdapter<String> adapterModel = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, modelV);
                model.setAdapter(adapterModel);
                break;
            case 1:
                model = findViewById(R.id.model);
                String[] modelV1 = new String[]{"Suzuki Swift", "Suzuki Baleno","Wagon R","Celerio"};
                ArrayAdapter<String> adapterModel1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, modelV1);
                model.setAdapter(adapterModel1);
                break;

            case 2:
                model = findViewById(R.id.model);
                String[] modelToyota = new String[]{"Avalon", "Corolla", "Land Cruiser","Camry"};
                ArrayAdapter<String> adapterModelToyota = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, modelToyota);
                model.setAdapter(adapterModelToyota);
                break;
        }

    }
    //upload image into the backend server
    private void uploadBitmap(final Bitmap bitmap) {
        Log.d("qqq1", bitmap.toString());
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, JSON_URL_IMAGE,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
//                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
//                            finish();
//
//                            Intent intent = new Intent(VehicleActivity.this, NewUserActivity.class);

                            //Convert to byte array
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);


                            byte[] byteArray = stream.toByteArray();
                            Log.d("www", byteArray.toString());
                            Log.d("www1", String.valueOf(byteArray.length));
                            Log.d("www1", String.valueOf(byteArray != null));

//                            intent.putExtra("img",byteArray);


//                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("www1", error.toString());
                        Toast.makeText(getApplicationContext(), "Error occured", Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tags", "image");
                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                String imagename = id +"V";

                SharedPreferences.Editor store = getSharedPreferences("camera", MODE_PRIVATE).edit();
                store.putString("photo", imagename + ".jpg");
                store.apply();

                params.put("image", new DataPart(imagename + ".jpg", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        switch(requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else{
                    Toast.makeText(this, "Permission denied", LENGTH_SHORT).show();
                }
            }
        }
    }
    //convert the image into byte format
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    //add relevant vehicle details
    public void addVehicle() {
        try {

            //check whether the fields are empty or not
            if(KEY_EMPTY.equals(vNo.getText().toString().trim()) || KEY_EMPTY.equals(mileage.getText().toString().trim())){
                Toast.makeText(VehicleActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();

            }
            else {
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                JSONObject jsonObject = new JSONObject();

                SharedPreferences cameraStore = getSharedPreferences("camera",MODE_PRIVATE);
                String imageURL = cameraStore.getString("photo", null);

                jsonObject.put("UserID", id);
                jsonObject.put("Brand", brand.getSelectedItem().toString());
                jsonObject.put("Model", model.getSelectedItem().toString());
                jsonObject.put("VNumber", vNo.getText());
                jsonObject.put("Mileage", mileage.getText());
                jsonObject.put("MYear", manYear.getSelectedItem().toString());
                jsonObject.put("RYear", regYear.getSelectedItem().toString());
                jsonObject.put("FuelType", fuelType.getSelectedItem().toString());
                jsonObject.put("TType", tType.getSelectedItem().toString());
                jsonObject.put("EngineCapacity", capacity.getSelectedItem().toString());
                jsonObject.put("FrontView", "/images/"+imageURL);
                final String mRequestBody = jsonObject.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_ADD_VEHICLE, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_VOLLEY", response);
                        Toast.makeText(VehicleActivity.this, "Profile Created", Toast.LENGTH_LONG).show();
                        finish();
                        Intent intent = new Intent(VehicleActivity.this, Login.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_VOLLEY", error.toString());
                        Toast.makeText(VehicleActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
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

    //set values to vehicle fields
    private void setValuesVehicle() {

        Log.e("JSONREQUEST","started");
        JsonArrayRequest request = new JsonArrayRequest(JSON_URL_GET_VEHICLE+id, new Response.Listener<JSONArray>() {

            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.user2).error(R.drawable.user2);

                for(int i= 0; i<response.length(); i++){
                    try{
                        jsonObject = response.getJSONObject(i);
                        if(jsonObject.length()!=0) {

                            Log.d("qqq", jsonObject.getString("VNumber").toString());

                            brand.setSelection(((ArrayAdapter<String>) brand.getAdapter()).getPosition(jsonObject.getString("Brand")));
                            model.setSelection(((ArrayAdapter<String>) model.getAdapter()).getPosition(jsonObject.getString("Model")));
                            vNo.setText(jsonObject.getString("VNumber"));
                            mileage.setText(jsonObject.getString("Mileage"));
                            manYear.setSelection(((ArrayAdapter<String>) manYear.getAdapter()).getPosition(jsonObject.getString("MYear")));
                            regYear.setSelection(((ArrayAdapter<String>) regYear.getAdapter()).getPosition(jsonObject.getString("RYear")));
                            fuelType.setSelection(((ArrayAdapter<String>) fuelType.getAdapter()).getPosition(jsonObject.getString("FuelType")));
                            tType.setSelection(((ArrayAdapter<String>) tType.getAdapter()).getPosition(jsonObject.getString("TType")));
                            capacity.setSelection(((ArrayAdapter<String>) capacity.getAdapter()).getPosition(jsonObject.getString("EngineCapacity")));


                                //To load image using Glide
                            Glide.with(VehicleActivity.this)
                                    .load(BASECONTENT.IpAddress + jsonObject.getString("FrontView"))
                                    .apply(requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .skipMemoryCache(true))
                                    .into(frontView);


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

        requestQueue = Volley.newRequestQueue(VehicleActivity.this);
        requestQueue.add(request);

    }

    @Override
    public void onBackPressed() {
        finish();
        Intent map = new Intent(VehicleActivity.this, MainActivity.class);
        startActivity(map);
    }
}
