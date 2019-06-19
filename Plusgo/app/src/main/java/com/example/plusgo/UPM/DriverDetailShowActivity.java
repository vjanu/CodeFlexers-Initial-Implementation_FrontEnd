package com.example.plusgo.UPM;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.plusgo.R;

public class DriverDetailShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_detail_show);
        Intent intent = getIntent();

        String myVar1 = intent.getStringExtra("TID");
        String myVar2 = intent.getStringExtra("UID");
        String myVar3 = intent.getStringExtra("Name");
        String myVar4 = intent.getStringExtra("Source");
        String myVar5 = intent.getStringExtra("Destination");
        String myVar6 = intent.getStringExtra("img");



        Log.d("q1:", myVar1);
        Log.d("q2:", myVar2);
        Log.d("q3:", myVar3);
        Log.d("q4:", myVar4);
        Log.d("q5:", myVar5);
        Log.d("q6:", myVar6);
    }
}
