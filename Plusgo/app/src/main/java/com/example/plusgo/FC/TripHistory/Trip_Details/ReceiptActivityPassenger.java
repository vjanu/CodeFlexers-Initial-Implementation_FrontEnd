package com.example.plusgo.FC.TripHistory.Trip_Details;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.plusgo.BaseContent;
import com.example.plusgo.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ReceiptActivityPassenger extends Fragment {
    View view;
    RequestQueue requestQueue ;
    String PassengerId = "";
    String TripId = "";
    private TextView txtAmountvalue;
    BaseContent BASECONTENT = new BaseContent();
    private final String JSON_GET_TOTAL_AMOUNT = BASECONTENT.IpAddress+"/trip/history/passenger/receipt/";

    public ReceiptActivityPassenger() {
    }

    @SuppressLint("ValidFragment")
    public ReceiptActivityPassenger(String passengerId, String tripId) {
        PassengerId = passengerId;
        TripId = tripId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_receipt_passenger, container, false);
        txtAmountvalue = (TextView) view.findViewById(R.id.txtAmountvalue);
        getTotalAmount();
        return view;
    }

    public void getTotalAmount() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();

        //Log.d("DriverId--",DriverId);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_GET_TOTAL_AMOUNT+PassengerId+"/"+TripId,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressDialog.dismiss();
                        try {
                            String Price = response.getString("price");

                            //txtAmountvalue.setText(String.format("Rs.%.2f",Price));
                            txtAmountvalue.setText("Rs."+Price);
//                            txtVehicleName.setText(vehicleBrand + " "+vehicleModel);
//                            txtNoPlate.setText(noPlate);

                        } catch (JSONException e) {
                            Log.d("expe2",e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }
}
