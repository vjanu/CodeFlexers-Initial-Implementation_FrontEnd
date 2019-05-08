
/*
 * *
 *  * Created by Viraj Wickramasinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 4/17/19 1:40 PM
 *
 */

package com.example.plusgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.plusgo.UPM.AddPreferenceActivity;
import com.example.plusgo.UPM.NewUserActivity;
import com.example.plusgo.UPM.VerifyMobilePhoneActivity;

public class Login extends AppCompatActivity {

    private Button login;
    private TextView signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.btnLog);
        signUp = (TextView) findViewById(R.id.txtSign);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, NewUserActivity.class);
                startActivity(intent);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, VerifyMobilePhoneActivity.class);
                startActivity(intent);
            }
        });

    }
}
