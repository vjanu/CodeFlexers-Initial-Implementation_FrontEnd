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

import com.example.plusgo.FC.TripHistory.Trip_Details_Driver.TripDetailsDriver;
import com.example.plusgo.R;

import java.util.List;

public class DriverHistoryAdapter  extends RecyclerView.Adapter<DriverHistoryAdapter.ViewHolder> {

    //Create List item to the store details
    private List<Driver> driverList;
    private Context context;

    public DriverHistoryAdapter(List<Driver> driverList, Context context) {
        this.driverList = driverList;
        this.context = context;
    }


    @NonNull
    @Override
    public DriverHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.driver_list_history,viewGroup,false);
        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull DriverHistoryAdapter.ViewHolder viewHolder, int i) {
        //Read the listItem Iteratively using int i
        //Create an object of ListItem as listitem
        final Driver driverItems = driverList.get(i);
        //txtHead textview set the value from the getters in ListItem.java file
        viewHolder.txtTripId.setText(driverItems.getTripId());
        viewHolder.txtDate.setText(driverItems.getDate());
        viewHolder.txtEarn.setText(String.format("Rs.%.2f",driverItems.getEarn())+ " Earn from this Ride");
        viewHolder.txtTime.setText(driverItems.getTime());
        viewHolder.txtStartPoint.setText(driverItems.getSource());
        viewHolder.txtEndPoint.setText(driverItems.getDestination());

        viewHolder.displayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //implement onClick
                Intent i = new Intent(context, TripDetailsDriver.class);
                TextView tid  =(TextView)view.findViewById(R.id.txtTripId);
                TextView txtDate  =(TextView)view.findViewById(R.id.txtDate);
                TextView txtEarn  =(TextView)view.findViewById(R.id.txtEarn);
                TextView txtStartPoint  =(TextView)view.findViewById(R.id.txtStartPoint);
                TextView txtDestination  =(TextView)view.findViewById(R.id.txtEndPoint);
//                TextView txtsourceLatLong  =(TextView)view.findViewById(R.id.txtsourceLatLong);
//                TextView txtdestinationLatLong  =(TextView)view.findViewById(R.id.txtdestinationLatLong);



                i.putExtra("TID", String.valueOf(tid.getText()));
                i.putExtra("DATE", txtDate.getText().toString());
                i.putExtra("EARN", txtEarn.getText().toString());
                i.putExtra("START", txtStartPoint.getText().toString());
                i.putExtra("END", txtDestination.getText().toString());
                //i.putExtra("txtsourceLatLong", txtsourceLatLong.getText().toString());

                view.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return driverList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtTripId;
        public TextView txtDate;
        public TextView txtEarn;
        public TextView txtTime;
        public TextView txtStartPoint;
        public TextView txtEndPoint;
        public RelativeLayout displayLayout;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            txtTripId = (TextView) itemView.findViewById(R.id.txtTripId);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtEarn = (TextView) itemView.findViewById(R.id.txtEarn);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            txtStartPoint = (TextView) itemView.findViewById(R.id.txtStartPoint);
            txtEndPoint = (TextView) itemView.findViewById(R.id.txtEndPoint);
            displayLayout = (RelativeLayout) itemView.findViewById(R.id.displayLayout);
        }
    }
}
