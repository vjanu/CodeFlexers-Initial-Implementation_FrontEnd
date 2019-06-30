package com.example.plusgo.FC.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.plusgo.FC.Current_Passenger;
import com.example.plusgo.FC.PassengerCurrentTrip;
import com.example.plusgo.R;

import java.util.ArrayList;
import java.util.List;

public class CurrentPassengerAdapter extends  RecyclerView.Adapter<CurrentPassengerAdapter.ViewHolder> {

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
        viewHolder.txtHiddenuserImage.setText(current_passenger.getUserImage());


        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, PassengerCurrentTrip.class);



                //tripid, passengerId, name, source detination , txtToken , status
                TextView tripId  =(TextView)view.findViewById(R.id.txtHiddenTripId);
                TextView passengerId  =(TextView)view.findViewById(R.id.txtHiddenPassengerId);
                TextView name  =(TextView)view.findViewById(R.id.txtName);
                TextView source  =(TextView)view.findViewById(R.id.txtSource);
                TextView destination  =(TextView)view.findViewById(R.id.txtDestination);
                //CircleImageView profileImage = (CircleImageView)view.findViewById(R.id.proImage);
                TextView txtToken = (TextView) view.findViewById(R.id.txtHiddenToken);
                TextView txtStatus = (TextView) view.findViewById(R.id.txtTripStatus);
                TextView txtHiddenuserImage = (TextView) view.findViewById(R.id.txtHiddenuserImage);

                i.putExtra("TripId", String.valueOf(tripId.getText()));
                i.putExtra("PassengerId", String.valueOf(passengerId.getText()));
                i.putExtra("Name", String.valueOf(name.getText()));
                i.putExtra("Source", String.valueOf(source.getText()));
                i.putExtra("Destination",String.valueOf(destination.getText()));
                //i.putExtra("img",String.valueOf(profileImage));
                i.putExtra("Token",String.valueOf(txtToken.getText()));
                i.putExtra("Status",String.valueOf(txtStatus.getText()));
                i.putExtra("userImage",String.valueOf(txtHiddenuserImage.getText()));

                //Test
//                PassengerCurrentTrip passengerCurrentTrip = new PassengerCurrentTrip();
//                passengerCurrentTrip.btnStartTrip.setVisibility(View.INVISIBLE);


                view.getContext().startActivity(i);

            }
        });
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
        public TextView txtHiddenuserImage;
        public LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtSource = (TextView) itemView.findViewById(R.id.txtSource);
            txtDestination = (TextView) itemView.findViewById(R.id.txtDestination);
            txtTripStatus = (TextView) itemView.findViewById(R.id.txtTripStatus);
            txtHiddenPassengerId = (TextView) itemView.findViewById(R.id.txtHiddenPassengerId);
            txtHiddenTripId = (TextView) itemView.findViewById(R.id.txtHiddenTripId);
            txtHiddenToken = (TextView) itemView.findViewById(R.id.txtHiddenToken);
            txtHiddenuserImage = (TextView) itemView.findViewById(R.id.txtHiddenuserImage);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);

        }


    }



}
