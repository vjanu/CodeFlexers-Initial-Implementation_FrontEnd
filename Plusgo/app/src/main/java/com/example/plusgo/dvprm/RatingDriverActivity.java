/*
 * *
 *  * Created by Ashane Edirisinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/18/19 4:33 PM
 *
 */

package com.example.plusgo.dvprm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.plusgo.R;

import static android.graphics.Color.BLACK;

public class RatingDriverActivity extends AppCompatActivity {

    RatingBar ratingbarD;
    CardView cardview,cardview2;
    LinearLayout layoutSubmitD;
    Button btnOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_driver);

        ratingbarD = (RatingBar)findViewById(R.id.ratingBarD);
        cardview =(CardView)findViewById(R.id.cardViewForDriver);
        cardview2 =(CardView)findViewById(R.id.cardViewForDriver2);
        layoutSubmitD=(LinearLayout) findViewById(R.id.layoutsubmitD);
        ratingbarD.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

//                Toast.makeText(getApplicationContext(),"Your Selected Ratings  : " + String.valueOf(rating),Toast.LENGTH_LONG).show();
                if(rating == 5.0){
                    cardview.setVisibility(View.GONE);
                    layoutSubmitD.setVisibility(View.VISIBLE);
                    cardview2.setVisibility(View.GONE);
                }else{
                    cardview.setVisibility(View.VISIBLE);
                    layoutSubmitD.setVisibility(View.GONE);
                    cardview2.setVisibility(View.GONE);
                }
            }
        });

        btnOther =(Button)findViewById(R.id.keywd6);
        btnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardview.setVisibility(View.GONE);
                layoutSubmitD.setVisibility(View.VISIBLE);
                cardview2.setVisibility(View.VISIBLE);
            }
        });
    }
}
