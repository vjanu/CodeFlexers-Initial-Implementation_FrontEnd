/*
 * *
 *  * Created by Surath Gunawardena
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 4/30/19 9:47 AM
 *
 */

package com.example.plusgo.FC;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.plusgo.R;

public class TripSummaryActivity extends AppCompatActivity {

    private TextView txtViewProfile;
    private TextView txtUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_summary);

        //added Viraj------------
        Intent intent = getIntent();

        String myVar1 = intent.getStringExtra("TID");
        String myVar2 = intent.getStringExtra("UID");
        String myVar3 = intent.getStringExtra("Name");
        String myVar4 = intent.getStringExtra("Source");
        String myVar5 = intent.getStringExtra("Destination");



        Log.d("q1:", myVar1);
        Log.d("q2:", myVar2);
        Log.d("q3:", myVar3);
        Log.d("q4:", myVar4);
        Log.d("q5:", myVar5);

        //added Viraj------------


        txtViewProfile = (TextView)findViewById(R.id.txtviewprofile);
        txtUserName = (TextView)findViewById(R.id.txtUserName);
        txtViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openUserProfile();
            }
        });

        //added Viraj
        txtUserName.setText(myVar3);
    }

    public void openUserProfile(){
        Intent intent = new Intent(this,UserProfile.class);
        startActivity(intent);
    }
}
