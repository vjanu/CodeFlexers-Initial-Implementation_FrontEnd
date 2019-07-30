package com.example.plusgo.FC.TripHistory.Trip_Details_Driver;

import android.content.Context;
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
import com.example.plusgo.FC.TripHistory.Passenger;
import com.example.plusgo.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DriverReceiptAdapter extends RecyclerView.Adapter<DriverReceiptAdapter.ViewHolder> {
    private List<Passenger> passengerList;
    private Context context;
    BaseContent BASECONTENT = new BaseContent();

    public DriverReceiptAdapter(List<Passenger> passengerList, Context context) {
        this.passengerList = passengerList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.receipt_all_passenger,viewGroup,false);
        return new DriverReceiptAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverReceiptAdapter.ViewHolder viewHolder, int i) {
        final Passenger passengerItems = passengerList.get(i);
        //txtHead textview set the value from the getters in ListItem.java file
        viewHolder.uidHidden.setText(passengerItems.getTripId());
        viewHolder.txtFullName.setText(passengerItems.getPassengerName());
        //viewHolder.txtPrice.setText(passengerItems.getFare().toString());
        viewHolder.txtPrice.setText(String.format("Rs.%.2f",passengerItems.getFare()));


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
        public TextView txtPrice;

        public CircleImageView profileImage;
        public RelativeLayout displayLayout;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            uidHidden = (TextView) itemView.findViewById(R.id.uidHidden);
            txtFullName = (TextView) itemView.findViewById(R.id.txtFullName);
            txtPrice = (TextView) itemView.findViewById(R.id.txtPrice);
            profileImage = (CircleImageView)itemView.findViewById(R.id.proImage);
            displayLayout = (RelativeLayout) itemView.findViewById(R.id.displayLayout);
        }
    }

}
