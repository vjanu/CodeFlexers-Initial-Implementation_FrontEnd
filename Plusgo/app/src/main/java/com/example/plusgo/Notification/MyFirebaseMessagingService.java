package com.example.plusgo.Notification;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.example.plusgo.DVPRM.RatingMainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private TextView textview;
    public static String NotificationBodyCatcher;
    public String title;
    public String body;





    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getNotification() !=null){

            title = remoteMessage.getNotification().getTitle();
            body = remoteMessage.getNotification().getBody();


            setBody(body.toString());
            setTitle(title.toString());

            if(title.endsWith("Ride Request")){
                NotificationHelper.displayNotification(getApplicationContext(),title,body);
                //When Driver received the Trip Requested Notification automatically move to the Trip Accept Activity
                Intent intent=new Intent(MyFirebaseMessagingService.this,OpenNotification.class);
                startActivity(intent);

            }if(title.endsWith("Ride Confirmation")){
                //Ride Confirmation
                NotificationHelperAcceptRequest.displayNotification(getApplicationContext(),title,body);
                //Todo : Redirect passenger to the Navigation activity

            }
            if(title.endsWith("Fare")){

                EndTripNotification.displayNotification(getApplicationContext(),title,body);
                //When Passenger recieved the the fare Notification , then automatically move to the Rating Mian activity
                Intent intent=new Intent(MyFirebaseMessagingService.this, RatingMainActivity.class);
                startActivity(intent);
            }
            if(title.equals("Trip is in Progress"))
            {
                StartTripNotification.displayNotification(getApplicationContext(),title,body);
            }

            NotificationBodyCatcher = getBody().toString();


            Log.d("Notification " , body);

        }
    }




}
