package com.example.plusgo.DVPRM;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.plusgo.BaseContent;
import com.example.plusgo.app.AppController;
import com.example.plusgo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DocUploadActivity extends AppCompatActivity {

    BaseContent BASECONTENT = new BaseContent();
    Button buttonChoose;
    FloatingActionButton buttonUpload;
    Toolbar toolbar;
    ImageView imageView;
    EditText txt_name;
    Bitmap bitmap, decoded;
    int success;
    int PICK_IMAGE_REQUEST = 1;
    int bitmap_size = 100; // range 1 - 100

    private static final String TAG = DocUploadActivity.class.getSimpleName();

    //TODO : path of the phpfile in the server
    String NIC_URL_UPLOAD = BASECONTENT.phpIP+"/android/uploadnic.php";
    String ELEC_NIC_URL_UPLOAD = BASECONTENT.phpIP+"/android/uploadnic.php";
    String LISENCE_URL_UPLOAD = BASECONTENT.phpIP+"/android/uploadlisence.php"; //TODO

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_upload);

        DisplayMetrics dm1 = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm1);

        int width = dm1.widthPixels;
        int height = dm1.heightPixels;

        getWindow().setLayout((int)(width*.80),(int)(height*.50));

        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (FloatingActionButton) findViewById(R.id.buttonUpload);

        txt_name = (EditText) findViewById(R.id.editText1);

        imageView = (ImageView) findViewById(R.id.imageView1);

        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage() {

        String UPLOAD_URL = null;
        SharedPreferences preferences = getSharedPreferences("dvprm", Context.MODE_PRIVATE);
        String dv_type = preferences.getString("dv_type", "general");

        if(dv_type.equals("lisence")) {
            UPLOAD_URL = LISENCE_URL_UPLOAD;
        }else if(dv_type.equals("electronic")){
            UPLOAD_URL = ELEC_NIC_URL_UPLOAD;
        }else{
            UPLOAD_URL = NIC_URL_UPLOAD;
        }

            //progress dialog
            final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e(TAG, "Response: " + response.toString());
                            try {
                                JSONObject jObj = new JSONObject(response);
                                success = jObj.getInt(TAG_SUCCESS);

                                if (success == 1) {
                                    Log.e("v Add", jObj.toString());

                                    Toast.makeText(DocUploadActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                                    kosong();

                                } else {
                                    Toast.makeText(DocUploadActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //progress dialog
                            loading.dismiss();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //progress dialog
                            loading.dismiss();

                            //toast
                            Toast.makeText(DocUploadActivity.this, "Phone incompatibility or server error", Toast.LENGTH_LONG).show();
                            Log.e(TAG,"Response error");
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    //parameters
                    Map<String, String> params = new HashMap<String, String>();

                    params.put(KEY_IMAGE, getStringImage(decoded));
                    params.put(KEY_NAME, txt_name.getText().toString().trim());

                    Log.e(TAG, "" + params);
                    return params;
                }
            };
            Log.e("DDD", stringRequest.toString());
            stringRequest.setShouldCache(false);
            AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                setToImageView(getResizedBitmap(bitmap, 1024));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void kosong() {
        imageView.setImageResource(0);
        txt_name.setText(null);
        finish();
    }

    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        imageView.setImageBitmap(decoded);
    }

    //resize image
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
