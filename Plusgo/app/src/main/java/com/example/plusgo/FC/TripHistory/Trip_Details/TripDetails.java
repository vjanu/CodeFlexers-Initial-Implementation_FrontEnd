package com.example.plusgo.FC.TripHistory.Trip_Details;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.plusgo.FC.FragmentCurrentPassenger;
import com.example.plusgo.FC.FragmentMap;
import com.example.plusgo.FC.TripHistory.Driver;
import com.example.plusgo.FC.ViewPagerAdapter;
import com.example.plusgo.R;

public class TripDetails extends AppCompatActivity {
    private TabLayout tabLayout;
    //private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    private TextView txtDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_trip_details);
//
//        Intent intent = getIntent();
//        String TripId = intent.getStringExtra("TID");
//        String Date = intent.getStringExtra("DATE");
//
//        txtDate = (TextView)findViewById(R.id.txtTripDate);
//        txtDate.setText(Date);
//
//        tabLayout = (TabLayout) findViewById(R.id.tabLayoutId);
//        //appBarLayout = (AppBarLayout) findViewById(R.id.appbarId);
//        viewPager = (ViewPager) findViewById(R.id.view_pager_history_id);
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //Adding Fragment



//        adapter.AddFrgment(new DriverHistoryDetailsFragment(),"Driver Details");
//        adapter.AddFrgment(new PassengerHistoryDetailsFragment(TripId),"Co-Passengers Details");
//        adapter.AddFrgment(new ReceiptFragment(),"Receipt");
//
//        //adapter Setup
//        viewPager.setAdapter(adapter);
//        tabLayout.setupWithViewPager(viewPager);
    }
}
