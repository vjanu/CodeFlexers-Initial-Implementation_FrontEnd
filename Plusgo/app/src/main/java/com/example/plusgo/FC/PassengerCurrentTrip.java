package com.example.plusgo.FC;

import android.app.MediaRouteButton;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.plusgo.R;

public class PassengerCurrentTrip extends AppCompatActivity {

    private TextView txtPassengerName,txtStartPoint,txtEndPoint,txtHiddenTripId,txtHiddenPassengerId,txtHiddenToken,txtTripStatus;
    public Button btnEndTrip,btnStartTrip;
    private ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_current_trip);

        Intent intent = getIntent();

        String TripId = intent.getStringExtra("TripId");
        String PassengerId = intent.getStringExtra("PassengerId");
        String Name = intent.getStringExtra("Name");
        String startpoint = intent.getStringExtra("Source");
        String endpoint = intent.getStringExtra("Destination");
        String Token = intent.getStringExtra("Token");
        String Status = intent.getStringExtra("Status");
        String userImage = intent.getStringExtra("userImage");
        //String startButtonVisibility = intent.getStringExtra("btnStartifTripNotStartVisibility");



        //Variable assign With TextView Which is used in the layout
        txtPassengerName = (TextView)findViewById(R.id.txtPassengerName);
        txtStartPoint = (TextView)findViewById(R.id.txtStartPoint);
        txtEndPoint = (TextView)findViewById(R.id.txtEndPoint);
        txtHiddenTripId = (TextView)findViewById(R.id.txtHiddenTripId);
        txtHiddenPassengerId = (TextView)findViewById(R.id.txtHiddenPassengerId);
        txtHiddenToken = (TextView)findViewById(R.id.txtHiddenToken);
        txtTripStatus = (TextView)findViewById(R.id.txtTripStatus);
        btnStartTrip = (Button)findViewById(R.id.btnStartTrip);
        btnEndTrip = (Button) findViewById(R.id.btnEndTrip);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);

        txtHiddenTripId.setText(TripId);
        txtHiddenPassengerId.setText(PassengerId);
        txtPassengerName.setText(Name);
        txtStartPoint.setText(startpoint);
        txtEndPoint.setText(endpoint);
        txtHiddenToken.setText(Token);
        txtTripStatus.setText(Status);
        //Need To user Image to the imgLogo

        Log.d("status",Status);

        if(Status.equals("Trip Started")){
            btnStartTrip.setVisibility(View.GONE);
            btnEndTrip.setVisibility(View.VISIBLE);
        }

        else if(Status.equals("Trip Not Started")){
            btnEndTrip.setVisibility(View.GONE);
            btnStartTrip.setVisibility(View.VISIBLE);
        }
        else{
            //Trip Ended
            btnEndTrip.setVisibility(View.GONE);
            btnStartTrip.setVisibility(View.GONE);
        }




    }


}
