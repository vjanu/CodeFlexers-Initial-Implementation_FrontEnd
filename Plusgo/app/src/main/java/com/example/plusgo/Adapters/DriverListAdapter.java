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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.plusgo.BaseContent;
import com.example.plusgo.FC.TripSummaryActivity;
import com.example.plusgo.R;
import com.example.plusgo.Utility.DriverListItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DriverListAdapter extends RecyclerView.Adapter<DriverListAdapter.ViewHolder> {

    private Context context;
    private List<DriverListItem> driverListItems;
    DriverListItem driverListItem;
    BaseContent BASECONTENT = new BaseContent();
    public DriverListAdapter(List<DriverListItem> driverListItems, Context context) {
        this.driverListItems = driverListItems;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View v = LayoutInflater.from(viewGroup.getContext())
               .inflate(R.layout.driver_list, viewGroup, false);
       return new ViewHolder(v);
    }

    //bind the values to the list
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        driverListItem = driverListItems.get(i);
        viewHolder.txtName.setText(driverListItem.getName());
        viewHolder.txtStart.setText(driverListItem.getStarting());
        viewHolder.txtDest.setText(driverListItem.getDestination());
        viewHolder.txtRate.setText(String.valueOf(driverListItem.getRate()));
        viewHolder.txtTime.setText(driverListItem.getTime());
        viewHolder.txtTid.setText(driverListItem.getTid());
        viewHolder.txtUid.setText(driverListItem.getUid());
        viewHolder.txtToken.setText(driverListItem.getToken());

        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.user2).error(R.drawable.user2);

        viewHolder.driverCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //implement onClick
                Intent i = new Intent(context, TripSummaryActivity.class);

                //tripid, userid, fullname, source detination
                TextView tid  =(TextView)view.findViewById(R.id.tripidHidden);
                TextView uid  =(TextView)view.findViewById(R.id.uidHidden);
                TextView name  =(TextView)view.findViewById(R.id.txtFullName);
                TextView source  =(TextView)view.findViewById(R.id.txtStarting);
                TextView destination  =(TextView)view.findViewById(R.id.txtDestination);
                CircleImageView profileImage = (CircleImageView)view.findViewById(R.id.proImage);
                TextView txtToken = (TextView) view.findViewById(R.id.txtTokenHidden);

                i.putExtra("TID", String.valueOf(tid.getText()));
                i.putExtra("UID", String.valueOf(uid.getText()));
                i.putExtra("Name", String.valueOf(name.getText()));
                i.putExtra("Source", String.valueOf(source.getText()));
                i.putExtra("Destination",String.valueOf(destination.getText()));
                i.putExtra("img",String.valueOf(profileImage));
                i.putExtra("Token",String.valueOf(txtToken.getText()));


                view.getContext().startActivity(i);
            }
        });

        //To load image using Glide
        Glide.with(context)
                .load(BASECONTENT.IpAddress  +driverListItem.getImage())
                .apply(requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .into(viewHolder.profileImage);

        //click event of each card

    }



    //get number of elements in the list
    @Override
    public int getItemCount() {
        return driverListItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtName;
        public TextView txtStart;
        public TextView txtDest;
        public TextView txtRate;
        public TextView txtTid;
        public TextView txtUid;
        public TextView txtToken;

        public TextView txtTime;
        public CircleImageView profileImage;
        public CardView driverCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView)itemView.findViewById(R.id.txtFullName);
            txtStart = (TextView)itemView.findViewById(R.id.txtStarting);
            txtDest = (TextView)itemView.findViewById(R.id.txtDestination);
            txtRate = (TextView)itemView.findViewById(R.id.txtRating);
            txtTime = (TextView)itemView.findViewById(R.id.txtTime);
            txtTid = (TextView)itemView.findViewById(R.id.tripidHidden);
            txtUid = (TextView)itemView.findViewById(R.id.uidHidden);
            profileImage = (CircleImageView)itemView.findViewById(R.id.proImage);
            driverCard = (CardView) itemView.findViewById(R.id.driver_card);
            txtToken = (TextView) itemView.findViewById(R.id.txtTokenHidden);

        }
    }

}
