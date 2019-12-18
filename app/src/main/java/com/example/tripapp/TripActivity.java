package com.example.tripapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.tripapp.com.example.notification.MainReceiver;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class TripActivity extends AppCompatActivity implements View.OnClickListener , AsyncCallback<Trips> , LocationListener {


    int Year,Month,Day,Hour,Min ;
    Date date ,checkDate ;
    int hours,min;
    EditText tripName;
    AutoCompleteTextView tripEnd , tripStart;
    ImageView TimePicker, DatePicker;
    TextView DateTxt ,TimeTxt,showNotes;
    Button saveTrip;

    Geocoder g ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        tripName = findViewById(R.id.tripName);
        tripEnd = findViewById(R.id.tripEnd);
        tripStart = findViewById(R.id.tripStart);
        DatePicker = findViewById(R.id.date_id);
        TimePicker = findViewById(R.id.time_id);
        DateTxt = findViewById(R.id.date_shown);
        TimeTxt = findViewById(R.id.time_shown);
        DatePicker.setOnClickListener(this);
        TimePicker.setOnClickListener(this);
        showNotes = findViewById(R.id.showNotes);
        saveTrip = findViewById(R.id.saveTrip);



        String[] countries = getResources().getStringArray(R.array.countries);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,countries);
        tripEnd.setAdapter(adapter);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,countries);
        tripStart.setAdapter(adapter2);

        g = new Geocoder(this);

        //save trip to DB
        saveTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Trips trips = new Trips();


//                String user = getIntent().getStringExtra("userID");
//                trips.setObjectId(user);
//                String userID =  getIntent().getStringExtra("userID");

                trips.setTripName(tripName.getText().toString());
                trips.setTripStart(tripStart.getText().toString());
                trips.setTripEnd(tripEnd.getText().toString());

                trips.setOwnerId(Backendless.UserService.CurrentUser().getUserId());

                System.out.println(Backendless.UserService.CurrentUser().getUserId());
                trips.setTripStatus("Upcoming");
                trips.setTripDateTime(DateTxt.getText().toString() + "|" + TimeTxt.getText().toString());


                ExampleDialog dialog = new ExampleDialog();
                final StringBuilder allNotes = dialog.saveNotes();
                trips.setTripNotes(allNotes.toString());

                //try to convert from address to lat long

                try {

                    List<Address> startFromLocationName = g.getFromLocationName(tripStart.getText().toString(), 1);
                    List<Address> endFromLocationName = g.getFromLocationName(tripEnd.getText().toString(), 1);

                    if(startFromLocationName.isEmpty() || endFromLocationName.isEmpty()){
                        Toast.makeText(TripActivity.this, "enter valid location", Toast.LENGTH_SHORT).show();
                        return;
                    }

                        System.out.println(" LatLng " + startFromLocationName.get(0).getLatitude());

                        trips.setsLat(startFromLocationName.get(0).getLatitude());
                        trips.setsLong(startFromLocationName.get(0).getLongitude());
                        trips.seteLat(endFromLocationName.get(0).getLatitude());
                        trips.seteLong(endFromLocationName.get(0).getLongitude());


                } catch (IOException e) {
                    e.printStackTrace();
                }



               // notification manager


                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                Intent intent = new Intent(TripActivity.this, MainReceiver.class);
                intent.putExtra("startLat",trips.getsLat());
                intent.putExtra("startLong",trips.getsLong());
                intent.putExtra("endLat",trips.geteLat());
                intent.putExtra("endLong",trips.geteLong());
                intent.putExtra("TripStart",trips.getTripStart());
                intent.putExtra("TripEnd",trips.getTripEnd());


