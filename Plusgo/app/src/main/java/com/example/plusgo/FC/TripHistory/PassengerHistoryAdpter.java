package com.example.plusgo.FC.TripHistory;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.plusgo.FC.TripHistory.Trip_Details.TripDetails;
import com.example.plusgo.FC.TripSummaryActivity;
import com.example.plusgo.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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
        viewHolder.txtTripId.setText(passengerItems.getTripId() );
        viewHolder.txtDate.setText(passengerItems.getDateTime());
        viewHolder.txtFare.setText(String.format("Rs.%.2f",passengerItems.getFare()));
        viewHolder.txtDriverName.setText(passengerItems.getDriverName());
        viewHolder.txtStartPoint.setText(passengerItems.getStartingPoint());
        viewHolder.txtEndPoint.setText(passengerItems.getDestinationPoint());

        viewHolder.displayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //implement onClick
                Intent i = new Intent(context, TripDetails.class);

                //tripid, userid, fullname, source detination
                TextView tid  =(TextView)view.findViewById(R.id.txtTripId);
                TextView txtDate  =(TextView)view.findViewById(R.id.txtDate);



                i.putExtra("TID", String.valueOf(tid.getText()));
                i.putExtra("DATE", txtDate.getText().toString());
                view.getContext().startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return passengerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtTripId,txtDate,txtFare,txtStartPoint,txtEndPoint,txtDriverName;
        public RelativeLayout displayLayout;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            txtTripId = (TextView) itemView.findViewById(R.id.txtTripId);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtFare = (TextView) itemView.findViewById(R.id.txtFare);
            txtStartPoint = (TextView) itemView.findViewById(R.id.txtStartPoint);
            txtEndPoint = (TextView) itemView.findViewById(R.id.txtEndPoint);
            txtDriverName = (TextView) itemView.findViewById(R.id.txtDriverName);
            displayLayout = (RelativeLayout) itemView.findViewById(R.id.displayLayout);
        }
    }
}
