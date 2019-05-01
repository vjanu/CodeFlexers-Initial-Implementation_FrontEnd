/*
 * *
 *  * Athrie
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/1/19 11:45 AM
 *
 */

package com.example.plusgo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class OfferRide extends AppCompatActivity {

    EditText source_txt,desti_txt,starttime_txt,startdate_txt,waitingtime_txt;
    Button nxt_btn;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_ride);

        source_txt=findViewById(R.id.source_txt);
        desti_txt=findViewById(R.id.desti_txt);
        starttime_txt=findViewById(R.id.strtime_txt);
        startdate_txt=findViewById(R.id.date_txt);
        waitingtime_txt=findViewById(R.id.waitingtime_txt);
        nxt_btn=findViewById(R.id.nxt_btn);

        nxt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
