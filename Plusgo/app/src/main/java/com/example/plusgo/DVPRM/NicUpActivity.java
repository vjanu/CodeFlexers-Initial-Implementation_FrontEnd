package com.example.plusgo.DVPRM;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.plusgo.BaseContent;
import com.example.plusgo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NicUpActivity extends AppCompatActivity {

    BaseContent BASECONTENT = new BaseContent();

    //Add the relavent IP to retrieve NIC from image
    String JSON_URL = "http://192.168.1.4/nic/test.png";

    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    public String ExtractedNIC;


    Button uploadNICbtn,verifyNICbtn,proceedNICbtn;
    EditText EditExtractedNIC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nic_up);

        EditExtractedNIC =(EditText)findViewById(R.id.extractednic);
        EditExtractedNIC.setEnabled(false);

        uploadNICbtn =(Button)findViewById(R.id.nic_upload_up);
        uploadNICbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent verify = new Intent(NicUpActivity.this, DocUploadActivity.class);
                startActivity(verify);
            }
        });

        proceedNICbtn =(Button)findViewById(R.id.proceednic);
        proceedNICbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent verify1 = new Intent(NicUpActivity.this, LisenceUpActivity.class);
                startActivity(verify1);
                finish();
            }
        });

        verifyNICbtn =(Button)findViewById(R.id.nic_upload_verify);
        verifyNICbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               jsonrequest();
            }
        });
    }

    //Retrieve NIC
    private void jsonrequest() {
        Log.e("JSONREQUEST","started");
        Log.e("JSON_URL_FIRST",JSON_URL);
        request = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                Log.e("JSON_URL",JSON_URL);
                JSONObject jsonObject = null;
                if(response.length() > 0){
                    try{
                        jsonObject = response.getJSONObject(0);

                        ExtractedNIC = jsonObject.getString("ExtractedNIC");
                        String EXT= ExtractedNIC.replaceAll("\\[", "").replaceAll("\\]","");
                        Log.e("ExtractedNIC", EXT);
                        if(jsonObject.getString("ExtractedNIC") != null){
                            EditExtractedNIC.setText(EXT.replaceAll("\"", "").replaceAll(",",""));
                            EditExtractedNIC.setEnabled(true);
                        }
//
//                        if(jsonObject.getString("TeacherID") != null){
//                            rateBtn.setText("You have rated");
//                            rateBtn.setEnabled(false);
//                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                        Log.e("JSONREQUEST","ERROR");
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("JSONREQUEST_ERROR",error.toString());
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue = Volley.newRequestQueue(NicUpActivity.this);
        requestQueue.add(request);
    }


}
