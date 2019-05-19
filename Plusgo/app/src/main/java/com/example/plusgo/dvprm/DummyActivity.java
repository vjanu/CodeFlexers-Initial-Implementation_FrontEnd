/*
 * *
 *  * Created by Ashane Edirisinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/18/19 10:57 PM
 *
 */

package com.example.plusgo.dvprm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.plusgo.R;

public class DummyActivity extends AppCompatActivity {

    Button ratingDri,RatingPass,UploLice,UploadNic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);

        ratingDri = (Button)findViewById(R.id.button1);
        ratingDri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent verify = new Intent(DummyActivity.this, RatingDriverActivity.class);
                startActivity(verify);
            }
        });
        RatingPass = (Button)findViewById(R.id.button2);
        RatingPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent verify = new Intent(DummyActivity.this, RatingMainActivity.class);
                startActivity(verify);
            }
        });
        UploadNic = (Button)findViewById(R.id.button3);
        UploadNic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent verify = new Intent(DummyActivity.this, NicUploadMainActivity.class);
                startActivity(verify);
            }
        });
        UploLice = (Button)findViewById(R.id.button4);
        UploLice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent verify = new Intent(DummyActivity.this, LisenceUploadActivity.class);
                startActivity(verify);
            }
        });


    }
}
