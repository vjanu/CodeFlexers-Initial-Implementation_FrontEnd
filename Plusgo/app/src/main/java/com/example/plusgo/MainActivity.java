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



        //splash screen at the start
        Thread splashScreen = new Thread(){
            public void run(){
                try {
                    sleep(3000); //for 3 seconds

                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    Intent home = new Intent(MainActivity.this, Home.class);
                    startActivity(home);
                }
            }
        };
        splashScreen.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
