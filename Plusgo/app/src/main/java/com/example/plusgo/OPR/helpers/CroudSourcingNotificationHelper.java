package com.example.plusgo.OPR.helpers;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CroudSourcingNotificationHelper {

    private static final String ONLINE_DRIVERS = "drivers";

    private DatabaseReference onlineDriversDatabaseReference;

    public CroudSourcingNotificationHelper() {
        onlineDriversDatabaseReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(ONLINE_DRIVERS);
    }

    public DatabaseReference getOnlineDriversDatabaseReference() {
        return onlineDriversDatabaseReference;
    }

}
