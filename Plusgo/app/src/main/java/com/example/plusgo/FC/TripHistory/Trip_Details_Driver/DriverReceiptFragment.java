package com.example.plusgo.FC.TripHistory.Trip_Details_Driver;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.plusgo.BaseContent;
import com.example.plusgo.FC.TripHistory.Passenger;
import com.example.plusgo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class DriverReceiptFragment extends Fragment {
    BaseContent BASECONTENT = new BaseContent();
    private final String JSON_GET_PASSENGERS = BASECONTENT.IpAddress + "/trip/history/copassengers/";
    private final String JSON_GET_HISTORY_DRIVER_RECEIPT = BASECONTENT.IpAddress+"/trip/history/driver/receipt/";
    View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Passenger> passengersList;
    private String userId;
    String TripId = "";
    RequestQueue requestQueue ;
    TextView txtAmountvalue;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        passengersList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getContext());

        SharedPreferences user = getActivity().getSharedPreferences("userStore",MODE_PRIVATE);
        userId = user.getString("UId", null);

        loadAllPassengers();
    }

    public DriverReceiptFragment() {
    }

    @SuppressLint("ValidFragment")
    public DriverReceiptFragment(String tripId) {
        TripId = tripId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_driver_receipt_fragment,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.passengerRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        txtAmountvalue = (TextView) view.findViewById(R.id.txtAmountvalue);
        getTotalAmount();
        return view;
    }

    private void loadAllPassengers() {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_GET_PASSENGERS + TripId + "/"+userId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("sdss", response);
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("coPassengers");

                            for (int i = 0; i < array.length(); i++) {
                                Log.d("444", "bxxxx");
                                JSONObject o = array.getJSONObject(i);
                                Passenger item = new Passenger(
                                        o.getString("tripId"),
                                        o.getString("FullName"),
                                        o.getString("img"),
                                        Double.parseDouble(o.getString("price"))

                                );
                                //   Log.d("for",o.getString("description"));
                                passengersList.add(item);
                                Log.d("for", item.toString());
                            }
                            adapter = new DriverReceiptAdapter(passengersList, getContext());
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            Log.d("expe", e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                       Log.d("12435",error.getMessage());

                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    public void getTotalAmount() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();

        //Log.d("DriverId--",DriverId);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_GET_HISTORY_DRIVER_RECEIPT+userId+"/"+TripId,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressDialog.dismiss();
                        try {
                            String Price = response.getString("Earn");

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
