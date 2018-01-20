package com.example.amitava.hourlyalarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Amitava on 18-Jan-18.
 * This class is working as the Broadcast Receiver for receiving
 * intents from the MainActivity and setting off the alarm
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("We are on the receiver", "Yay!");
        //Checking if the received intent is for hourly alarm or normal alarm
        if (intent.hasExtra("extra")){
            Log.d("Alarm Receiver", "Receiving");
            String alarmChoice = intent.getExtras().getString("extra");

            //Initializing intent and start ringtone service
            Intent service_intent = new Intent(context, RingtonePlayingService.class);
            //Putting extraString in the Intent for differentiating from te hourly alarm intent
            service_intent.putExtra("extra", alarmChoice);
            //Starting the service with the context and the created intent
            context.startService(service_intent);
        } else if (intent.hasExtra("hourly")){
            //String time = intent.getData().toString();
            Log.d("Hourly Alarm Receiver", "Receiving");
            String hourlyAlarm = intent.getExtras().getString("hourly");

            //Initializing ringtone service for the hourly alarm
            Intent hourly_service_intent = new Intent(context, RingtonePlayingService.class);
            //Putting extraString in the Intent for differentiating from te normal alarm intent
            hourly_service_intent.putExtra("hourly", hourlyAlarm);
            //By setting the flag of the intent Clear Top, it will replace the previously
            //existing same intent, if exists.
            hourly_service_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //Starting the service through the context with the created intent
            context.startService(hourly_service_intent);
        }

    }
}
