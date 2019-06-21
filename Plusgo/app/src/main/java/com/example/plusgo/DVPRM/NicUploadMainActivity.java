/*
 * *
 *  * Created by Ashane Edirisinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/19/19 9:47 AM
 *
 */

package com.example.plusgo.DVPRM;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.plusgo.R;

public class NicUploadMainActivity extends AppCompatActivity {

    Button btnGeneralNIC,btnElecNIC;
    LinearLayout nic_upload_elec_ll,nic_upload_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nic_upload_main);

        btnGeneralNIC =(Button)findViewById(R.id.nic_upload);
        btnElecNIC =(Button)findViewById(R.id.nic_upload_elec);
        nic_upload_elec_ll =(LinearLayout)findViewById(R.id.nic_upload_elec_ll);
        nic_upload_ll =(LinearLayout)findViewById(R.id.nic_upload_ll);

        btnGeneralNIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("dvprm", MODE_PRIVATE).edit();
                editor.putString("dv_type", "general");
                editor.apply();
                Intent verify = new Intent(NicUploadMainActivity.this, NicUpActivity.class);
                startActivity(verify);
                finish();
            }
        });

        nic_upload_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("dvprm", MODE_PRIVATE).edit();
                editor.putString("dv_type", "general");
                editor.apply();
                Intent verify = new Intent(NicUploadMainActivity.this, NicUpActivity.class);
                startActivity(verify);
                finish();
            }
        });

        nic_upload_elec_ll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("dvprm", MODE_PRIVATE).edit();
                editor.putString("dv_type", "electronic");
                editor.apply();
                Intent verify1 = new Intent(NicUploadMainActivity.this, NicUpActivity.class);
                startActivity(verify1);
                finish();
            }
        });

        btnElecNIC.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("dvprm", MODE_PRIVATE).edit();
                editor.putString("dv_type", "electronic");
                editor.apply();
                Intent verify1 = new Intent(NicUploadMainActivity.this, NicUpActivity.class);
                startActivity(verify1);
                finish();
            }
        });
    }
}
