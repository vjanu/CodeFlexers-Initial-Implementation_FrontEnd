package com.example.plusgo.Notification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.plusgo.DVPRM.RatingMainActivity;
import com.example.plusgo.R;
import com.example.plusgo.SignUp;

public class StartTripNotification  extends AppCompatActivity {
    //Display Notification
    public static void displayNotification(Context context, String title, String body){


        NotificationCompat.Builder mbuilder =
                new NotificationCompat.Builder(context, SignUp.CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_note)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_MAX);
        //Set Notification Sound
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mbuilder.setSound(alarmSound);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1,mbuilder.build());

    }
}
