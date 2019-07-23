package com.example.plusgo.FC;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plusgo.OPR.MainActivity;
import com.example.plusgo.R;
import com.example.plusgo.UPM.PaymentActivity;
import com.sohrab.obd.reader.application.ObdPreferences;
import com.sohrab.obd.reader.obdCommand.ObdConfiguration;
import com.sohrab.obd.reader.service.ObdReaderService;
import com.sohrab.obd.reader.trip.TripRecord;

import static com.sohrab.obd.reader.constants.DefineObdReader.ACTION_OBD_CONNECTION_STATUS;
import static com.sohrab.obd.reader.constants.DefineObdReader.ACTION_READ_OBD_REAL_TIME_DATA;

public class MapCurrentPassengerActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    public TextView txtMileage;
    public static String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_current_passenger);


        tabLayout = (TabLayout) findViewById(R.id.tabLayoutId);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbarId);
        viewPager = (ViewPager) findViewById(R.id.view_pager_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //txtMileage = findViewById(R.id.txtMileage);
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




//    /**
//     * Broadcast Receiver to receive OBD connection status and real time data
//     */
//    private final BroadcastReceiver mObdReaderReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            // findViewById(R.id.progress_bar).setVisibility(View.GONE);
//            txtMileage.setVisibility(View.VISIBLE);
//            String action = intent.getAction();
//
//            if (action.equals(ACTION_OBD_CONNECTION_STATUS)) {
//                result = "9";
//                String connectionStatusMsg = intent.getStringExtra(ObdReaderService.INTENT_OBD_EXTRA_DATA);
//                txtMileage.setText(connectionStatusMsg);
//                Toast.makeText(MapCurrentPassengerActivity.this, connectionStatusMsg, Toast.LENGTH_SHORT).show();
//
//                if (connectionStatusMsg.equals(getString(R.string.obd_connected))) {
//                    //OBD connected  do what want after OBD connection
//                } else if (connectionStatusMsg.equals(getString(R.string.connect_lost))) {
//                    //OBD disconnected  do what want after OBD disconnection
//                } else {
//                    // here you could check OBD connection and pairing status
//                }
//
//            } else if (action.equals(ACTION_READ_OBD_REAL_TIME_DATA)) {
//
//                TripRecord tripRecord = TripRecord.getTripRecode(MapCurrentPassengerActivity.this);
//                //result = tripRecord.getMileage();
//                result = "3";
//                txtMileage.setText(tripRecord.getMileage());
//
//            }
//
//        }
//    };




}