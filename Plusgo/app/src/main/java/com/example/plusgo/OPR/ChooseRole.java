package com.example.plusgo.OPR;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.plusgo.OPR.OfferRide;
import com.example.plusgo.OPR.SetRidePass;
import com.example.plusgo.R;

public class ChooseRole extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_role);

        Button offerRidebtn = (Button)findViewById(R.id.btnOffer_ride);
        offerRidebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseRole.this, OfferRide.class);
                startActivity(intent);
            }
        });

        Button requestridebtn = (Button)findViewById(R.id.btnrequest_ride);
        requestridebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseRole.this, SetRidePass.class);
                startActivity(intent);
            }
        });

    }

}
