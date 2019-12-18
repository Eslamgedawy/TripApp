package com.example.tripapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditTrip extends AppCompatActivity implements View.OnClickListener  {


    int Year,Month,Day,Hour,Min ;
    Date date ,checkDate ;
    int hours,min;
    EditText tripName;
    AutoCompleteTextView tripEnd , tripStart;
    ImageView TimePicker, DatePicker;
    TextView DateTxt ,TimeTxt,tripNotes;
    Button editTrip ,deleteTrip;
    Trips trips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_edit);

        tripName = findViewById(R.id.tripName);
        tripEnd = findViewById(R.id.tripEnd);
        tripStart = findViewById(R.id.tripStart);
        DatePicker = findViewById(R.id.date_id);
        TimePicker = findViewById(R.id.time_id);
        DateTxt = findViewById(R.id.date_shown);
        TimeTxt = findViewById(R.id.time_shown);
        DatePicker.setOnClickListener(this);
        TimePicker.setOnClickListener(this);
        tripNotes = findViewById(R.id.tripNotes);
        editTrip = findViewById(R.id.saveTrip);
        deleteTrip = findViewById(R.id.delete);



        String[] countries = getResources().getStringArray(R.array.countries);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,countries);
        tripEnd.setAdapter(adapter);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,countries);
        tripStart.setAdapter(adapter2);



        trips = new Trips();
        final String obj = getIntent().getStringExtra("objectID");
        String tname = getIntent().getStringExtra("tripName");
        String tstart = getIntent().getStringExtra("tripStart");
        String tend = getIntent().getStringExtra("tripEnd");
        String notes = getIntent().getStringExtra("tripNotes");

        tripName.setText(tname);
        tripStart.setText(tstart);
        tripEnd.setText(tend);
        tripNotes.setText(notes);


        //save trip to DB
        editTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> changes = new HashMap<>();
                changes.put( "tripName",tripName.getText().toString());
                changes.put( "tripStart",tripStart.getText().toString());
                changes.put( "tripEnd",tripEnd.getText().toString());
                changes.put( "tripNotes",tripNotes.getText().toString());
                String d = DateTxt.getText().toString() + "|" + TimeTxt.getText().toString();
                changes.put("tripDateTime",d);


                Backendless.Data.of( "Trips" ).update( "objectID = '"+obj+"'", changes, new AsyncCallback<Integer>()
                {
                    @Override
                    public void handleResponse( Integer objectsUpdated )
                    {
//                        Log.i( "MYAPP", "Server has updated " + objectsUpdated + " objects" );
                        Toast.makeText(EditTrip.this, objectsUpdated+"record has updated successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(EditTrip.this,HomeActivity.class);
                        startActivity(i);
                    }

                    @Override
                    public void handleFault( BackendlessFault fault )
                    {

                        Toast.makeText(EditTrip.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } );
            }
        });


        //delete a trip
        deleteTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Backendless.Data.of( "Trips" ).remove( "objectID = '"+obj+"'", new AsyncCallback<Integer>()
                {
                    @Override
                    public void handleResponse( Integer objectsDeleted )
                    {

                        Toast.makeText(EditTrip.this, objectsDeleted+"record has deleted successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(EditTrip.this,HomeActivity.class);
                        startActivity(i);
                    }

                    @Override
                    public void handleFault( BackendlessFault fault )
                    {
                        Toast.makeText(EditTrip.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } );
            }
        });




    }


    public void openDialog(){
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


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


}



