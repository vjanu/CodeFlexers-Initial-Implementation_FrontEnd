package com.example.plusgo.FC.TripHistory.Trip_Details_Driver;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.plusgo.FC.ViewPagerAdapter;
import com.example.plusgo.R;

public class TripDetailsDriver extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView txtDate,txtEarn,txtStartPoint,txtEndPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details_driver);

        Intent intent = getIntent();
        String TripId = intent.getStringExtra("TID");
        String Date = intent.getStringExtra("DATE");
        String Earn = intent.getStringExtra("EARN");
        String Start = intent.getStringExtra("START");
        String End = intent.getStringExtra("END");

        tabLayout = (TabLayout) findViewById(R.id.tabLayoutId);
        viewPager = (ViewPager) findViewById(R.id.view_pager_history_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //Adding Fragment


        txtDate = (TextView)findViewById(R.id.txtTripDate);
        txtEarn = (TextView)findViewById(R.id.txtFare);
        txtStartPoint = (TextView)findViewById(R.id.txtStartPoint);
        txtEndPoint = (TextView)findViewById(R.id.txtEndPoint);
        //imgLogo = (ImageView)findViewById(R.id.imgLogo);

        String splitEarn = Earn.split(" Earn")[0];

        txtDate.setText(Date);
        txtEarn.setText(splitEarn);

        txtStartPoint.setText(Start);
        txtEndPoint.setText(End);

        adapter.AddFrgment(new AllPassengerHistoryDetailsFragment(TripId),"Passengers");
        adapter.AddFrgment(new DriverReceiptFragment(TripId),"Receipt");

        //adapter Setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
