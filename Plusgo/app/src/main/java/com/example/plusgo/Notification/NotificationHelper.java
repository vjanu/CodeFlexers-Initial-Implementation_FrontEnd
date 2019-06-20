package com.example.plusgo.Notification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import com.example.plusgo.R;
import com.example.plusgo.UPM.AddPreferenceActivity;
import com.example.plusgo.UPM.NewUserActivity;

public class NotificationHelper  extends AppCompatActivity {

    //Display Notification
    public static void displayNotification(Context context, String title, String body){

        Intent intent = new Intent(context, AddPreferenceActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                100,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );

        //if Accept button click Redirect to the YesActivity
        Intent YesIntent = new Intent(context,YesActivity.class);
        YesIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent YesPendingIntent = PendingIntent.getActivity(context,0,YesIntent,PendingIntent.FLAG_ONE_SHOT);

        //if Decline button click Redirect to the YesActivity
        Intent NoIntent = new Intent(context,NoActivity.class);
        NoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent NoPendingIntent = PendingIntent.getActivity(context,0,NoIntent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mbuilder =
                new NotificationCompat.Builder(context, NewUserActivity.CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_note)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_MAX);
        //Set Notification Sound
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mbuilder.setSound(alarmSound);

        mbuilder.addAction(R.drawable.ic_accept,"Accept",YesPendingIntent);
        mbuilder.addAction(R.drawable.ic_decline,"Decline",NoPendingIntent);

        //Set Expandable Notification
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.car);
        mbuilder.setLargeIcon(bitmap);
        mbuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null));



        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1,mbuilder.build());

    }
}