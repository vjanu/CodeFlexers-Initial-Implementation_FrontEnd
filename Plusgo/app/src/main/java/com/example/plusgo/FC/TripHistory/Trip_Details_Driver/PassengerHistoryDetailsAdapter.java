package com.example.plusgo.FC.TripHistory.Trip_Details_Driver;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.plusgo.BaseContent;
import com.example.plusgo.FC.TripHistory.Passenger;
import com.example.plusgo.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PassengerHistoryDetailsAdapter extends RecyclerView.Adapter<PassengerHistoryDetailsAdapter.ViewHolder> {


    private List<Passenger> passengerList;
    private Context context;
    BaseContent BASECONTENT = new BaseContent();

    public PassengerHistoryDetailsAdapter(List<Passenger> passengerList, Context context) {
        this.passengerList = passengerList;
        this.context = context;
    }


    @NonNull
    @Override
    public PassengerHistoryDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PassengerHistoryDetailsAdapter.ViewHolder viewHolder, int i) {
        final Passenger passengerItems = passengerList.get(i);
        //txtHead textview set the value from the getters in ListItem.java file
        viewHolder.uidHidden.setText(passengerItems.getTripId());
        viewHolder.txtFullName.setText(passengerItems.getPassengerName());
        viewHolder.txtStarting.setText(passengerItems.getStartingPoint());
        viewHolder.txtDestination.setText(passengerItems.getDestinationPoint());

        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.user2).error(R.drawable.user2);

        //To load image using Glide
        Glide.with(context)
                .load(BASECONTENT.IpAddress  +passengerItems.getImage())
                .apply(requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .into(viewHolder.profileImage);

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

        public CircleImageView profileImage;
        public RelativeLayout displayLayout;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            uidHidden = (TextView) itemView.findViewById(R.id.uidHidden);
            txtFullName = (TextView) itemView.findViewById(R.id.txtFullName);
            txtStarting = (TextView) itemView.findViewById(R.id.txtStarting);
            txtDestination = (TextView) itemView.findViewById(R.id.txtDestination);

            profileImage = (CircleImageView)itemView.findViewById(R.id.proImage);
            displayLayout = (RelativeLayout) itemView.findViewById(R.id.displayLayout);
        }
    }
}
