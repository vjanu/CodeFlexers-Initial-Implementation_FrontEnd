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
import android.view.View;
import android.widget.TextView;

import com.example.plusgo.R;

public class TripSummaryActivity extends AppCompatActivity {

    private TextView txtViewProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_summary);

        txtViewProfile = (TextView)findViewById(R.id.txtviewprofile);
        txtViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openUserProfile();
            }
        });
    }

    public void openUserProfile(){
        Intent intent = new Intent(this,UserProfile.class);
        startActivity(intent);
    }
}
