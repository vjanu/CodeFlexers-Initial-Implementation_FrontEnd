package com.example.plusgo.FC;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.plusgo.R;

import java.util.List;

public class DriverHistoryAdapter  extends RecyclerView.Adapter<DriverHistoryAdapter.ViewHolder> {

    //Create List item to the store details
    private List<Driver> driverList;
    private Context context;

    public DriverHistoryAdapter(List<Driver> driverList, Context context) {
        this.driverList = driverList;
        this.context = context;
    }


    @NonNull
    @Override
    public DriverHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.driver_list_history,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverHistoryAdapter.ViewHolder viewHolder, int i) {
        //Read the listItem Iteratively using int i
        //Create an object of ListItem as listitem
        final Driver driverItems = driverList.get(i);
        //txtHead textview set the value from the getters in ListItem.java file
        viewHolder.txtTripId.setText(driverItems.getTripId());
        viewHolder.txtDate.setText(driverItems.getDateTime());
        viewHolder.txtEarn.setText(driverItems.getEarn().toString());
    }

    @Override
    public int getItemCount() {
        return driverList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtTripId;
        public TextView txtDate;
        public TextView txtEarn;
        public RelativeLayout displayLayout;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            txtTripId = (TextView) itemView.findViewById(R.id.txtTripId);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
//            txtEarn = (TextView) itemView.findViewById(R.id.txtEarn);
            displayLayout = (RelativeLayout) itemView.findViewById(R.id.displayLayout);
        }
    }
}
