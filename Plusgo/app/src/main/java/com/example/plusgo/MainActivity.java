package com.example.plusgo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {



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
                    Intent home = new Intent(MainActivity.this, Login.class);
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
