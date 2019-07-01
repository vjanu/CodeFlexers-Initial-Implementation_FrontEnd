package com.example.plusgo.FC;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.plusgo.R;

import java.util.List;

public class PassengerHistoryAdpter extends RecyclerView.Adapter<PassengerHistoryAdpter.ViewHolder> {
    //Create List item to the store details
    private List<Passenger> passengerList;
    private Context context;

    public PassengerHistoryAdpter(List<Passenger> passengerList, Context context) {
        this.passengerList = passengerList;
        this.context = context;
    }



    @NonNull
    @Override
    public PassengerHistoryAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.passenger_list,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PassengerHistoryAdpter.ViewHolder viewHolder, int i) {
        //Read the listItem Iteratively using int i
        //Create an object of ListItem as listitem
        final Passenger passengerItems = passengerList.get(i);
        //txtHead textview set the value from the getters in ListItem.java file
        viewHolder.txtTripId.setText(passengerItems.getTripId());
        viewHolder.txtDate.setText(passengerItems.getDateTime());
        viewHolder.txtFare.setText(passengerItems.getFare().toString());
    }

    @Override
    public int getItemCount() {
        return passengerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtTripId;
        public TextView txtDate;
        public TextView txtFare;
        public RelativeLayout displayLayout;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            txtTripId = (TextView) itemView.findViewById(R.id.txtTripId);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtFare = (TextView) itemView.findViewById(R.id.txtFare);
            displayLayout = (RelativeLayout) itemView.findViewById(R.id.displayLayout);
        }
    }
}
