/*
 * *
 *  * Created by Ashane Edirisinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/18/19 2:04 PM
 *
 */

package com.example.plusgo.DVPRM.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.plusgo.R;
import com.example.plusgo.DVPRM.model.copassenger;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private Context mContext;
    private List<copassenger> mData;
    RequestOptions option;

    public RecyclerViewAdapter(Context mContext, List<copassenger> mData) {
        this.mContext = mContext;
        this.mData = mData;

        option = new RequestOptions().centerCrop().placeholder(R.color.colorAccent).error(R.color.colorAccent);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view;
        LayoutInflater inflater =LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.copassenger_row_item,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.tv_name.setText(mData.get(i).getName());
        Glide.with(mContext).load(mData.get(i).getImage_url()).apply(option).into(viewHolder.img_thumb);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_name;
        ImageView img_thumb;


        public ViewHolder(View itemView){
            super(itemView);

            tv_name = itemView.findViewById(R.id.name);
            img_thumb= itemView.findViewById(R.id.copimg);

        }
    }

}
