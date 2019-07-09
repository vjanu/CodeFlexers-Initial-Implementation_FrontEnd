package com.example.plusgo.FC.TripHistory.Trip_Details;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.plusgo.BaseContent;
import com.example.plusgo.FC.TripHistory.Passenger;
import com.example.plusgo.FC.TripHistory.PassengerHistoryAdpter;
import com.example.plusgo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class PassengerHistoryDetailsFragment extends Fragment {

    BaseContent BASECONTENT = new BaseContent();
    private final String JSON_GET_CO_PASSENGERS = BASECONTENT.IpAddress + "/trip/history/copassengers/";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Passenger> passengersList;
    String TripId = "";
    View view;
    private String userId;

    public PassengerHistoryDetailsFragment() {
    }

    @SuppressLint("ValidFragment")
    public PassengerHistoryDetailsFragment(String tripId) {
        this.TripId = tripId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        passengersList = new ArrayList<>();

        SharedPreferences user = getActivity().getSharedPreferences("userStore",MODE_PRIVATE);
        userId = user.getString("UId", null);

        loadCoPassengers();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_passenger_history_details_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.coPassengersRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private void loadCoPassengers() {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_GET_CO_PASSENGERS + TripId + "/"+userId,
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
                                        o.getString("source"),
                                        o.getString("destination")

                                );
                                //   Log.d("for",o.getString("description"));
                                passengersList.add(item);
                                Log.d("for", item.toString());
                            }
                            adapter = new PassengerHistoryDetailsAdapter(passengersList, getContext());
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
}