//                 int pid = (int) System.currentTimeMillis();
//                 intent.putExtra("pid",pid);
                 PendingIntent pendingIntent = PendingIntent.getBroadcast(TripActivity.this, 0, intent, 0);

                Calendar calendar = Calendar.getInstance();

                calendar.set(Calendar.HOUR_OF_DAY,hours);
                calendar.set(Calendar.MINUTE,min);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.DATE,Day);
                calendar.set(Calendar.MONTH,Month);
                calendar.set(Calendar.YEAR,Year);

                alarmManager.set(AlarmManager.RTC,calendar.getTimeInMillis() ,pendingIntent);

         //end of notification

                //save trip to Database
                Backendless.Persistence.save(trips, new AsyncCallback<Trips>() {
                    @Override
                    public void handleResponse(Trips response) {
                        if(tripName.getText().toString().isEmpty() || tripStart.getText().toString().isEmpty() ||
                                tripEnd.getText().toString().isEmpty() ||
                                allNotes.toString().isEmpty() ||
                                DateTxt.getText().toString().isEmpty() || TimeTxt.getText().toString().isEmpty()){
                            Toast.makeText(TripActivity.this, "fields is empty!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(TripActivity.this, "trip saved successfully", Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(TripActivity.this, HomeActivity.class);

                            startActivity(in);
                        }
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(TripActivity.this, fault.getMessage()+"", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        findViewById(R.id.customDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }


    public void openDialog(){
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }


    //time picker
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    //date picker
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    //validate time and date
    @Override
    public void onClick(View v) {

        if (v == DatePicker) {
            final Calendar c = Calendar.getInstance();
            Year = c.get(Calendar.YEAR);
            Month = c.get(Calendar.MONTH);
            Day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            String myDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                            java.text.DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                            try {
                                date = format.parse(timeStamp);
                                checkDate = format.parse(myDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if(checkDate.equals(null)) {
                                try {
                                    checkDate = format.parse(timeStamp);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                if (checkDate.before(date)) {
                                    Toast.makeText(view.getContext(), "Enter Valid Date", Toast.LENGTH_SHORT).show();
                                } else {
                                    DateTxt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                }
                            }
                        }
                    }, Year, Month, Day);
            datePickerDialog.show();
        }
        if (v == TimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            Hour = c.get(Calendar.HOUR_OF_DAY);
            Min = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            Calendar myCalInstance = Calendar.getInstance();
                            Calendar myRealCalender = Calendar.getInstance();
                            if(checkDate==null){
                                String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                                java.text.DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                                try {
                                    checkDate = format.parse(timeStamp);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            myRealCalender.setTime(checkDate);
                            myRealCalender.set(Calendar.HOUR_OF_DAY,hourOfDay);
                            myRealCalender.set(Calendar.MINUTE,minute);
                            Log.i("time", String.valueOf(myCalInstance.getTime()));
                            Log.i("time", String.valueOf(myRealCalender.getTime()));
                            if((myRealCalender.getTime()).before(myCalInstance.getTime()))
                            {
                                Toast.makeText(view.getContext(),"Enter Valid Time",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                hours = hourOfDay;
                                min = minute;
                                if(hourOfDay<10&&min>=10) {
                                    TimeTxt.setText("0" + hourOfDay + ":" + minute);
                                }
                                if(hourOfDay<10&&min<10)
                                {
                                    TimeTxt.setText("0" + hourOfDay + ":"+"0"+ minute);
                                }
                                if(hourOfDay>=10&&min<10)
                                {
                                    TimeTxt.setText(hourOfDay + ":"+"0"+ minute);
                                }
                                if(hourOfDay>=10&&min>=10)
                                {
                                    TimeTxt.setText(hourOfDay + ":"+ minute);
                                }

                            }
                        }
                    }, Hour, Min, false);
            timePickerDialog.show();
        }
    }

    @Override
    public void handleResponse(Trips response) {

    }

    @Override
    public void handleFault(BackendlessFault fault) {

    }

    // time picker
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
        }
    }

    // date picker
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
        }
    }

    @Override
    public void onLocationChanged(Location location) {


         g=new Geocoder(this);


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


}



