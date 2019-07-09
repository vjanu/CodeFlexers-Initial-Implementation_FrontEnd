package com.example.plusgo.FC.TripHistory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.plusgo.R;

import java.util.ArrayList;
import java.util.List;

public class DriverFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Driver> driverList;

    View view;

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
        driverList = new ArrayList<>();
        //loadRecycleViewData();
    }

//    private void loadRecycleViewData() {
//        Log.d("sdwssss","Call sssMethod");
//        final ProgressDialog progressDialog = new ProgressDialog(getContext());
//        progressDialog.setMessage("Loading Data...");
//        progressDialog.show();
//        Log.d("wswww","wswww");
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        Log.d("sdss",response);
//                        progressDialog.dismiss();
//                        try {
//                            Log.d("222","2222");
//
//                            JSONObject jsonObject = new JSONObject(response);
//                            Log.d("333","333");
//
//                            JSONArray array =jsonObject.getJSONArray("passenger");
////
//                            Log.d("444",array.toString());
//
//
//                            for(int i=0;i<array.length();i++){
//                                Log.d("444","bxxxx");
//                                JSONObject o = array.getJSONObject(i);
//                                Driver item = new Driver(
//                                        o.getString("heading"),
//                                        o.getString("description"),
//                                        Double.parseDouble(o.getString("destination"))
//
//                                );
//                                Log.d("for",o.getString("description"));
//                                driverList.add(item);
//                                Log.d("for",item.toString());
//                            }
//                            adapter = new DriverHistoryAdapter(driverList,getContext());
//                            recyclerView.setAdapter(adapter);
//
//                        } catch (JSONException e) {
//                            Log.d("expe",e.toString());
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("12435",error.getMessage());
//
//                        Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
//                    }
//                });
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        requestQueue.add(stringRequest);
//
//
//    }
}
