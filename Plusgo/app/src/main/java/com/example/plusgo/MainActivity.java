package com.example.plusgo;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity {


    Button NICbutton;
    Button RatingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Start NIC activity
        NICbutton=(Button)findViewById(R.id.NICbutton);
        NICbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NicUploadActivity.class);
                startActivity(i);
            }
        });

        //Start Rating activity
        RatingButton=(Button)findViewById(R.id.Ratingbutton);
        RatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RatingMainActivity.class);
                startActivity(i);
            }
        });
    }
}
