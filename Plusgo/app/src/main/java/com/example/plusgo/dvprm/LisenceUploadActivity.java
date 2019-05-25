/*
 * *
 *  * Created by Ashane Edirisinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/19/19 10:12 AM
 *
 */

package com.example.plusgo.dvprm;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.plusgo.BaseContent;
import com.example.plusgo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

public class LisenceUploadActivity extends AppCompatActivity {

    Button capture,upload;
    ImageView imageview;
    static final int PERMISSION_CODE = 1000;
    static final int IMAGE_CAPTURE_CODE = 1001;
    final int CROP_PIC = 2;
    String path;
    BaseContent BASECONTENT = new BaseContent();
    private String JSON_URL = BASECONTENT.IpAddress + "/upload";
    String pathI;
    Bitmap thePicI;
    Uri image_uri;
    String id;

    public String getPathI() {
        return pathI;
    }

    public void setPathI(String pathI) {
        this.pathI = pathI;
    }



    public Bitmap getThePicI() {
        return thePicI;
    }

    public void setThePicI(Bitmap thePicI) {
        this.thePicI = thePicI;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lisence_upload);

        capture=(Button)findViewById(R.id.button);
        upload=(Button)findViewById(R.id.button2);
        upload.setEnabled(false);
        imageview=(ImageView)findViewById(R.id.imageView);

        SharedPreferences mPrefs = getSharedPreferences("teacherStore",MODE_PRIVATE);
        id = mPrefs.getString("TeacherId", null);


        //capturing images from the camera
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                                    PackageManager.PERMISSION_DENIED){
                        String[] permission = {Manifest.permission.CAMERA ,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission,PERMISSION_CODE);

                    }
                    else{
                        openCamera();
                    }
                }
                else{

                }
            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadBitmap(getThePicI());
            }});
    }

    //convert the image into byte format
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    //upload image into the backend server
    private void uploadBitmap(final Bitmap bitmap) {
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();

//                            Intent intent = new Intent(LisenceUploadActivity.this, TeacherProfileActivity.class);

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
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                String imagename = id;

                SharedPreferences.Editor store = getSharedPreferences("camera", MODE_PRIVATE).edit();
                store.putString("photo", imagename + ".jpg");
                store.apply();

                SharedPreferences cax = getSharedPreferences("camera",MODE_PRIVATE);

                params.put("image", new DataPart(imagename + ".jpg", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }


    //ope the camera to take images
    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        performCrop();
        startActivityForResult(cameraIntent,IMAGE_CAPTURE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if (requestCode == CROP_PIC) {
                Bundle extras = data.getExtras();
                Bitmap thePic = extras.getParcelable("data");

                Bitmap newPic = RotateBitmap(thePic, 90);
//                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                newPic.compress(Bitmap.CompressFormat.PNG, 0, bytes);
                path = MediaStore.Images.Media.insertImage(this.getContentResolver(), newPic, "Title", null);

                Matrix matrix = new Matrix();
                matrix.postRotate(90);

                imageview.setImageURI(Uri.parse(path));
                setPathI(path);
                setThePicI(newPic);
                upload.setEnabled(true);

            }
        }
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

    private void performCrop() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(image_uri, "image/*");
            // set crop properties
//            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
//            cropIntent.putExtra("aspectX", 3);//change this to make it a square or rectangle
//            cropIntent.putExtra("aspectY", 4);//change this to make it a square or rectangle
            // indicate output X and Y
//            cropIntent.putExtra("outputX", 1024);
//            cropIntent.putExtra("outputY", 1024);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);

            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(this, "This device doesn't support the crop action!", LENGTH_SHORT);
            toast.show();
        }
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

}
