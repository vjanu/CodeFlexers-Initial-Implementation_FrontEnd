/*
 * *
 *  * Created by Ashane Edirisinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/3/19 9:49 PM
 *
 */

package com.example.plusgo.DVPRM;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.plusgo.R;

import org.json.JSONObject;

import java.util.Date;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.GRAY;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

public class RatingMainActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    RatingBar ratingbar;
    LinearLayout layoutcomplement;
    LinearLayout layoutdissatis;
    LinearLayout layoutsubmit;
    Button vehiclebtn,driverbtn,copassengerbtn,submit_rating,submit_complement;
    ImageView vehi,dri,coop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_main);

        sharedpreferences = getSharedPreferences("rating_preference", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();

        submit_rating=(Button)findViewById(R.id.submit_rating);
        submit_complement=(Button)findViewById(R.id.submit_complement);

        ratingbar = (RatingBar)findViewById(R.id.ratingBar);
        layoutcomplement = (LinearLayout)findViewById(R.id.layoutcomplement);
        layoutdissatis = (LinearLayout)findViewById(R.id.layoutdissatis);
        layoutsubmit = (LinearLayout)findViewById(R.id.layoutsubmit);

        vehi = (ImageView)findViewById(R.id.img);
        dri = (ImageView)findViewById(R.id.img1);
        coop = (ImageView)findViewById(R.id.img2);



        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                Toast.makeText(getApplicationContext(),"Your Selected Ratings  : " + String.valueOf(rating),Toast.LENGTH_LONG).show();
                if(rating == 5.0){
                    layoutcomplement.setVisibility(View.VISIBLE);
                    layoutdissatis.setVisibility(View.GONE);
                    layoutsubmit.setVisibility(View.VISIBLE);
                    submit_complement.setVisibility(View.VISIBLE);
                    submit_rating.setVisibility(View.GONE);
                }else{
                    layoutcomplement.setVisibility(View.GONE);
                    layoutdissatis.setVisibility(View.VISIBLE);
                    layoutsubmit.setVisibility(View.VISIBLE);
                    submit_rating.setVisibility(View.VISIBLE);
                    submit_complement.setVisibility(View.GONE);
                }
                editor.putString("GivenRating", String.valueOf(rating));
                editor.commit();
            }
        });
//        sharedpreferences = getSharedPreferences("rating_preference", Context.MODE_PRIVATE);
//        final SharedPreferences.Editor editor = sharedpreferences.edit();

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

        copassengerbtn=(Button)findViewById(R.id.selectedCoPass);
        copassengerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("selectedRateTab", "copassenger");
                editor.commit();
                Intent verify = new Intent(RatingMainActivity.this, CopassengerListActivity.class);
                startActivity(verify);
            }
        });

        submit_rating=(Button)findViewById(R.id.submit_rating);
        submit_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear().commit();
                finish();
            }
        });
        lockButtons();

    }
    @Override
    public void onResume(){
        super.onResume();
        lockButtons();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        sharedpreferences = getSharedPreferences("rating_preference", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear().apply();
    }

    public void lockButtons(){
        Date date = new Date();
        long timeMilli = date.getTime();
        sharedpreferences = getSharedPreferences("rating_preference", Context.MODE_PRIVATE);

        String DONEdriver = (sharedpreferences.getString("done_ratedriver", "NO"));
        String DONEcopassenger = (sharedpreferences.getString("done_copassenger", "NO"));
        String DONEvehicle = (sharedpreferences.getString("done_ratevehicle", "NO"));

        if(DONEvehicle.equals("YES")){
//            vehiclebtn.setEnabled(false);
            vehiclebtn.setTextColor(GRAY);
            vehi.setImageResource(R.drawable.star);
        }
        if(DONEdriver.equals("YES")){
            driverbtn.setTextColor(GRAY);
            dri.setImageResource(R.drawable.star);
        }
    }
}
