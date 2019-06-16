package com.example.plusgo.DVPRM;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
    String NIC_URL ="http://192.168.1.4/nic/test.png";
    String ELEC_NIC_URL ="http://192.168.1.4/nic/test.png";
//    String LISENCE_URL ="http://192.168.1.4/lisence/test.png";

    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    public String ExtractedNIC,EditedNIC;
    LinearLayout niclayout;

    Button uploadNICbtn,verifyNICbtn,proceedNICbtn,editNIC;
    EditText EditExtractedNIC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nic_up);

        niclayout = (LinearLayout) findViewById(R.id.nic_up_layout);
        niclayout.setVisibility(View.INVISIBLE);

        EditExtractedNIC =(EditText)findViewById(R.id.extractednic);
        EditExtractedNIC.setEnabled(false);

        uploadNICbtn =(Button)findViewById(R.id.nic_upload_up);
        uploadNICbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedNICbtn.setVisibility(View.INVISIBLE);
                niclayout.setVisibility(View.INVISIBLE);
                Intent verify = new Intent(NicUpActivity.this, DocUploadActivity.class);
                startActivity(verify);
            }
        });

        proceedNICbtn =(Button)findViewById(R.id.proceednic);
        proceedNICbtn.setVisibility(View.INVISIBLE);
        proceedNICbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("dvprm", MODE_PRIVATE).edit();
                editor.putString("nic_validated", String.valueOf(EditExtractedNIC.getText()));
                editor.putString("dv_type", "lisence");
                editor.apply();

                Intent verify1 = new Intent(NicUpActivity.this, LisenceUpActivity.class);
                startActivity(verify1);
                finish();
            }
        });

        verifyNICbtn =(Button)findViewById(R.id.nic_upload_verify);
        verifyNICbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                niclayout.setVisibility(View.VISIBLE);
               jsonrequest();

            }
        });

        editNIC =(Button)findViewById(R.id.editNICnum);
        editNIC.setVisibility(View.GONE);
        editNIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editnic();
            }
        });

        EditExtractedNIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    EditExtractedNIC.setTooltipText("This conversion adds born year infront of usual 9 digit NIC number and '0' as the 6th digit ");
                }
                Toast.makeText(getBaseContext(), "This conversion adds born year infront of usual 9 digit NIC number and '0' as the 6th digit", Toast.LENGTH_LONG).show();
                proceedNICbtn.setVisibility(View.INVISIBLE);
                editNIC.setVisibility(View.VISIBLE);
            }
        });
    }

    //Retrieve NIC
    private void jsonrequest() {
        String JSON_URL = null;
        SharedPreferences preferences = getSharedPreferences("dvprm", Context.MODE_PRIVATE);
        String dv_type = preferences.getString("dv_type", "general");

       if(dv_type.equals("electronic")){
            JSON_URL = ELEC_NIC_URL;
        }else{
            JSON_URL = NIC_URL;
        }

        final ProgressDialog loading = ProgressDialog.show(this, "Verifying Image...", "Please wait...", false, false);
        Log.e("JSONREQUEST","started");
        Log.e("JSON_URL_FIRST",JSON_URL);
        final String finalJSON_URL = JSON_URL;
        request = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                Log.e("JSON_URL", finalJSON_URL);
                JSONObject jsonObject = null;
                if(response.length() > 0){
                    try{
                        jsonObject = response.getJSONObject(0);

                        ExtractedNIC = jsonObject.getString("ExtractedNIC");
                        String EXT= ExtractedNIC.replaceAll("\\[", "").replaceAll("\\]","").replaceAll("\"", "").replaceAll(",","");
                        Log.e("ExtractedNIC", EXT);
                        if(jsonObject.getString("ExtractedNIC") != null){
                            EditExtractedNIC.setText(EXT);
                            EditExtractedNIC.setEnabled(true);
                            if(EXT.length() == 12) {
                                proceedNICbtn.setVisibility(View.VISIBLE);
                            }
//                            editNIC.setVisibility(View.VISIBLE);
                            SharedPreferences.Editor editor = getSharedPreferences("dvprm", MODE_PRIVATE).edit();
                            editor.putString("nic_identified", EXT);
                            editor.apply();
                        }
                    }catch (JSONException e){
                        loading.dismiss();
                        e.printStackTrace();
                        Log.e("JSONREQUEST","ERROR");
                    }
                }
                loading.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Log.e("JSONREQUEST_ERROR",error.toString());
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue = Volley.newRequestQueue(NicUpActivity.this);
        requestQueue.add(request);
    }

    private void editnic(){
        try {
            int count = 0;
            SharedPreferences preferences = getSharedPreferences("dvprm", Context.MODE_PRIVATE);
            String ExtractNIC = preferences.getString("nic_identified", "000000000000");
            EditedNIC = String.valueOf(EditExtractedNIC.getText());
            for (int i = 0; i < 12; i++) {
                if (ExtractNIC.charAt(i) == EditedNIC.charAt(i)) {
                    count = count + 1;
                }
            }
            Log.e("MATCHED_COUNT", String.valueOf(count));
//        Log.e("MATCHED_COUNT", ExtractNIC);
//        Log.e("MATCHED_COUNT", String.valueOf(EditedNIC.charAt(11)));
            if (count < 10) {
                Toast.makeText(this, "Please upload an image again", Toast.LENGTH_SHORT).show();
                proceedNICbtn.setVisibility(View.INVISIBLE);
            } else{
                if(EditedNIC.length() == 12)
                proceedNICbtn.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
            Log.e("editnic_EXCEPTION", e.toString());
            Toast.makeText(this, "Error, Please Upload valid Image ", Toast.LENGTH_SHORT).show();
        }
    }
}
