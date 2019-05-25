/*
 * *
 *  * Created by Viraj Wickramasinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/15/19 10:21 PM
 *
 */

package com.example.plusgo.UPM;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.plusgo.R;
import com.example.plusgo.SignUp;

public class VerifyCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);

        //Calling SignUp class
        Button btnVerify = (Button)findViewById(R.id.btnVerify);
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent verify = new Intent(VerifyCodeActivity.this, SignUp.class);
                startActivity(verify);

            }
        });

    }
}
