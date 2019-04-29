package com.example.plusgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * @author  Viraj
 */
public class Signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

//        //Calling Login class
//        Button btnSignup = (Button)findViewById(R.id.btnLogin);
//        btnLog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent verify = new Intent(Signup.this, VerifyMobilePhone.class);
//                startActivity(verify);
//
//            }
//        });
    }
}
