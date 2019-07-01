package com.example.plusgo.FC;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.plusgo.BaseContent;
import com.example.plusgo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PassengerFragment extends Fragment {

    BaseContent BASECONTENT = new BaseContent();
    private final String JSON_GET_CURRENT_PASSENGER = BASECONTENT.IpAddress+"/trip/history/passenger/";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Passenger> passengersList;
    View view;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_driver_fragment,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.driverRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this = your fragment
        //TODO::check shared pefences
        SharedPreferences user = this.getActivity().getSharedPreferences("userStore", Context.MODE_PRIVATE);
        userId = user.getString("UId", null);
        passengersList = new ArrayList<>();
        loadPassengerHistory();
    }

    private void loadPassengerHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();
        //TODO: get user Id from the user store
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_GET_CURRENT_PASSENGER+userId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("response",response);
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array =jsonObject.getJSONArray("passenger");

                            for(int i=0;i<array.length();i++){
                                JSONObject o = array.getJSONObject(i);
                                Passenger item = new Passenger(
                                        o.getString("tripId"),
                                        o.getString("dateTime"),
                                        Double.parseDouble(o.getString("fare"))

                                );
                                //Log.d("for",o.getString("description"));
                                passengersList.add(item);
                                Log.d("for",item.toString());
                            }
                            adapter = new PassengerHistoryAdpter(passengersList,getContext());
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            Log.d("expe",e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("12435",error.getMessage());

                        Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }

}
