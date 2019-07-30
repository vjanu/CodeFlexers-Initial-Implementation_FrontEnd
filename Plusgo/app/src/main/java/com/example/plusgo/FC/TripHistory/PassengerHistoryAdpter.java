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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.plusgo.BaseContent;
import com.example.plusgo.FC.TripHistory.Trip_Details.TripDetails;
import com.example.plusgo.FC.TripSummaryActivity;
import com.example.plusgo.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PassengerHistoryAdpter extends RecyclerView.Adapter<PassengerHistoryAdpter.ViewHolder> {
    //Create List item to the store details
    private List<Passenger> passengerList;
    private Context context;
    BaseContent BASECONTENT = new BaseContent();

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
        viewHolder.txtsourceLatLong.setText(passengerItems.getSourceLatLong());
        viewHolder.txtdestinationLatLong.setText(passengerItems.getDestinationLatLong());
        viewHolder.txtDriverId.setText(passengerItems.getDriverId());
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.user2).error(R.drawable.user2);

        //To load image using Glide
        Glide.with(context)
                .load(BASECONTENT.IpAddress  +passengerItems.getImage())
                .apply(requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .into(viewHolder.profileImage);

        viewHolder.displayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //implement onClick
                Intent i = new Intent(context, TripDetails.class);

                //tripid, userid, fullname, source detination
                TextView tid  =(TextView)view.findViewById(R.id.txtTripId);
                TextView txtDate  =(TextView)view.findViewById(R.id.txtDate);
                TextView txtFare  =(TextView)view.findViewById(R.id.txtFare);
                TextView txtStartPoint  =(TextView)view.findViewById(R.id.txtStartPoint);
                TextView txtEndPoint  =(TextView)view.findViewById(R.id.txtEndPoint);
                TextView txtsourceLatLong  =(TextView)view.findViewById(R.id.txtsourceLatLong);
                TextView txtdestinationLatLong  =(TextView)view.findViewById(R.id.txtdestinationLatLong);
                TextView txtDriverId  =(TextView)view.findViewById(R.id.txtDriverId);



                i.putExtra("TID", String.valueOf(tid.getText()));
                i.putExtra("DATE", txtDate.getText().toString());
                i.putExtra("FARE", txtFare.getText().toString());
                i.putExtra("START", txtStartPoint.getText().toString());
                i.putExtra("END", txtEndPoint.getText().toString());
                i.putExtra("txtsourceLatLong", txtsourceLatLong.getText().toString());
                i.putExtra("txtdestinationLatLong", txtdestinationLatLong.getText().toString());
                i.putExtra("txtDriverId", txtDriverId.getText().toString());
                //i.putExtra("DATE", txtF.getText().toString());
                view.getContext().startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return passengerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtTripId,txtDate,txtFare,txtStartPoint,txtEndPoint,txtDriverName,txtDriverId;
        public TextView txtsourceLatLong;
        public TextView txtdestinationLatLong;
        public CircleImageView profileImage;
        public RelativeLayout displayLayout;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            txtTripId = (TextView) itemView.findViewById(R.id.txtTripId);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtFare = (TextView) itemView.findViewById(R.id.txtFare);
            txtStartPoint = (TextView) itemView.findViewById(R.id.txtStartPoint);
            txtEndPoint = (TextView) itemView.findViewById(R.id.txtEndPoint);
            txtDriverName = (TextView) itemView.findViewById(R.id.txtDriverName);
            txtDriverId = (TextView) itemView.findViewById(R.id.txtDriverId);
            txtsourceLatLong = (TextView) itemView.findViewById(R.id.txtsourceLatLong);
            txtdestinationLatLong = (TextView) itemView.findViewById(R.id.txtdestinationLatLong);
            profileImage = (CircleImageView)itemView.findViewById(R.id.proImage);
            displayLayout = (RelativeLayout) itemView.findViewById(R.id.displayLayout);
        }
    }
}
