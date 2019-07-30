package com.example.plusgo.FC.TripHistory.Trip_Details;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.plusgo.FC.FragmentCurrentPassenger;
import com.example.plusgo.FC.FragmentMap;
import com.example.plusgo.FC.TripHistory.Driver;
import com.example.plusgo.FC.ViewPagerAdapter;
import com.example.plusgo.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class TripDetails extends AppCompatActivity {
    private TabLayout tabLayout;
    //private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    private TextView txtDate,txtFare,txtStartPoint,txtEndPoint,txtDriverId;
    public ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        Intent intent = getIntent();
        String TripId = intent.getStringExtra("TID");
        String Date = intent.getStringExtra("DATE");
        String Fare = intent.getStringExtra("FARE");
        String Start = intent.getStringExtra("START");
        String End = intent.getStringExtra("END");
        String txtsourceLatLong = intent.getStringExtra("txtsourceLatLong");
        String txtdestinationLatLong = intent.getStringExtra("txtdestinationLatLong");
        String txtDriverId = intent.getStringExtra("txtDriverId"); // Passed value to the DriverHistoryDetailsFragment() fragment

        Log.d("txtsourceLatLong",txtsourceLatLong);
        Log.d("txtdestinationLatLong",txtdestinationLatLong);
        Log.d("txtDriverId",txtDriverId);

        String soureLang = txtsourceLatLong.split(",")[0];
        String soureLat = txtsourceLatLong.split(",")[1];

        String destinationLang = txtdestinationLatLong.split(",")[0];
        String destinationLat = txtdestinationLatLong.split(",")[1];

        double centerLang = (Double.parseDouble(soureLang)+Double.parseDouble(destinationLang))/2;
        double centerLat = (Double.parseDouble(soureLat)+Double.parseDouble(destinationLat))/2;

        SharedPreferences userStore = getSharedPreferences("userStore",MODE_PRIVATE);
        String UID = userStore.getString("UId", null);

        Log.d("centerLang", String.valueOf(centerLang));
        Log.d("centerLat", String.valueOf(centerLat));


        txtDate = (TextView)findViewById(R.id.txtTripDate);
        txtFare = (TextView)findViewById(R.id.txtFare);
        txtStartPoint = (TextView)findViewById(R.id.txtStartPoint);
        txtEndPoint = (TextView)findViewById(R.id.txtEndPoint);
        imgLogo = (ImageView)findViewById(R.id.imgLogo);



        txtDate.setText(Date);
        txtFare.setText(Fare);
        txtStartPoint.setText(Start);
        txtEndPoint.setText(End);

        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.maploading).error(R.drawable.maploading);

        //To load image using Glide
        Glide.with(this)
                .load("https://maps.googleapis.com/maps/api/staticmap?center="+centerLang+","+centerLat+"&zoom=12&size=600x300&maptype=roadmap&markers=color:green%7C"+txtsourceLatLong+"&markers=color:red%7C"+txtdestinationLatLong+"&key=AIzaSyA61V4HM6lh6imhP6x0nG7W9vOAp8V318E")
                .apply(requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .into(imgLogo);

        tabLayout = (TabLayout) findViewById(R.id.tabLayoutId);
        //appBarLayout = (AppBarLayout) findViewById(R.id.appbarId);
        viewPager = (ViewPager) findViewById(R.id.view_pager_history_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //Adding Fragment



        adapter.AddFrgment(new DriverHistoryDetailsFragment(txtDriverId),"Driver Details");
        adapter.AddFrgment(new PassengerHistoryDetailsFragment(TripId),"Co-Passengers Details");
        adapter.AddFrgment(new ReceiptActivityPassenger(UID,TripId),"Receipt");

        //adapter Setup

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
