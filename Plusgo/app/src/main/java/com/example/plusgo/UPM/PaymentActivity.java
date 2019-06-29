package com.example.plusgo.UPM;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.plusgo.BaseContent;
import com.example.plusgo.OPR.MainActivity;
import com.example.plusgo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class PaymentActivity extends AppCompatActivity {

    private EditText payD, exp, cvc;
    private Button confirm;
    private String id;
    private BaseContent BASECONTENT = new BaseContent();
    private String JSON_URL_ADD_CARD = BASECONTENT.IpAddress + "/pay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        payD = (EditText)findViewById(R.id.payD);
        exp = (EditText)findViewById(R.id.exp);
        cvc = (EditText)findViewById(R.id.cvc);
        confirm = (Button)findViewById(R.id.btnConfirmP);

        SharedPreferences user = getSharedPreferences("userStore",MODE_PRIVATE);
        id = user.getString("UId", null);



        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCreditCard();
            }
        });

    }

    //add relevant user details
    public void addCreditCard() {
        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("UserID", id);
            jsonObject.put("CardNo", payD.getText());
            jsonObject.put("ExDate", exp.getText());
            jsonObject.put("CVC", cvc.getText());
            final String mRequestBody = jsonObject.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_ADD_CARD, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(PaymentActivity.this, "Credit Card Added", Toast.LENGTH_SHORT).show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_VOLLEY", error.toString());
                    Toast.makeText(PaymentActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
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
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onBackPressed() {
        finish();
        Intent map = new Intent(PaymentActivity.this, MainActivity.class);
        startActivity(map);
    }
}
