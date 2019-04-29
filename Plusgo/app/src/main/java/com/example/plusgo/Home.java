package com.example.plusgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * @author  Viraj
 */
public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Calling Login class
        Button btnLog = (Button)findViewById(R.id.btnLogin);
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(Home.this, Login.class);
                startActivity(login);

            }
        });

        //Calling Mobile Verification class
        Button btnSign = (Button)findViewById(R.id.btnSignup);
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(Home.this, Signup.class);
                startActivity(signup);

            }
        });
    }
}
