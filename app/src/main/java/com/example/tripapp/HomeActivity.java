package com.example.tripapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements LocationListener {

    ListView listView;
    ArrayList<Trips> trips=new ArrayList<>();

    Button startTrip ;
    Trips tripss;

    //vars
    LocationManager manager;
    double longg,lat;
    String userID;
    ListViewAdapter listViewAdapter;



    private static String APPID = "E250A2F2-A4E3-ABBF-FF4B-5AB19D36B100";
    private static String APIKEY = "1392D548-4976-417C-95F0-9B08710FFAE3";
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        listView = findViewById(R.id.list);
        startTrip = findViewById(R.id.startTripMap);

        Backendless.initApp(this, APPID, APIKEY);


        userID = getIntent().getStringExtra("userID");

        //location
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String []permisions= {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this,permisions,1);
        }
        else{
            manager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
        }

//        DataQueryBuilder dq = DataQueryBuilder.create();
//        String where="ownerId='"+userID+"'";
//        dq.setWhereClause(where);

        Backendless.Data.of(Trips.class).find(new AsyncCallback<List<Trips>>() {
            @Override
            public void handleResponse(List<Trips> response) {
                for (int i = 0; i < response.size(); i++) {

                    trips.add(response.get(i));


                    tripss = new Trips();

                    //current user
//                    tripss.setOwnerId(Backendless.UserService.CurrentUser().getUserId());

                    tripss.setObjectId(response.get(i).getObjectId());
                    tripss.setTripName(response.get(i).getTripName());
                    tripss.setTripStart(response.get(i).getTripStart());
                    tripss.setTripEnd(response.get(i).getTripEnd());
                    tripss.setTripNotes(response.get(i).getTripNotes());

                    tripss.setsLat(response.get(i).getsLat());
                    tripss.setsLong(response.get(i).getsLong());
                    tripss.seteLat(response.get(i).geteLat());
                    tripss.seteLong(response.get(i).geteLong());



                }

                ListViewAdapter listViewAdapter = new ListViewAdapter(HomeActivity.this,trips);
                listView.setAdapter(listViewAdapter);
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });


    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        DataQueryBuilder dq = DataQueryBuilder.create();
//        String where="ownerId='"+userID+"'";
//        dq.setWhereClause(where);
//
//        Backendless.Data.of(Trips.class).find(dq,new AsyncCallback<List<Trips>>() {
//            @Override
//            public void handleResponse(List<Trips> response) {
//                for (int i = 0; i < response.size(); i++) {
//
//                    trips.add(response.get(i));
//
//
//                    tripss = new Trips();
//
//                    //current user
//                    tripss.setOwnerId(Backendless.UserService.CurrentUser().getUserId());
//
//                    tripss.setObjectId(response.get(i).getObjectId());
//                    tripss.setTripName(response.get(i).getTripName());
//                    tripss.setTripStart(response.get(i).getTripStart());
//                    tripss.setTripEnd(response.get(i).getTripEnd());
//                    tripss.setTripNotes(response.get(i).getTripNotes());
//
//                    tripss.setsLat(response.get(i).getsLat());
//                    tripss.setsLong(response.get(i).getsLong());
//                    tripss.seteLat(response.get(i).geteLat());
//                    tripss.seteLong(response.get(i).geteLong());
//
//
//
//                }
//
//                listViewAdapter = new ListViewAdapter(HomeActivity.this,trips);
//                listView.setAdapter(listViewAdapter);
//            }
//
//            @Override
//            public void handleFault(BackendlessFault fault) {
//
//            }
//        });
//    }

    //adding trip
    public void add(View view) {

        Intent in = new Intent(this,TripActivity.class);
        startActivity(in);

//        Toast.makeText(this, "add trip", Toast.LENGTH_SHORT).show();
    }

//    public void startTripOnMap(View view) {
//
//        double sg = tripss.getsLong();
//        double sl = tripss.getsLat();
//        double el = tripss.geteLat();
//        double eg = tripss.geteLong();
//
//        startLocation = new Location("");
//        startLocation.setLatitude(sLat);
//        startLocation.setLongitude(sLong);
//        distance=myLocation.distanceTo(startLocation);
//
//
//        if(lat>0 && longg>0){
//            myLocation.setLatitude(lat);
//            myLocation.setLongitude(longg);
//            Toast.makeText(this, "index "+distance, Toast.LENGTH_SHORT).show();
//
//        }
//        else
//            Toast.makeText(this, "Zero", Toast.LENGTH_SHORT).show();
//
//        Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" +lat+","+longg+"&daddr="+sl+","+sg));
//        startActivity(i);
//
//    }

    @Override
    public void onLocationChanged(Location location) {

//        Toast.makeText(this, "got loc", Toast.LENGTH_SHORT).show();
//
//
//        lat = location.getLatitude();
//        longg=location.getLongitude();
////        Log.i("lat", String.valueOf(lat));
//        Toast.makeText(this, ""+lat, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, ""+longg, Toast.LENGTH_SHORT).show();
////        Log.i("long", String.valueOf(longg));
//
//        Geocoder geocoder=new Geocoder(this);
//        try {
//            List<Address> addresses = geocoder.getFromLocation(lat, longg, 1);
//            Log.i("address",addresses.get(0).getAddressLine(0));
//            Toast.makeText(this, ""+addresses.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }




    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

//    public void ln(View view) {
//
//        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
//
//    }

//    public void showDetails(View view) {
//        Intent in = new Intent(this, EditTrip.class);
//        startActivity(in);
//        Toast.makeText(this, "data shown here", Toast.LENGTH_SHORT).show();
//
//    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//        t =  trips.get(position);
//        Log.d("object id",t.getObjectId());
//        Toast.makeText(this, ""+t.getObjectId(), Toast.LENGTH_SHORT).show();
//        Intent in = new Intent(this, EditTrip.class);
//        startActivity(in);
//    }
}
