package com.example.helper;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class Main2Activity extends AppCompatActivity{

    TextView textView_time, textView_activity, textView_mode;
    RadioGroup radioGroup_mode;
    RadioButton radioButton_normal, radioButton_walk;
    Switch switch1;
    Calendar calendar;
    AlarmManager alarm_manager;
    PendingIntent pending_intent;
    String text_time, radiobuttonchecked;
    int alarmset_hour, alarmset_minute;
    SharedPreferences sharedPreferences;

    // Visibility of all the UI elements except fab is set to invisible by default
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialising variables
        switch1 = (Switch)findViewById(R.id.switch1);
        textView_time = (TextView)findViewById(R.id.textView_time);
        textView_activity = (TextView)findViewById(R.id.textView_activity);
        textView_mode = (TextView)findViewById(R.id.textView_mode);
        radioButton_normal = (RadioButton)findViewById(R.id.radioButton_normal);
        radioButton_walk = (RadioButton)findViewById(R.id.radioButton_walk);
        radioGroup_mode = (RadioGroup)findViewById(R.id.radioGroup_mode);
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        // Setting the text to be displayed when the app is opened
        textView_activity.setVisibility(View.VISIBLE);
        textView_activity.setText("Click the ' + ' button to create a new alarm");

        calendar = Calendar.getInstance();
        final Intent my_intent = new Intent(Main2Activity.this, AlarmReceiver.class);
        // Shared preferences is used to store the radio button which is checked & the time for which the alarm is set
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        // The radio button which is checked, is retrieved from shared preferences
        radiobuttonchecked = sharedPreferences.getString("RadioButtonChecked","Normal");

        //Listener for switch
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // If switch is OFF, then turn off the alarm
                if(!isChecked){
                    textView_activity.setText("Alarm Turned Off By Sliding The Switch!");
                    // cancel the alarm
                    alarm_manager.cancel(pending_intent);
                    // Clearing the screen (i.e.) Setting the visibility of all UI elements except fab to invisible
                    textView_time.setVisibility(View.INVISIBLE);
                    switch1.setVisibility(View.INVISIBLE);
                    textView_activity.setVisibility(View.INVISIBLE);
                    radioGroup_mode.setVisibility(View.INVISIBLE);
                    // Enabling fab so that next alarm can be se
                    fab.setEnabled(true);
                    // Setting the text to be displayed when the app is opened
                    textView_activity.setVisibility(View.VISIBLE);
                    textView_activity.setText("Click the ' + ' button to create a new alarm");
                    // Displaying "Alarm Deactivated" Toast
                    Toast.makeText(Main2Activity.this, "Alarm Deactivated!", Toast.LENGTH_LONG).show();
                    // Put extra string into my_intent which tells the clock that alarm must be switched off
                    my_intent.putExtra("Action", "Alarm OFF");
                    // Stop the ringtone
                    sendBroadcast(my_intent);
                } else{
                    // Switch is ON --> Set the visibility of UI elements to visible
                    textView_time.setVisibility(View.VISIBLE);
                    switch1.setVisibility(View.VISIBLE);
                    textView_activity.setVisibility(View.VISIBLE);
                    radioGroup_mode.setVisibility(View.VISIBLE);
                    textView_activity.setText("Alarm Set!");
                    // Displaying "Alarm Deactivated" Toast
                    Toast.makeText(Main2Activity.this, "Alarm Set!", Toast.LENGTH_LONG).show();
                    // Put extra string into my_intent which tells the clock that alarm must be switched on
                    my_intent.putExtra("Action", "Alarm ON");

                    // Create pending intent
                    pending_intent = PendingIntent.getBroadcast(Main2Activity.this, 0,
                            my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    // Set the alarm manager
                    alarm_manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            pending_intent);
                }
            }
        });

        // Listener for radio group
        radioGroup_mode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == radioButton_normal.getId()){
                    radiobuttonchecked = "Normal";
                    //Log.d("RadioButtonChecked", "Normal");
                } else {
                    radiobuttonchecked = "Walk";
                    //Log.d("RadioButtonChecked", "Walk");
                }
                // Saving the radio button which is checked as a string in shared preferences
                editor.putString("RadioButtonChecked",radiobuttonchecked);
                editor.apply();
            }
        });

        // Getting intent from ActivityRecognizer.java
        // The intent sends the activity recognised as an int
        Intent intent_activity = getIntent();
        int act_num = intent_activity.getIntExtra("RecognisedActivity", -1);
        // The activity refreshes once this intent is received
        // So, the time text and the checked radio button are set again

        // Time of alarm is retrieved from shared preferences and the textview is set to this value
        String text1 = sharedPreferences.getString("TextTime", "7:00 AM");
        textView_time.setText(text1);
        // The checked radio button is set again
        if(radiobuttonchecked != null) {
            if (radiobuttonchecked.equals("Walk")) {
                radioButton_walk.setChecked(true);
                //Log.d("RadioButton", "Walk Checked");
            } else {
                radioButton_normal.setChecked(true);
                //Log.d("RadioButton", "Normal Checked");
            }
        }
        // act_num = -2 only before receiving the intent. So, act_num != -2 means an activity is recognised
        if(act_num != -1){
            // Alarm is ringing. UI elements are set to visible
            textView_time.setVisibility(View.VISIBLE);
            switch1.setVisibility(View.VISIBLE);
            textView_activity.setVisibility(View.VISIBLE);
            radioGroup_mode.setVisibility(View.VISIBLE);
            switch1.setChecked(true);
            // Switch is disabled if walk mode is not selected
            if(radioButton_walk.isChecked()) {
                switch1.setEnabled(false);
            }
            // Radio buttons and fab are disabled
            fab.setEnabled(false);
            radioGroup_mode.setEnabled(false);
            radioButton_walk.setEnabled(false);
            radioButton_normal.setEnabled(false);
        }
        // Activity Recognition API gives a value 2 if the activity is ON_FOOT which encompasses WALKING and RUNNING
        // So, if act_num = 2, then alarm must turn off
        if(act_num == 1){
            textView_activity.setText("Alarm Turned Off By Walking!");
            // Cancel the alarm
            alarm_manager.cancel(pending_intent);
            // Clearing the screen (i.e.) Setting the visibility of all UI elements except fab to invisible
            textView_time.setVisibility(View.INVISIBLE);
            switch1.setVisibility(View.INVISIBLE);
            textView_activity.setVisibility(View.INVISIBLE);
            radioGroup_mode.setVisibility(View.INVISIBLE);
            // Setting the text to be displayed when the app is opened
            textView_activity.setVisibility(View.VISIBLE);
            textView_activity.setText("Click the ' + ' button to create a new alarm");
            // Displaying "Alarm Deactivated" Toast
            Toast.makeText(Main2Activity.this, "Alarm Deactivated!", Toast.LENGTH_LONG).show();
            // Enabling fab so that next alarm can be set
            fab.setEnabled(true);
            // Put extra string into my_intent which tells the clock that alarm must be switched off
            my_intent.putExtra("Action", "Alarm OFF");
            // Stop the ringtone
            sendBroadcast(my_intent);
        }

        // Floating Action Button is used to create an alarm or edit an existing one
        fab.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                // Getting the current time
                // This is done to set the the time in time picker to current time when the time picker dialog is displayed
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                // Creating a time picker dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(Main2Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Initialising the calendar variable with the time picked from time picker
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        alarmset_hour = hourOfDay;
                        alarmset_minute = minute;
                        String hour_string = String.valueOf(hourOfDay);
                        String minute_string = String.valueOf(minute);
                        // hourOfDay is in 24 hour format. So, it can have integral values from 0 to 23
                        // We are converting it to 12 hour format
                        if (hourOfDay > 12) {
                            hour_string = String.valueOf(hourOfDay - 12);
                        } else if (hourOfDay == 0){
                            hour_string = "12";
                        }
                        // If minute is less than 10, contains only single digit.
                        if (minute < 10) {
                            // (e.g.) 10:7 --> 10:07
                            minute_string = "0" + String.valueOf(minute);
                        }
                        // Deciding AM or PM
                        String am_pm = (hourOfDay < 12) ? "AM" : "PM";
                        text_time = hour_string + ":" + minute_string + " " + am_pm;
                        // Setting the time for which the alarm is set in the text view
                        textView_time.setText(text_time);
                        // Saving the time for which alarm is set in shared preferences
                        editor.putString("TextTime",text_time);
                        editor.apply();
                        // Switch turns on as alarm is set
                        switch1.setChecked(true);

                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        // This app won't exit when back key is pressed. So, a toast is displayed to state this message
        Toast.makeText(Main2Activity.this, "This app does not exit when back button is pressed!", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected void onDestroy() {
        // Shared preferences are removed
        sharedPreferences.edit().remove("MyPreferences").apply();
        SharedPreferences preferences = getSharedPreferences("CountPref", 0);
        preferences.edit().remove("count").apply();
        super.onDestroy();
    }
}


