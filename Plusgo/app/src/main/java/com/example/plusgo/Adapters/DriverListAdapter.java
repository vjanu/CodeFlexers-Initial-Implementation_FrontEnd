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
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.plusgo.R;
import com.example.plusgo.UPM.VerifyCodeActivity;
import com.example.plusgo.Utility.DriverListItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DriverListAdapter extends RecyclerView.Adapter<DriverListAdapter.ViewHolder> {

    private Context context;
    private List<DriverListItem> driverListItems;
    DriverListItem driverListItem;
    public DriverListAdapter(List<DriverListItem> driverListItems, Context context) {
        this.driverListItems = driverListItems;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View v = LayoutInflater.from(viewGroup.getContext())
               .inflate(R.layout.comments_list, viewGroup, false);
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
        viewHolder.txtCost.setText(String.valueOf(driverListItem.getCost()));
        viewHolder.txtTime.setText(driverListItem.getTime());
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.user2).error(R.drawable.user2);

        viewHolder.driverCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //implement onClick
                Log.d("driverIDL: ",  driverListItem.getName());
            }
        });

        //To load image using Glide
        Glide.with(context)
                .load(driverListItem.getImage())
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
        public TextView txtCost;
        public TextView txtTime;
        public CircleImageView profileImage;
        public CardView driverCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView)itemView.findViewById(R.id.txtFullName);
            txtStart = (TextView)itemView.findViewById(R.id.txtStarting);
            txtDest = (TextView)itemView.findViewById(R.id.txtDestination);
            txtRate = (TextView)itemView.findViewById(R.id.txtRating);
            txtCost = (TextView)itemView.findViewById(R.id.txtCost);
            txtTime = (TextView)itemView.findViewById(R.id.txtTime);
            profileImage = (CircleImageView)itemView.findViewById(R.id.proImage);
            driverCard = (CardView) itemView.findViewById(R.id.driver_card);

        }
    }

}
