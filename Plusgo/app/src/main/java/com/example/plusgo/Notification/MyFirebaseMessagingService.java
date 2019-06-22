package com.example.plusgo.Notification;

import android.util.Log;
import android.widget.TextView;

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

            NotificationBodyCatcher = getBody().toString();
            NotificationHelper.displayNotification(getApplicationContext(),title,body);
            //NotificationHelper.displayAcceptNotification(getApplicationContext(),title,body);
            Log.d("Notification " , body);

        }
    }




}
