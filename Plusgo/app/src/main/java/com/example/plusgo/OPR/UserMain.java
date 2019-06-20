package com.example.plusgo.OPR;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.plusgo.R;

public class UserMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        Button request = (Button) findViewById(R.id.btnreq);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(UserMain.this,MainActivity.class);
                startActivity(i);
            }
        });

        Button offer = (Button) findViewById(R.id.btnoffer);
        offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(UserMain.this,RiderMenu.class);
                startActivity(i);
            }
        });

    }

}
