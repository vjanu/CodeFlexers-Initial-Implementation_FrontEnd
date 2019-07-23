package com.example.plusgo.FC.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.plusgo.BaseContent;
import com.example.plusgo.FC.Current_Passenger;
import com.example.plusgo.FC.PassengerCurrentTrip;
import com.example.plusgo.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CurrentPassengerAdapter extends  RecyclerView.Adapter<CurrentPassengerAdapter.ViewHolder> {
    BaseContent BASECONTENT = new BaseContent();
    public CurrentPassengerAdapter(List<Current_Passenger> currentPassengerLists, Context context) {
        this.currentPassengerLists = currentPassengerLists;
        this.context = context;
    }

    //Create List item to the store details
    private List<Current_Passenger> currentPassengerLists;
    private Context context;

//    public void update(ArrayList<Current_Passenger> datas){
//        currentPassengerLists.clear();
//        currentPassengerLists.addAll(datas);
//        notifyDataSetChanged();
//    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.currentpassengerlist,viewGroup,false);
        return new CurrentPassengerAdapter.ViewHolder(v);



    }

    @Override
    public void onBindViewHolder(@NonNull CurrentPassengerAdapter.ViewHolder viewHolder, int i) {
         /*
        Read the listItem Iteratively using int i
        Create an object of ListItem as listitem
        */

        final Current_Passenger current_passenger = currentPassengerLists.get(i);
        /*txtHead textview set the value from the getters in ListItem.java file*/
        viewHolder.txtName.setText(current_passenger.getName());
        viewHolder.txtSource.setText(current_passenger.getSource());
        viewHolder.txtDestination.setText(current_passenger.getDestination());
        viewHolder.txtTripStatus.setText(current_passenger.getStatus());
        viewHolder.txtHiddenPassengerId.setText(current_passenger.getStatus());
        viewHolder.txtHiddenPassengerId.setText(current_passenger.getUserId());
        viewHolder.txtHiddenTripId.setText(current_passenger.getTripId());
        viewHolder.txtHiddenToken.setText(current_passenger.getToken());
        //viewHolder.txtHiddenuserImage.setText(current_passenger.getUserImage());
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.user2).error(R.drawable.user2);

        //To load image using Glide
        Glide.with(context)
                .load(BASECONTENT.IpAddress  +current_passenger.getUserImage())
                .apply(requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .into(viewHolder.profileImage);


        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, PassengerCurrentTrip.class);



                //tripid, passengerId, name, source detination , txtToken , status
                TextView tripId  =(TextView)view.findViewById(R.id.txtHiddenTripId);
                TextView passengerId  =(TextView)view.findViewById(R.id.txtHiddenPassengerId);
                TextView name  =(TextView)view.findViewById(R.id.txtName);
                TextView source  =(TextView)view.findViewById(R.id.txtSource);
                TextView destination  =(TextView)view.findViewById(R.id.txtDestination);
                CircleImageView profileImage = (CircleImageView)view.findViewById(R.id.proImage);
                TextView txtToken = (TextView) view.findViewById(R.id.txtHiddenToken);
                TextView txtStatus = (TextView) view.findViewById(R.id.txtTripStatus);
                //TextView txtHiddenuserImage = (TextView) view.findViewById(R.id.txtHiddenuserImage);
//                TextView txtMileage = (TextView) view.findViewById(R.id.txtMileage);

                //TextView txtMileage = (TextView) v2.findViewById( R.id.txtMileage );
               // Log.d("OBD",txtMileage.getText().toString());

//                TextView txtMileage = (TextView) activity_map_current_passenger


                i.putExtra("TripId", String.valueOf(tripId.getText()));
                i.putExtra("PassengerId", String.valueOf(passengerId.getText()));
                i.putExtra("Name", String.valueOf(name.getText()));
                i.putExtra("Source", String.valueOf(source.getText()));
                i.putExtra("Destination",String.valueOf(destination.getText()));
                i.putExtra("img",String.valueOf(current_passenger.getUserImage()));
                i.putExtra("Token",String.valueOf(txtToken.getText()));
                i.putExtra("Status",String.valueOf(txtStatus.getText()));
               // i.putExtra("userImage",String.valueOf(txtHiddenuserImage.getText()));
//                i.putExtra("currentMileage",String.valueOf(txtMileage.getText()+"12"));


                //Test
//                PassengerCurrentTrip passengerCurrentTrip = new PassengerCurrentTrip();
//                passengerCurrentTrip.btnStartTrip.setVisibility(View.INVISIBLE);


                view.getContext().startActivity(i);

            }
        });

        //To load image using Glide
        Glide.with(context)
                .load(BASECONTENT.IpAddress  +current_passenger.getUserImage())
                .apply(requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .into(viewHolder.profileImage);
    }




    @Override
    public int getItemCount() {
        return currentPassengerLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtName;
        public TextView txtSource;
        public TextView txtDestination;
        public TextView txtTripStatus;
        public TextView txtHiddenPassengerId;
        public TextView txtHiddenTripId;
        public TextView txtHiddenToken;
        public CircleImageView profileImage;
        public RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtSource = (TextView) itemView.findViewById(R.id.txtSource);
            txtDestination = (TextView) itemView.findViewById(R.id.txtDestination);
            txtTripStatus = (TextView) itemView.findViewById(R.id.txtTripStatus);
            txtHiddenPassengerId = (TextView) itemView.findViewById(R.id.txtHiddenPassengerId);
            txtHiddenTripId = (TextView) itemView.findViewById(R.id.txtHiddenTripId);
            txtHiddenToken = (TextView) itemView.findViewById(R.id.txtHiddenToken);
            profileImage = (CircleImageView)itemView.findViewById(R.id.proImage);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);

        }


    }



}
