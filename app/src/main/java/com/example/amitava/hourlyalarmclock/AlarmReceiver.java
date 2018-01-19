package com.example.amitava.hourlyalarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Amitava on 18-Jan-18.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("We are on the receiver", "Yay!");
        if (intent.hasExtra("extra")){
            Log.d("Alarm Receiver", "Receiving");
            String alarmChoice = intent.getExtras().getString("extra");

            //Initializing intent and start ringtone service
            Intent service_intent = new Intent(context, RingtonePlayingService.class);
            service_intent.putExtra("extra", alarmChoice);
            context.startService(service_intent);
        } else if (intent.hasExtra("hourly")){
            //String time = intent.getData().toString();
            Log.d("Hourly Alarm Receiver", "Receiving");
            String hourlyAlarm = intent.getExtras().getString("hourly");

            //Initializing ringtone service for the hourly alarm
            Intent hourly_service_intent = new Intent(context, RingtonePlayingService.class);
            hourly_service_intent.putExtra("hourly", hourlyAlarm);
            hourly_service_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startService(hourly_service_intent);
        }

    }
}
