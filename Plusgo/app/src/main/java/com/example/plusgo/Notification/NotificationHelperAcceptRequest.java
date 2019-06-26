package com.example.plusgo.Notification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.plusgo.FC.MapCurrentPassengerActivity;
import com.example.plusgo.FC.PassengerCurrentTrip;
import com.example.plusgo.OPR.MainActivity;
import com.example.plusgo.R;
import com.example.plusgo.SignUp;

public class NotificationHelperAcceptRequest   extends AppCompatActivity {



    //Display Notification
    public static void displayNotification(Context context, String title, String body){


        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                100,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );




        //if Accept button click Redirect to the YesActivity
        Intent YesIntent = new Intent(context,YesActivity.class);
        YesIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //notification message will get at NotificationView
        YesIntent.putExtra("message", "This is a notification message");
        PendingIntent YesPendingIntent = PendingIntent.getActivity(context,0,YesIntent,PendingIntent.FLAG_ONE_SHOT);

        //if Decline button click Redirect to the YesActivity
        Intent NoIntent = new Intent(context,NoActivity.class);
        NoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent NoPendingIntent = PendingIntent.getActivity(context,0,NoIntent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mbuilder =
                new NotificationCompat.Builder(context, SignUp.CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_note)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_MAX);
        //Set Notification Sound
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mbuilder.setSound(alarmSound);

//        mbuilder.addAction(R.drawable.ic_accept,"Accept",YesPendingIntent);
//        mbuilder.addAction(R.drawable.ic_decline,"Decline",NoPendingIntent);

        //Set Expandable Notification
        /*Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.car);
        mbuilder.setLargeIcon(bitmap);
        mbuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null));*/

        //Assign BigText style notification
//        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
//        bigText.bigText(MyFirebaseMessagingService.NotificationBodyCatcher.toString());
//        bigText.setSummaryText(MyFirebaseMessagingService.NotificationBodyCatcher.toString());
//        mbuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(null));



        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1,mbuilder.build());

    }



}