/*
 * *
 *  * Created by Viraj Wickramasinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/8/19 10:15 PM
 *
 */

package com.example.plusgo.UPM;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.plusgo.R;

public class AddPreferenceActivity extends AppCompatActivity {

    private Button btnConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_preference);


        btnConfirm = (Button)findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vehicle = new Intent(AddPreferenceActivity.this, VehicleActivity.class);
                startActivity(vehicle);
            }
        });
    }
}
