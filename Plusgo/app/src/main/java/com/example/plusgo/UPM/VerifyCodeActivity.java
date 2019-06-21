/*
 * *
 *  * Created by Viraj Wickramasinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/15/19 10:21 PM
 *
 */

package com.example.plusgo.UPM;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plusgo.DVPRM.NicUploadMainActivity;
import com.example.plusgo.R;
import com.example.plusgo.SignUp;

public class VerifyCodeActivity extends AppCompatActivity {

    private EditText codeN;
    private TextView phoneN;
    private String code = "";
    private String phone = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);

        SharedPreferences selfStore = getSharedPreferences("Phone",MODE_PRIVATE);
        phone = selfStore.getString("Number", null);
        code = selfStore.getString("Code", null);

        phoneN = (TextView)findViewById(R.id.txtPhone);
        codeN = (EditText)findViewById(R.id.txtCode);

        phoneN.setText("Code is sent to "+phone);

        Button btnVerify = (Button)findViewById(R.id.btnVerify);
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("codeN", codeN.getText().toString());
                Log.d("code", code.toString());
                if(codeN.getText().toString().equals(code)){
                    Toast.makeText(VerifyCodeActivity.this, "Phone Number Verified!", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent verify = new Intent(VerifyCodeActivity.this, NicUploadMainActivity.class);
                    startActivity(verify);
                }
                else{
                    Toast.makeText(VerifyCodeActivity.this, "Incorrect Code", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}
