package com.example.plusgo.FC;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.TimedMetaData;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.plusgo.MainActivity;
import com.example.plusgo.R;

import java.util.Timer;
import java.util.TimerTask;

public class MapCurrentPassengerActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;

    public static Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_current_passenger);

//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(MapCurrentPassengerActivity.this,MapCurrentPassengerActivity.class);
//                startActivity(intent);
//                finish();
//
//            }
//        },20000);

        tabLayout = (TabLayout) findViewById(R.id.tabLayoutId);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbarId);
        viewPager = (ViewPager) findViewById(R.id.view_pager_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //Adding Fragment
        // adapter.AddFrgment(new FragmentPassenger(),"Passenger");
        SharedPreferences tripStore = getSharedPreferences("tripStore",MODE_PRIVATE);
        String TripId = tripStore.getString("TripId", null);

        adapter.AddFrgment(new FragmentCurrentPassenger(TripId),"Current Passenger");
        adapter.AddFrgment(new FragmentMap(),"Map");



        //adapter Setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

}
