package com.example.plusgo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.plusgo.OPR.RiderMenu;
import com.example.plusgo.OPR.UserMain;

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

                    SharedPreferences user = getSharedPreferences("userStore",MODE_PRIVATE);
                    boolean Islogin = user.getBoolean("Islogin", false);

                    Log.d("login", String.valueOf(Islogin));
                    if(Islogin)
                    {   // condition true means user is already login
                        Intent home = new Intent(MainActivity.this, UserMain.class);
                        startActivity(home);
                    }

                    else
                    {
                        Intent home = new Intent(MainActivity.this, Login.class);
                        startActivity(home);
                    }

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
