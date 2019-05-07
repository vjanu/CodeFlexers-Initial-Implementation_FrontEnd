package com.example.plusgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Signup extends AppCompatActivity {


    Button NICbutton;
    Button RatingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

//        //Calling Login class
//        Button btnSignup = (Button)findViewById(R.id.btnLogin);
//        btnLog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent verify = new Intent(Signup.this, VerifyMobilePhoneActivity.class);
//                startActivity(verify);
//
//            }
//        });

        NICbutton=(Button)findViewById(R.id.NICbutton);
        NICbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent verify = new Intent(Signup.this, NicUploadActivity.class);
                startActivity(verify);

            }
        });

        RatingButton=(Button)findViewById(R.id.Ratingbutton);
        RatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent verify = new Intent(Signup.this, RatingMainActivity.class);
                startActivity(verify);

            }
        });
    }
}
