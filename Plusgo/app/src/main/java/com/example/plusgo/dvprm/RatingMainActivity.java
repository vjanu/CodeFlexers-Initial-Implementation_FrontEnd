/*
 * *
 *  * Created by Ashane Edirisinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/3/19 9:49 PM
 *
 */

package com.example.plusgo.dvprm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.plusgo.R;

public class RatingMainActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    RatingBar ratingbar;
    LinearLayout layoutcomplement;
    LinearLayout layoutdissatis;
    Button vehiclebtn,driverbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_main);

        ratingbar = (RatingBar)findViewById(R.id.ratingBar);
        layoutcomplement = (LinearLayout)findViewById(R.id.layoutcomplement);
        layoutdissatis = (LinearLayout)findViewById(R.id.layoutdissatis);

        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                Toast.makeText(getApplicationContext(),"Your Selected Ratings  : " + String.valueOf(rating),Toast.LENGTH_LONG).show();
                if(rating == 5.0){
                    layoutcomplement.setVisibility(View.VISIBLE);
                    layoutdissatis.setVisibility(View.GONE);
                }else{
                    layoutcomplement.setVisibility(View.GONE);
                    layoutdissatis.setVisibility(View.VISIBLE);
                }
            }
        });
        sharedpreferences = getSharedPreferences("rating_preference", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();

        vehiclebtn=(Button)findViewById(R.id.selectedVehicle);
        vehiclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putString("selectedRateTab", "vehicle");
                editor.commit();
                Intent verify = new Intent(RatingMainActivity.this, RatingPassActivity.class);
                startActivity(verify);
            }
        });

        driverbtn=(Button)findViewById(R.id.selectedDriver);
        driverbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putString("selectedRateTab", "driver");
                editor.commit();
                Intent verify = new Intent(RatingMainActivity.this, RatingPassActivity.class);
                startActivity(verify);
            }
        });
    }
}
