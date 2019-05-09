/*
 * *
 *  * Created by Ashane Edirisinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/4/19 12:16 AM
 *
 */

package com.example.plusgo.dvprm;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.plusgo.R;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class NicUploadActivity extends Activity {

    Button button;
    ImageView imageview;
    static final int PERMISSION_CODE = 1000;
    static final int IMAGE_CAPTURE_CODE = 1001;
    final int CROP_PIC = 2;


    Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nic_upload);

        button=(Button)findViewById(R.id.nic_upload);
        imageview=(ImageView)findViewById(R.id.image_view_nic);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED ||
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
    }

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
           Log.e("EEEEEEEEEEE", String.valueOf(image_uri));
           Log.e("FFFFFFFFF", String.valueOf(MediaStore.Images.Media.EXTERNAL_CONTENT_URI));

//           imageview.setImageURI(image_uri);
           if (requestCode == CROP_PIC) {
               Bundle extras = data.getExtras();
               Bitmap thePic = extras.getParcelable("data");
//               imageview.setImageBitmap(thePic);

               ByteArrayOutputStream bytes = new ByteArrayOutputStream();
               thePic.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
               String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), thePic, "Title", null);

               Log.e("AAAAAAA", path);
               imageview.setImageURI(Uri.parse(path));

           }
       }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else{
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
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
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 4);//change this to make it a square or rectangle
            cropIntent.putExtra("aspectY", 3);//change this to make it a square or rectangle
            // indicate output X and Y
//            cropIntent.putExtra("outputX", 3096);
//            cropIntent.putExtra("outputY", 4128);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);

            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
