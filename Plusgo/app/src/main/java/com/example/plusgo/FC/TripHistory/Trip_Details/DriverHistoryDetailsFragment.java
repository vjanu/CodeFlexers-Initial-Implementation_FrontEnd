package com.example.plusgo.FC.TripHistory.Trip_Details;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.plusgo.BaseContent;
import com.example.plusgo.FC.TripHistory.Passenger;
import com.example.plusgo.R;

import java.util.List;

public class DriverHistoryDetailsFragment extends Fragment {
    BaseContent BASECONTENT = new BaseContent();
   // private final String JSON_GET_CURRENT_PASSENGER = BASECONTENT.IpAddress+"/trip/history/passenger/";
  //  private List<Passenger> passengersList;
    View view;
   // private String userId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_driver_history_details_fragment,container,false);

        return view;
    }
}
