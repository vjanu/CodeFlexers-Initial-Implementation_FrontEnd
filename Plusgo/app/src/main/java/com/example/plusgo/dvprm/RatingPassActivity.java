/*
 * *
 *  * Created by Ashane Edirisinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/6/19 11:59 PM
 *
 */

package com.example.plusgo.dvprm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.plusgo.R;
import com.example.plusgo.Signup;

public class RatingPassActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    LinearLayout layoutrew;
    Button btnother;
    Button back;
    Button keyw1,keyw2,keyw3,keyw4,keyw5,keyw6;
    String vehiclekey[]={"Air Condition","Comfortability","Cleanliness","Noise","Vehicle Quality","Break Functionality"};
    String driverkey[]={"Bad Navigation","Professionalism","Cleanliness","Service","Music","Reckless Driving"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_pass);

        DisplayMetrics dm1 = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm1);

        int width = dm1.widthPixels;
        int height = dm1.heightPixels;

        getWindow().setLayout((int)(width*.95),(int)(height*.70));

        layoutrew = (LinearLayout)findViewById(R.id.layoutownrew);
        btnother =(Button)findViewById(R.id.btnother);
        btnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutrew.setVisibility(View.VISIBLE);
            }
        });

        back =(Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent verify = new Intent(RatingPassActivity.this, RatingMainActivity.class);
//                startActivity(verify);
                finish();
            }
        });

        keyw1 =(Button)findViewById(R.id.keyw1);
        keyw2 =(Button)findViewById(R.id.keyw2);
        keyw3 =(Button)findViewById(R.id.keyw3);
        keyw4 =(Button)findViewById(R.id.keyw4);
        keyw5 =(Button)findViewById(R.id.keyw5);
        keyw6 =(Button)findViewById(R.id.keyw6);

        sharedpreferences = getSharedPreferences("rating_preference", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        String selectedTab = (sharedpreferences.getString("selectedRateTab", "vehicle"));

        if(selectedTab.equals("vehicle")) {
            keyw1.setText(vehiclekey[0]);
            keyw2.setText(vehiclekey[1]);
            keyw3.setText(vehiclekey[2]);
            keyw4.setText(vehiclekey[3]);
            keyw5.setText(vehiclekey[4]);
            keyw6.setText(vehiclekey[5]);
        }else if(selectedTab.equals("driver")){
            keyw1.setText(driverkey[0]);
            keyw2.setText(driverkey[1]);
            keyw3.setText(driverkey[2]);
            keyw4.setText(driverkey[3]);
            keyw5.setText(driverkey[4]);
            keyw6.setText(driverkey[5]);
        }

        keyw1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutrew.setVisibility(View.GONE);
                ((Button)v).setBackgroundColor(Color.TRANSPARENT);
                editor.putString("selectedKey", keyw1.getText().toString());
                editor.commit();
            }
        });
    }
}
