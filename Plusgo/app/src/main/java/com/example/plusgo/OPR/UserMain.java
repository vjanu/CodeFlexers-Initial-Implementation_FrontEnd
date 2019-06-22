package com.example.plusgo.OPR;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.plusgo.Login;
import com.example.plusgo.R;
import com.example.plusgo.UPM.DriverListActivity;

public class UserMain extends AppCompatActivity {

    private ImageButton logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        logout = (ImageButton)findViewById(R.id.logout1);
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

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

    }

    public void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("userStore", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear().commit();

        finish();
        Intent login = new Intent(UserMain.this, Login.class);
        startActivity(login);
    }
}
