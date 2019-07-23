package com.example.plusgo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.plusgo.OPR.UserMain;
import com.example.plusgo.UPM.ReportedDriverListActivity;

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
                    String status = user.getString("Status", null);

                    Log.d("login", String.valueOf(Islogin));
                    if(Islogin && status.equalsIgnoreCase("P"))
                    {   // condition true means user is already login
                        Intent home = new Intent(MainActivity.this, UserMain.class);
                        startActivity(home);
                    }

                    if(Islogin && status.equalsIgnoreCase("S"))
                    {   // condition true means user is already login
                        Intent home = new Intent(MainActivity.this, ReportedDriverListActivity.class);
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
