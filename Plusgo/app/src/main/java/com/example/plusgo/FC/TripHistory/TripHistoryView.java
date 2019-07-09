package com.example.plusgo.FC.TripHistory;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.plusgo.OPR.MainActivity;
import com.example.plusgo.R;

public class TripHistoryView extends AppCompatActivity {
    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history_view);

        tabLayout = (TabLayout) findViewById(R.id.tabLayoutId);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbarId);
        viewPager = (ViewPager) findViewById(R.id.view_pager_id);
        HistoryPagerAdapter adapter = new HistoryPagerAdapter(getSupportFragmentManager());
        //Adding Fragment
        adapter.AddFrgment(new PassengerFragment(),"Passenger");
       adapter.AddFrgment(new DriverFragment(),"Driver");


        //adapter Setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(TripHistoryView.this, MainActivity.class));
    }
}
