/*
 * *
 *  * Created by Viraj Wickramasinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/3/19 1:29 PM
 *
 */

package com.example.plusgo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.plusgo.BaseContent;
import com.example.plusgo.R;
import com.example.plusgo.UPM.ViewReportTripHistoryActivity;
import com.example.plusgo.Utility.ReportedDriverHistoryItem;
import com.example.plusgo.Utility.ReportedDriverListItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReportedDriverHistoryAdapter extends RecyclerView.Adapter<ReportedDriverHistoryAdapter.ViewHolder> {

    private Context context;
    private List<ReportedDriverHistoryItem> driverListItems;
    ReportedDriverHistoryItem driverListItem;
    BaseContent BASECONTENT = new BaseContent();
    public ReportedDriverHistoryAdapter(List<ReportedDriverHistoryItem> driverListItems, Context context) {
        this.driverListItems = driverListItems;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View v = LayoutInflater.from(viewGroup.getContext())
               .inflate(R.layout.reported_drivers_trips, viewGroup, false);
       return new ViewHolder(v);
    }

    //bind the values to the list
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        driverListItem = driverListItems.get(i);
        viewHolder.txtSource.setText(driverListItem.getSource());
        viewHolder.txtDestination.setText(String.valueOf(driverListItem.getDestination()));
        viewHolder.txtPrice.setText(String.valueOf(driverListItem.getPrice()));
        viewHolder.txtDate.setText(driverListItem.getDateT());
//        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.user2).error(R.drawable.user2);

//        viewHolder.checkHistory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //implement onClick
//                Intent i = new Intent(context, ViewReportTripHistoryActivity.class);
//                TextView puid  =(TextView)view.findViewById(R.id.puidHidden);
//                TextView duid  =(TextView)view.findViewById(R.id.duidHidden);
//                i.putExtra("PUID", String.valueOf(puid.getText()));
//                i.putExtra("DUID", String.valueOf(duid.getText()));
//                view.getContext().startActivity(i);
//            }
//        });

        //To load image using Glide
//        Glide.with(context)
//                .load(BASECONTENT.IpAddress  +driverListItem.getImage())
//                .apply(requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .skipMemoryCache(true))
//                .into(viewHolder.profileImage);

    }



    //get number of elements in the list
    @Override
    public int getItemCount() {
        return driverListItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtSource;
        public TextView txtDestination;
        public TextView txtPrice;
        public TextView txtDate;
//        public CircleImageView profileImage;
        public CardView trip_history;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSource = (TextView)itemView.findViewById(R.id.txtsLoc);
            txtDestination = (TextView)itemView.findViewById(R.id.txtdLoc);
            txtPrice = (TextView)itemView.findViewById(R.id.txtPrice);
            txtDate = (TextView)itemView.findViewById(R.id.txtDate);
//            profileImage = (CircleImageView)itemView.findViewById(R.id.proImage1);
            trip_history = (CardView) itemView.findViewById(R.id.trip_history);

        }
    }

}
