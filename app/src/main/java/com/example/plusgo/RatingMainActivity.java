package com.example.plusgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

public class RatingMainActivity extends AppCompatActivity {

    RatingBar ratingbar;
    LinearLayout layoutcomplement;
    LinearLayout layoutdissatis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_main);

        ratingbar = (RatingBar)findViewById(R.id.ratingBar);
        layoutcomplement = (LinearLayout)findViewById(R.id.layoutcomplement);
        layoutdissatis = (LinearLayout)findViewById(R.id.layoutdissatis);

        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                Toast.makeText(getApplicationContext(),"Your Selected Ratings  : " + String.valueOf(rating),Toast.LENGTH_LONG).show();
                if(rating == 5.0){
                    layoutcomplement.setVisibility(View.VISIBLE);
                    layoutdissatis.setVisibility(View.GONE);
                }else{
                    layoutcomplement.setVisibility(View.GONE);
                    layoutdissatis.setVisibility(View.VISIBLE);
                }
            }
        });
//        button=(Button)findViewById(R.id.nic_upload);
    }
}
