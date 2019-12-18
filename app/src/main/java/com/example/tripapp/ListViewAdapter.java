package com.example.tripapp;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class ListViewAdapter  extends BaseAdapter  {


//    Trips tripss;
//    double longg,lat;
    //------------------
    ArrayList<Trips> list;
    Context context;
    String objectId,TripName,TripStart,TripEnd,TripNotes;
    double getsLat,getsLong,geteLat,geteLong;

    public ListViewAdapter(Context context , ArrayList<Trips> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.trips_layout,parent,false);
        TextView textView = convertView.findViewById(R.id.tripName);
        ImageView imageView = convertView.findViewById(R.id.startTripMap);
        textView.setText(list.get(position).getTripName());


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                objectId = list.get(position).getObjectId();
                Toast.makeText(context, objectId, Toast.LENGTH_SHORT).show();

                TripName = list.get(position).getTripName();
                TripStart = list.get(position).getTripStart();
                TripEnd = list.get(position).getTripEnd();
                TripNotes = list.get(position).getTripNotes();

                Intent i = new Intent(context,EditTrip.class);
                i.putExtra("objectID",objectId);
                i.putExtra("tripName",TripName);
                i.putExtra("tripStart",TripStart);
                i.putExtra("tripEnd",TripEnd);
                i.putExtra("tripNotes",TripNotes);


                context.startActivity(i);
            }
        });


        //img
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                objectId = list.get(position).getObjectId();

                Toast.makeText(context, objectId, Toast.LENGTH_SHORT).show();


                getsLat = list.get(position).getsLat();
                getsLong = list.get(position).getsLong();
                geteLat = list.get(position).geteLat();
                geteLong = list.get(position).geteLong();


                Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" +getsLat+","+getsLong+"&daddr="+geteLat+","+geteLong));
                context.startActivity(i);

            }
        });

        return convertView;
    }

//    @Override
//    public void onLocationChanged(Location location) {
//
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//
//    }
}
