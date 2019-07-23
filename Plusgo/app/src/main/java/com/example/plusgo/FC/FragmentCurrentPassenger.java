package com.example.plusgo.FC;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.plusgo.BaseContent;
import com.example.plusgo.FC.Adapters.CurrentPassengerAdapter;
import com.example.plusgo.R;
import com.sohrab.obd.reader.application.ObdPreferences;
import com.sohrab.obd.reader.obdCommand.ObdConfiguration;
import com.sohrab.obd.reader.service.ObdReaderService;
import com.sohrab.obd.reader.trip.TripRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.sohrab.obd.reader.constants.DefineObdReader.ACTION_OBD_CONNECTION_STATUS;
import static com.sohrab.obd.reader.constants.DefineObdReader.ACTION_READ_OBD_REAL_TIME_DATA;

public class FragmentCurrentPassenger extends Fragment {

    BaseContent BASECONTENT = new BaseContent();
    private final String JSON_GET_CURRENT_PASSENGER = BASECONTENT.IpAddress+"/trip/";
    private RecyclerView recyclerView;
    public RecyclerView.Adapter CPassengeradapter;
    private List<Current_Passenger> currentPassenger;
    View view,view2;
    String TripId = "";
    private RequestQueue requestQueue;
    private Handler handler;
    private TextView txtMileage;

    Handler mHandler;

    public FragmentCurrentPassenger() {

        Log.d("j","1Call FragmentCurrentPassenger");
    }

    @SuppressLint("ValidFragment")
    public FragmentCurrentPassenger(String tripId) {

        this.TripId = tripId;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mHandler = new Handler();
    }


    @Override
    public void onStart() {
        super.onStart();

        try {
            currentPassenger.clear();
            mHandler.post(runnableCode);


        } catch (Exception e) {

            //ErrorMessage
        }
    }



    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {

            try {
                currentPassenger.clear();
                try {
                    loadCurrentPassengerData();
                }catch(Exception ex){

                }
                currentPassenger.clear();
                mHandler.postDelayed(runnableCode, 10000);

            } catch (Exception e) {
                //ErrorMessage
            }
        }
    };





    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentPassenger = new ArrayList<>();
        Log.d("SharedTripId" ,TripId );
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_fragment_current_passenger,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.currentPassengerRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;


    }




    public void loadCurrentPassengerData() {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();
        Log.d("TripIDD",TripId);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_GET_CURRENT_PASSENGER+TripId,
                //StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_GET_CURRENT_PASSENGER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("currentUsers");
                            Log.d("array.length()", String.valueOf(array.length()));
                            for(int i=0;i<array.length();i++){

                                JSONObject o = array.getJSONObject(i);
                                Current_Passenger items = new Current_Passenger(
                                        o.getString("FullName"),
                                        o.getString("passengerId"),
                                        o.getString("tripId"),
                                        o.getString("source"),
                                        o.getString("destination"),
                                        o.getString("trip_status"),
                                        o.getString("Token"),
                                        o.getString("img")

                                );
                                Log.d("for",o.getString("trip_status"));
                                currentPassenger.add(items);
                                Log.d("for888",items.toString());
                            }
                            CPassengeradapter = new CurrentPassengerAdapter(currentPassenger,getContext());
                            recyclerView.setAdapter(CPassengeradapter);
                            //CPassengeradapter.notifyDataSetChanged();



                        } catch (JSONException e) {
                            Log.d("kachal",e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.d("12435",error.getMessage());

                        Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        //RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getContext());

        }
        requestQueue.add(stringRequest);


    }



}
