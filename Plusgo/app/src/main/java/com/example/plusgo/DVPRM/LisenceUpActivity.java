package com.example.plusgo.DVPRM;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LisenceUpActivity extends AppCompatActivity {

    BaseContent BASECONTENT = new BaseContent();

    //Add the relavent IP to retrieve NIC from image
//    String LISENCE_URL ="http://192.168.1.4:81/lisence/test.png";
    String LISENCE_URL = BASECONTENT.IpAddress +":8088/lisence/test.png";

    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    public String ExtractedNIC,CorrectNIC,Expiration;
    LinearLayout niclayout,exp_layout;

    Button uploadLISbtn,verifyLISbtn,proceedLISbtn,edit_exp;
    TextView extractednic_lis;
    EditText extracteddate_lis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lisence_up);
        SharedPreferences preferences = getSharedPreferences("dvprm", Context.MODE_PRIVATE);
        String nic_validated = preferences.getString("nic_validated", "000000000000");

        niclayout = (LinearLayout) findViewById(R.id.nic_layout);
        exp_layout = (LinearLayout) findViewById(R.id.exp_layout);

        //Set this visible after comparing extracted nic from lisence, if not ask to upload another image
        extractednic_lis =(TextView)findViewById(R.id.extractednic_lis);
        extractednic_lis.setText(nic_validated);
        niclayout.setVisibility(View.INVISIBLE);
        exp_layout.setVisibility(View.INVISIBLE);

        proceedLISbtn=(Button) findViewById(R.id.proceednic_lis);
        proceedLISbtn.setVisibility(View.INVISIBLE);

        edit_exp=(Button) findViewById(R.id.edit_exp);
        edit_exp.setVisibility(View.GONE);

        uploadLISbtn =(Button)findViewById(R.id.lis_upload_up);
        uploadLISbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                niclayout.setVisibility(View.INVISIBLE);
                exp_layout.setVisibility(View.INVISIBLE);
                proceedLISbtn.setVisibility(View.INVISIBLE);
                Intent verify = new Intent(LisenceUpActivity.this, DocUploadActivity.class);
                startActivity(verify);
            }
        });

        verifyLISbtn =(Button)findViewById(R.id.lis_upload_verify);
        verifyLISbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonrequest();
            }
        });


        proceedLISbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String exptempdate = String.valueOf(extracteddate_lis.getText());
                SharedPreferences.Editor editor = getSharedPreferences("dvprm", MODE_PRIVATE).edit();
                editor.putString("expDateValidated", exptempdate);
                editor.apply();
                finish();
            }
        });

        extracteddate_lis =(EditText) findViewById(R.id.extracteddate_lis);
        extracteddate_lis.setEnabled(false);
        extracteddate_lis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_exp.setVisibility(View.VISIBLE);
                proceedLISbtn.setVisibility(View.INVISIBLE);
            }
        });

        edit_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String exptempdate = String.valueOf(extracteddate_lis.getText());
                matchdate(exptempdate);
            }
        });
    }

    //Retrieve Lisence
    private void jsonrequest() {
        String JSON_URL = LISENCE_URL;

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
                        Expiration = jsonObject.getString("Expiration");
                        String EXT= ExtractedNIC.replaceAll("\\[", "").replaceAll("\\]","").replaceAll("\"", "").replaceAll(",","");
                        Log.e("ExtractedNIC_Lis", EXT);
                        Log.e("ExtractedExp_Lis", Expiration);
                        if(jsonObject.getString("ExtractedNIC") != null){

                            SharedPreferences.Editor editor = getSharedPreferences("dvprm", MODE_PRIVATE).edit();
                            editor.putString("nic_identified_lis", EXT);
                            editor.putString("exp_identified_lis", Expiration);
                            editor.apply();

                            matchnic();
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
        request.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue = Volley.newRequestQueue(LisenceUpActivity.this);
        requestQueue.add(request);
    }


    private void matchnic(){
        try {
            int count = 0;
            SharedPreferences preferences = getSharedPreferences("dvprm", Context.MODE_PRIVATE);
            String ExtractNIC = preferences.getString("nic_identified_lis", "000000000000");
            CorrectNIC = String.valueOf(extractednic_lis.getText());
            Log.e("ExtractNIC", String.valueOf(ExtractNIC));
            Log.e("CorrectNIC", String.valueOf(CorrectNIC));

            for (int i = 0; i < 12; i++) {
                if (ExtractNIC.charAt(i) == CorrectNIC.charAt(i)) {
                    count = count + 1;
                    Log.e("MATCHED_NUM", String.valueOf(ExtractNIC.charAt(i)));
                }
            }
            Log.e("MATCHED_COUNT", String.valueOf(count));

            if (count < 10) {
                Toast.makeText(this, "Please upload a clear lisence again", Toast.LENGTH_SHORT).show();
                proceedLISbtn.setVisibility(View.INVISIBLE);
            } else{
                if(ExtractNIC.length() == 12) {
                    niclayout.setVisibility(View.VISIBLE);
                    exp_layout.setVisibility(View.VISIBLE);
                    proceedLISbtn.setVisibility(View.VISIBLE);
                    String ExtractEXP = preferences.getString("exp_identified_lis", "00.00.0000");
                    matchdate(ExtractEXP);
                }
            }
        }catch (Exception e){
            Log.e("editnic_EXCEPTION", e.toString());
            Toast.makeText(this, "Error, Please Upload valid lisence ", Toast.LENGTH_SHORT).show();
        }
    }

    private void matchdate(String ExtractEXP){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = df.format(c);
        Log.e("formattedDate", String.valueOf(formattedDate));
        int yN = Integer.parseInt(formattedDate.substring(6, 10)) + 8;
        String DateOfExp;
        try {
//            String DateOfExp;
            int d1 = Integer.parseInt(ExtractEXP.substring(0, 1));
            int d = Integer.parseInt(ExtractEXP.substring(0, 2));
            int m1 = Integer.parseInt(ExtractEXP.substring(3, 4));
            int m2 = Integer.parseInt(ExtractEXP.substring(4, 5));
            int m = Integer.parseInt(ExtractEXP.substring(3, 5));
            int y = Integer.parseInt(ExtractEXP.substring(6, 10));
//        Log.e("d1", String.valueOf(d1));
//        Log.e("m1", String.valueOf(m1));
//        Log.e("m2", String.valueOf(m2));
//        Log.e("y12", String.valueOf(y12));

//            Date c = Calendar.getInstance().getTime();
//            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
//            String formattedDate = df.format(c);
//            Log.e("formattedDate", String.valueOf(formattedDate));
//            int yN = Integer.parseInt(formattedDate.substring(6, 10)) + 8;

            if (ExtractEXP.equals("Not Found")) {
                Toast.makeText(this, "Please Check Date", Toast.LENGTH_SHORT).show();
                DateOfExp = formattedDate;
            } else {
                if ((d > 31) || (d < 1)) {
                    Toast.makeText(this, "Please check the date", Toast.LENGTH_SHORT).show();
                    if (d1 > 3)
                        d = Integer.parseInt(2 + ExtractEXP.substring(1, 2));
                    else
                        d = Integer.parseInt(formattedDate.substring(0, 2));
                }
                if ((m > 12) || (m < 0)) {
                    Toast.makeText(this, "Please check the month", Toast.LENGTH_SHORT).show();
                    if (m1 > 1)
                        m1 = 1;
                    else if (m2 > 2)
                        m2 = 2;
                }
                if (y > yN) {
                    Toast.makeText(this, "Please check the year", Toast.LENGTH_SHORT).show();
                    y = yN;
                }

                DateOfExp = d + "." + m1 + "" + m2 + "." + y;
            }
            Log.e("DateOfExp", DateOfExp);
            extracteddate_lis.setText(DateOfExp);
            extracteddate_lis.setEnabled(true);
            extracteddate_lis.setVisibility(View.VISIBLE);
            proceedLISbtn.setVisibility(View.VISIBLE);
        }catch (Exception e){
            DateOfExp = formattedDate;
            extracteddate_lis.setText(DateOfExp);
            extracteddate_lis.setEnabled(true);
            Log.e("formattedDate Exception", String.valueOf(e));
            Toast.makeText(this, "Error !, Please Enter Correct Details", Toast.LENGTH_SHORT).show();
        }
    }
}
