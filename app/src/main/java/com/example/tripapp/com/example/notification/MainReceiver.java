package com.example.tripapp.com.example.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MainReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        double startLat = intent.getExtras().getDouble("startLat");
        double startLong = intent.getExtras().getDouble("startLong");
        double endLat = intent.getExtras().getDouble("endLat");
        double endLong = intent.getExtras().getDouble("endLong");
        String tripStrat = intent.getExtras().getString("TripStart");
        String tripEnd = intent.getExtras().getString("TripEnd");
//        int pid = intent.getExtras().getInt("pid");
        Toast.makeText(context, "alarm receiver", Toast.LENGTH_SHORT).show();
        Intent in = new Intent(context,NotificationReceiver.class);

        in.putExtra("startLat",startLat);
        in.putExtra("startLong",startLong);
        in.putExtra("endLat",endLat);
        in.putExtra("endLong",endLong);
        in.putExtra("TripStart",tripStrat);
        in.putExtra("TripEnd",tripEnd);
//        in.putExtra("pid",pid);
        context.startService(in);




    }
}
