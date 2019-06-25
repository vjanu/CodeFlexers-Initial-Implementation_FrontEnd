package com.example.plusgo.Notification;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.plusgo.BaseContent;
import com.example.plusgo.FC.PassengerCurrentTrip;
import com.example.plusgo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenNotification extends AppCompatActivity {
    private TextView txtFullName,txtSource,txtDestination,txtHiddenPassengerToken,txtHiddenPassengerid,txtHiddendriverid,txtHiddenTripId;
    private Button btnAccept,btnDecline;
    private BaseContent BASECONTENT = new BaseContent();
    private String JSON_URL_PUT_ACCEPT_RIDE = BASECONTENT.IpAddress + "/trip/accept/";


    public OpenNotification() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_notification);
        txtFullName = (findViewById(R.id.txtFullName));
        txtSource = (findViewById(R.id.txtSource));
        txtDestination = (findViewById(R.id.txtDestination));
        txtHiddenPassengerToken = (findViewById(R.id.txtHiddenPassengerToken));
        txtHiddenPassengerid = (findViewById(R.id.txtHiddenPassengerid));
        txtHiddendriverid = (findViewById(R.id.txtHiddendriverid));
        txtHiddenTripId = (findViewById(R.id.txtHiddenTripId));
        btnAccept = (findViewById(R.id.btnAccept));
        btnDecline = (findViewById(R.id.btnDecline));

        String notificationBody = MyFirebaseMessagingService.NotificationBodyCatcher;
        Log.d("Check" , notificationBody);
//        textView.setText(notificationBody);

        //Set Variables to Text Views
        String Full_Name = notificationBody.split("\n")[1];
        txtFullName.setText(Full_Name);

        String Source = notificationBody.split("\n")[3];
        txtSource.setText(Source);

        String Destination = notificationBody.split("\n")[4];
        txtDestination.setText(Destination);

        String passengerToken = notificationBody.split("\n")[5];
        txtHiddenPassengerToken.setText(passengerToken);

        String reqPassengerId = notificationBody.split("\n")[6];
        txtHiddenPassengerid.setText(reqPassengerId);

        String driverId = notificationBody.split("\n")[7];
        txtHiddendriverid.setText(reqPassengerId);

        String tripId = notificationBody.split("\n")[8];
        txtHiddenTripId.setText(reqPassengerId);




        findViewById(R.id.btnAccept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDriverAccept();
                AcceptRideNotification();
            }
        });
    }




    //Accept Message
    private void AcceptRideNotification(){
        String title = "Ride Confirmation";
        String body = "Driver will arriving soon";
        String passengerToken = "eQ1tTDeDAk8:APA91bHDVmf-2pd5qlEUxwFoR_ENMeTYEBENoyG7infabGxNXo51Mqkg1UkbWddVih29qS44UpIed8_qrKSUwijvDVpj3iBogsrOsOImMAjT60ikts4Fu0K3Ez-RVuycWkEoJJo6Uq3m";


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://plusgo-ce90f.firebaseapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> call = api.AcceptNotification(passengerToken,title,body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Toast.makeText(OpenNotification.this,response.body().string(),Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void updateDriverAccept() {
        try {


            String tripId = txtHiddenTripId.getText().toString();
            String passengerId = txtHiddenPassengerid.getText().toString();
            // price = Double.parseDouble(txtHiddenPrice.getText().toString());
            String driverId =  txtHiddendriverid.getText().toString();


            Log.d("@@@@tripId",tripId);
            Log.d("@@@passengerId",passengerId);
            Log.d("@@@driverId",driverId);

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("trip_status", 0);
//            jsonObject.put("startMileage", txtHiddenCurrentMileage.getText().toString());
            //jsonObject.put("price", price);
            final String mRequestBody = jsonObject.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.PUT, JSON_URL_PUT_ACCEPT_RIDE+tripId+"/"+passengerId+"/"+driverId, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_VOLLEY", response);
                    Toast.makeText(OpenNotification.this, "Accept the Ride", Toast.LENGTH_SHORT).show();
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_VOLLEY", error.toString());
                    Toast.makeText(OpenNotification.this, "Error Occurred", Toast.LENGTH_SHORT).show();
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
                protected com.android.volley.Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                    }
                    return com.android.volley.Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);


        }catch(JSONException e){
            e.printStackTrace();
        }

    }

}
