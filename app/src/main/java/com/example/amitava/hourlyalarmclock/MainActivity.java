package com.example.amitava.hourlyalarmclock;

import android.app.AlarmManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TimePicker timePicker;
    AlarmManager alarmManager;
    Calendar calendar;

    int alarmHour;
    int alarmMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        timePicker = findViewById(R.id.timePicker);
        calendar = Calendar.getInstance();
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
        String hour = null;
        String minute = null;
        String day = "am";

        if(alarmHour > 12){
            alarmHour = alarmHour - 12;
            hour = String.valueOf(alarmHour);
            day = "pm";
        }
        if(alarmHour < 10){
            minute = "0" + String.valueOf(alarmMinute);
        }


        if(item.getItemId() == R.id.set_alarm){
            Toast.makeText(MainActivity.this, "Alarm is set to " + hour+":"+minute+" "+day, Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
