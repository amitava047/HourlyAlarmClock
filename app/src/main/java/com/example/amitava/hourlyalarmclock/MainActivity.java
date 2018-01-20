package com.example.amitava.hourlyalarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/*
* This class will basically call the tasks and services
* for the basic alarm and the hourly reminder alarm, as
* scheduled and required.
* */
public class MainActivity extends AppCompatActivity {

    //Initialization of the objects that are required for handling the
    //Alarm scheduling
    TimePicker timePicker;
    AlarmManager alarmManager, alarmManagerHourly;
    Calendar calendar;
    //Intents for passing messages to other services as required
    Intent intent, intentHourly;
    //PendingIntent for delaying the message passing as per scheduled time
    PendingIntent pendingIntent, pendingIntentHourly;
    //Checkbox object for recognizing the Checkox in the MainActivity
    CheckBox checkBox;

    int alarmHour;
    int alarmMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        timePicker = findViewById(R.id.timePicker);
        calendar = Calendar.getInstance();

        alarmManagerHourly = (AlarmManager) getSystemService(ALARM_SERVICE);

        checkBox = (CheckBox) findViewById(R.id.hourly_alarm_check);

        //Creating an Intent for the Alarm Receiver class
        intent = new Intent(MainActivity.this, AlarmReceiver.class);

        //Creating an Intent for the Hourly Alarm
        intentHourly = new Intent(MainActivity.this, AlarmReceiver.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //This will inflate the menu in the MainActivity
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.set_alarm_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //For converting the time returned by the TimePicker from 24 hour format to 12 hour format
        alarmHour = timePicker.getHour();
        alarmMinute = timePicker.getMinute();
        String hour = String.valueOf(alarmHour);
        String minute = String.valueOf(alarmMinute);
        String day = "am";

        if(alarmHour > 12){
            alarmHour = alarmHour - 12;
            hour = String.valueOf(alarmHour);
            day = "pm";
        }else if(alarmHour == 0){
            hour = "12";
        }
        if(alarmMinute < 10){
            minute = "0" + String.valueOf(alarmMinute);
        }

        //The switch case for passing the intents as per the option clicked
        switch (item.getItemId()) {
            //The "Set Alarm" menu is clicked
            case R.id.set_alarm:
                //Creating Pending Intent for delaying the till the specified time
                if(intent != null){
                    //put extra string to let the receiver and service know that alarm needs to turn on
                    intent.putExtra("extra", "alarm_on");
                    //Pending Intent will get sent to the Broadcast Receiver
                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,
                            intent, PendingIntent.FLAG_UPDATE_CURRENT);
                }
                //set the alarm manager for the created pending intent
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                //Display a message to the user with the information of the scheduled alarm
                Toast.makeText(MainActivity.this, "Alarm is set to " + hour+":"+minute+" "+day, Toast.LENGTH_LONG).show();
                return true;

            //The "Cancel Alarm" menu is clicked
            case R.id.cancel_alarm:
                //For avoiding the application crashing, first checking if the object is null or not
                if(pendingIntent != null) {
                    //cancel the pending alarm intent
                    alarmManager.cancel(pendingIntent);
                    //put extra string to let the service know that Alarm needs to turn off
                    intent.putExtra("extra", "alarm_off");
                    //The intent will be sent to the Broadcast Receiver
                    sendBroadcast(intent);
                }
                //Displaying a message to the user that the alarm is cancelled
                Toast.makeText(MainActivity.this, getString(R.string.alarm_cancel_message), Toast.LENGTH_LONG).show();
                return true;

            default:
                Toast.makeText(MainActivity.this, getString(R.string.alarm_default_message), Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
        }
    }
    //This function will be called when the checkbox is clicked
    public void onCheckboxClicked(View view) {
        //get the current status of the checkbox
        boolean isChecked = ((CheckBox) view).isChecked();
        //When the checkbox will be checked
        if (isChecked) {
            Log.d("PendingIntent", "Created for hourly alarm");
            //Initializing the intent and the PendingIntent for calling the alarm service
            intentHourly.putExtra("hourly", "time");
            pendingIntentHourly = PendingIntent.getBroadcast(MainActivity.this,
                    1, intentHourly, PendingIntent.FLAG_UPDATE_CURRENT);
            //Creating an hourly repeating alarm from the time it's initialized
            alarmManagerHourly.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_HOUR, pendingIntentHourly);
        } else {
            //When the checkbox will be unchecked
            alarmManagerHourly.cancel(pendingIntentHourly);
        }
    }
}
