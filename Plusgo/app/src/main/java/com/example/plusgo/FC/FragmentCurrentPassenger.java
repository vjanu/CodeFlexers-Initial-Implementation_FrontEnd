package com.example.plusgo.FC;

import android.app.ProgressDialog;
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
import com.example.plusgo.FC.Adapters.CurrentPassengerAdapter;
import com.example.plusgo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentCurrentPassenger extends Fragment {

    BaseContent BASECONTENT = new BaseContent();
    private final String JSON_GET_CURRENT_PASSENGER = BASECONTENT.FCBASEIPROUTE+"/trip/";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter CPassengeradapter;
    private List<Current_Passenger> currentPassenger;
    View view;

    public FragmentCurrentPassenger() {
        Log.d("j","1Call FragmentCurrentPassenger");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentPassenger = new ArrayList<>();
        loadCurrentPassengerData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_fragment_current_passenger,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.currentPassengerRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;


    }

    private void loadCurrentPassengerData() {
        Log.d("sdwssss","Call sssMethod");
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();
        Log.d("wswww","wswww");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_GET_CURRENT_PASSENGER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("sdss",response);
                        progressDialog.dismiss();
                        try {
                            Log.d("222","2222");

                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("333","333");

                            JSONArray array =jsonObject.getJSONArray("currentUsers");
//
                            Log.d("444",array.toString());


                            for(int i=0;i<array.length();i++){
                                Log.d("444","bxxxx");
                                JSONObject o = array.getJSONObject(i);
                                Current_Passenger items = new Current_Passenger(
                                        o.getString("FullName"),
                                        o.getString("passengerId"),
                                        o.getString("tripId"),
                                        o.getString("source"),
                                        o.getString("destination"),
                                        o.getString("description"),
                                        o.getString("Token"),
                                        o.getString("img")

                                );
                                Log.d("for",o.getString("description"));
                                currentPassenger.add(items);
                                Log.d("for888",items.toString());
                            }
                            CPassengeradapter = new CurrentPassengerAdapter(currentPassenger,getContext());
                            recyclerView.setAdapter(CPassengeradapter);

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
