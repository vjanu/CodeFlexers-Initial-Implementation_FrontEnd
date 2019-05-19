/*
 * *
 *  * Created by Ashane Edirisinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/19/19 9:47 AM
 *
 */

package com.example.plusgo.dvprm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.plusgo.R;

public class NicUploadMainActivity extends AppCompatActivity {

    Button btnGeneralNIC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nic_upload_main);

        btnGeneralNIC =(Button)findViewById(R.id.nic_upload);
        btnGeneralNIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent verify = new Intent(NicUploadMainActivity.this, NicUploadActivity.class);
                startActivity(verify);
            }
        });
    }
}
