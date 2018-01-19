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

public class MainActivity extends AppCompatActivity {

    TimePicker timePicker;
    AlarmManager alarmManager, alarmManagerHourly;
    Calendar calendar;
    Intent intent, intentHourly;
    PendingIntent pendingIntent, pendingIntentHourly;
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.set_alarm_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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


        switch (item.getItemId()) {
            case R.id.set_alarm:
                //Creating Pending Intent for delaying the till the specified time
                if(intent != null){
                    //put extra string to let the receiver and service know that alarm needs to turn on
                    intent.putExtra("extra", "alarm_on");
                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,
                            intent, PendingIntent.FLAG_UPDATE_CURRENT);
                }
                //set the alarm manager for the created pending intent
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                Toast.makeText(MainActivity.this, "Alarm is set to " + hour+":"+minute+" "+day, Toast.LENGTH_LONG).show();
                return true;

            case R.id.cancel_alarm:
                if(pendingIntent != null) {
                    //cancel the pending alarm intent
                    alarmManager.cancel(pendingIntent);
                    //put extra string to let the service know that Alarm needs to turn off
                    intent.putExtra("extra", "alarm_off");

                    sendBroadcast(intent);
                }
                Toast.makeText(MainActivity.this, getString(R.string.alarm_cancel_message), Toast.LENGTH_LONG).show();
                return true;

            default:
                Toast.makeText(MainActivity.this, getString(R.string.alarm_default_message), Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
        }
    }
    public void onCheckboxClicked(View view) {
        //get the current status of the checkbox
        boolean isChecked = ((CheckBox) view).isChecked();

        if (isChecked) {
            Log.d("PendingIntent", "Created for hourly alarm");
            intentHourly.putExtra("hourly", "time");
            pendingIntentHourly = PendingIntent.getBroadcast(MainActivity.this,
                    1, intentHourly, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManagerHourly.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_HOUR, pendingIntentHourly);
        } else {
            alarmManagerHourly.cancel(pendingIntentHourly);
        }
    }
}
