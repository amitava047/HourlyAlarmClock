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
        String alarmChoice = intent.getExtras().getString("extra");

        //Initializing intent and start ringtone service
        Intent service_intent = new Intent(context, RingtonePlayingService.class);
        service_intent.putExtra("extra", alarmChoice);
        context.startService(service_intent);
    }
}
