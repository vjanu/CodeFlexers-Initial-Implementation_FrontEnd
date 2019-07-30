package com.example.plusgo.FC.TripHistory.Trip_Details;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.example.plusgo.FC.TripHistory.Passenger;
import com.example.plusgo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DriverHistoryDetailsFragment extends Fragment {
    BaseContent BASECONTENT = new BaseContent();
    private final String JSON_GET_DRIVER_DETAILS = BASECONTENT.IpAddress+"/trip/history/passenger/driverDetails/";
    String DriverId = "";
    View view;
    RequestQueue requestQueue ;
    private TextView txtDriverName,txtVehicleName,txtNoPlate;
    private ImageView driverImage;



    public DriverHistoryDetailsFragment() {
        Log.d("DriverId1--",DriverId);
    }

    @SuppressLint("ValidFragment")
    public DriverHistoryDetailsFragment(String driverId) {
        this.DriverId = driverId;
        Log.d("DriverId2--",DriverId);
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getContext());
        //getDriverDetails();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_driver_history_details_fragment,container,false);
        txtDriverName = (TextView) view.findViewById(R.id.txtDriverName);
        txtVehicleName = (TextView) view.findViewById(R.id.txtVehicleName);
        txtNoPlate = (TextView) view.findViewById(R.id.txtNoPlate);
        driverImage = (ImageView) view.findViewById(R.id.proImage);
        getDriverDetails();
        return view;
    }

    public void getDriverDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();

        Log.d("DriverId--",DriverId);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_GET_DRIVER_DETAILS+DriverId,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressDialog.dismiss();
                        try {
                            String driverName = response.getString("FullName");
                            String vehicleBrand = response.getString("brand");
                            String vehicleModel = response.getString("Model");
                            String noPlate = response.getString("VNumber");
                            String txtImage = response.getString("img");

                            txtDriverName.setText(driverName);
                            txtVehicleName.setText(vehicleBrand + " "+vehicleModel);
                            txtNoPlate.setText(noPlate);

                            RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.user2).error(R.drawable.user2);

                            //To load image using Glide
                            Glide.with(getContext())
                                    .load(BASECONTENT.IpAddress  +txtImage)
                                    .apply(requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .skipMemoryCache(true))
                                    .into(driverImage);
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
