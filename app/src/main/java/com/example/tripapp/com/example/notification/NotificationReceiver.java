package com.example.tripapp.com.example.notification;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.tripapp.HomeActivity;
import com.example.tripapp.R;
import com.example.tripapp.Trips;

public class NotificationReceiver extends IntentService {

    Trips trp ;
    double startLat;
    double startLong;
    double endLat;
    double endLong;
    String tripStart;
    String tripEnd;
    public NotificationReceiver() {
        super("NotificationReceiver");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        startLat = intent.getExtras().getDouble("startLat");

        startLong = intent.getExtras().getDouble("startLong");

        endLat = intent.getExtras().getDouble("endLat");

        endLong = intent.getExtras().getDouble("endLong");


        tripStart = intent.getExtras().getString("TripStart");

        tripEnd = intent.getExtras().getString("TripEnd");

        notificationActions();
    }

    private void notificationActions () {


        int NOTIFICATION_ID = 1;


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background));
        builder.setContentTitle("Trip App");
        builder.setContentText("You have new trip from "+ tripStart +" to "+tripEnd);
        builder.setAutoCancel(true);

        Intent in=new Intent(this, HomeActivity.class);
        PendingIntent launchIntent = PendingIntent.getActivity(this, 0, in, 0);

//
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" +startLat+","+startLong+"&daddr="+endLat+","+endLong));


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent buttonIntent = new Intent(getBaseContext(), NotificationReceiver.class);
        buttonIntent.putExtra("notificationId", NOTIFICATION_ID);
        PendingIntent dismissIntent = PendingIntent.getBroadcast(getBaseContext(), 0, buttonIntent, 0);

        builder.setContentIntent(launchIntent);
        builder.addAction(android.R.drawable.ic_menu_view, "Start", pendingIntent);
        builder.addAction(android.R.drawable.ic_delete, "Cancel", dismissIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Will display the notification in the notification bar
        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }

}
