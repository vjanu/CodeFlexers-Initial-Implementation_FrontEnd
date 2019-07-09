package com.example.plusgo.FC.TripHistory.Trip_Details;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.plusgo.FC.TripHistory.Passenger;
import com.example.plusgo.R;

import java.util.List;

public class PassengerHistoryDetailsAdapter extends RecyclerView.Adapter<PassengerHistoryDetailsAdapter.ViewHolder> {

    public PassengerHistoryDetailsAdapter(List<Passenger> passengerList, Context context) {
        this.passengerList = passengerList;
        this.context = context;
    }

    //Create List item to the store details
    private List<Passenger> passengerList;
    private Context context;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.copassenger_history_list,viewGroup,false);
        return new PassengerHistoryDetailsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PassengerHistoryDetailsAdapter.ViewHolder viewHolder, int i) {
        //Read the listItem Iteratively using int i
        //Create an object of ListItem as listitem
        final Passenger passengerItems = passengerList.get(i);
        //txtHead textview set the value from the getters in ListItem.java file
        viewHolder.uidHidden.setText(passengerItems.getTripId());
        viewHolder.txtFullName.setText(passengerItems.getPassengerName());
        viewHolder.txtStarting.setText(passengerItems.getStartingPoint());
        viewHolder.txtDestination.setText(passengerItems.getDestinationPoint());
    }

    @Override
    public int getItemCount() {
        return passengerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView uidHidden;
        public TextView txtFullName;
        public TextView txtStarting;
        public TextView txtDestination;
        public RelativeLayout displayLayout;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            uidHidden = (TextView) itemView.findViewById(R.id.uidHidden);
            txtFullName = (TextView) itemView.findViewById(R.id.txtFullName);
            txtStarting = (TextView) itemView.findViewById(R.id.txtStarting);
            txtDestination = (TextView) itemView.findViewById(R.id.txtDestination);
            displayLayout = (RelativeLayout) itemView.findViewById(R.id.displayLayout);
        }
    }
}
