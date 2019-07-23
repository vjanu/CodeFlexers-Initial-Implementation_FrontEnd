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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.plusgo.BaseContent;
import com.example.plusgo.FC.TripSummaryActivity;
import com.example.plusgo.R;
import com.example.plusgo.UPM.ViewReportTripHistoryActivity;
import com.example.plusgo.Utility.DriverListItem;
import com.example.plusgo.Utility.ReportedDriverListItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReportedDriverListAdapter extends RecyclerView.Adapter<ReportedDriverListAdapter.ViewHolder> {

    private Context context;
    private List<ReportedDriverListItem> driverListItems;
    ReportedDriverListItem driverListItem;
    BaseContent BASECONTENT = new BaseContent();
    public ReportedDriverListAdapter(List<ReportedDriverListItem> driverListItems, Context context) {
        this.driverListItems = driverListItems;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View v = LayoutInflater.from(viewGroup.getContext())
               .inflate(R.layout.reported_drivers_list, viewGroup, false);
       return new ViewHolder(v);
    }

    //bind the values to the list
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        driverListItem = driverListItems.get(i);
        viewHolder.txtName.setText(driverListItem.getName());
        viewHolder.txtAmt.setText(String.valueOf(driverListItem.getAmt()));
        viewHolder.txtTripsCount.setText(String.valueOf(driverListItem.getCount()));
        viewHolder.txtUid.setText(driverListItem.getUid());
        Log.d("txtUid", String.valueOf(viewHolder.txtUid.getText()));
        viewHolder.txtdUid.setText(driverListItem.getDuid());
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.user2).error(R.drawable.user2);

        viewHolder.driverCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //implement onClick
                Intent i = new Intent(context, ViewReportTripHistoryActivity.class);
                TextView puid  =(TextView)view.findViewById(R.id.puidHidden);
                TextView duid  =(TextView)view.findViewById(R.id.duidHidden);
                i.putExtra("PUID", String.valueOf(puid.getText()));
                i.putExtra("DUID", String.valueOf(duid.getText()));
                view.getContext().startActivity(i);
            }
        });

        //To load image using Glide
        Glide.with(context)
                .load(BASECONTENT.IpAddress  +driverListItem.getImage())
                .apply(requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .into(viewHolder.profileImage);

    }



    //get number of elements in the list
    @Override
    public int getItemCount() {
        return driverListItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtName;
        public TextView txtAmt;
        public TextView txtTripsCount;
        public TextView txtUid;
        public TextView txtdUid;
        public CircleImageView profileImage;
        public CardView driverCard;
//        public Button checkHistory;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView)itemView.findViewById(R.id.txtFullName1);
            txtAmt = (TextView)itemView.findViewById(R.id.txtAmt);
            txtTripsCount = (TextView)itemView.findViewById(R.id.txtTripsCount);
            txtUid = (TextView)itemView.findViewById(R.id.puidHidden);
            txtdUid = (TextView)itemView.findViewById(R.id.duidHidden);
            profileImage = (CircleImageView)itemView.findViewById(R.id.proImage1);
            driverCard = (CardView) itemView.findViewById(R.id.driver_card1);
//            checkHistory = (Button) itemView.findViewById(R.id.checkHis);

        }
    }

}
