package com.example.amitava.hourlyalarmclock;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Amitava on 18-Jan-18.
 * This class will basically start the Ringtone Service for handling the media file
 * and playing the ringtone when the alarm is triggered by the Broadcast Receiver
 */

public class RingtonePlayingService extends Service {
    //MediaPlayer objects for handling two different media file
    MediaPlayer mediaPlayer = new MediaPlayer();
    MediaPlayer mediaPlayerHourly = new MediaPlayer();
    //A boolean variable which will give the current status of the ringtone service
    //if it's running or not
    static boolean isRunning = false;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Recognizing if the service call occured for the Hourly alarm or the normal alarm
        if (intent.hasExtra("hourly")){
            Log.d("Hourly Alarm", "On the receiver");
            //If the Normal alarm is playing at this time, we need to stop the service first
            mediaPlayer.reset();
            String hourly_alarm_time = intent.getExtras().getString("hourly");
            //Initializing the mediaPlayer Object with the media file and starting it
            mediaPlayerHourly = MediaPlayer.create(this, R.raw.chimes);
            mediaPlayerHourly.start();

        } else {
            //If the Hourly alarm is playing at this time, we need to stop the service first
            mediaPlayerHourly.reset();
            String alarm_state = intent.getExtras().getString("extra");
            //converting the alarm state which is extra string in the Intent, to flags
            //assert alarm_state != null;
            switch (alarm_state) {
                case "alarm_on":
                    flags = 1;
                    break;
                case "alarm_off":
                    flags = 0;
                    break;
                default:
                    flags = 0;
                    break;
            }

            //if else statement to turn on/off ringtone service as per the alarm and ringtone states
            if (!this.isRunning && flags==1){
                //When music is not playing and the user clicks alarm on
                Log.d("There is no music","But user choosed to turn on alarm");
                mediaPlayer = MediaPlayer.create(this, R.raw.wake_up_sound);
                mediaPlayer.start();
                this.isRunning = true;
                //Initializing the Notification Manager with the System Notification service... hope it works :3
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                //Intent for opening the mainActivity when the notification is clicked
                Intent intentToMain = new Intent(this.getApplicationContext(), MainActivity.class);
                //Notification requires a pending intent
                PendingIntent pendingIntentToMain = PendingIntent.getActivity(this.getApplicationContext(), 0,
                        intentToMain,PendingIntent.FLAG_CANCEL_CURRENT);

                //creating the actual notification which will be shown to the user
                Notification notification = new Notification.Builder(this)
                        .setContentTitle("Alarm")
                        .setContentText("Click here to cancel/stop the alarm")
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntentToMain)
                        .setSmallIcon(R.drawable.icon)
                        .build();

                //notification start command to use the notification manager to call the notification
                notificationManager.notify(0, notification);
            }else if (this.isRunning && flags==0){
                //When music is playing and the user click on alarm off
                Log.d("There is music running","But you want to turn off alarm");
                mediaPlayer.stop();
                mediaPlayer.reset();
                this.isRunning = false;
            }else if (!this.isRunning && flags==0){
                //When music is not playing and the user presses alarm off
                Log.d("There is no music","But you want to turn off alarm");
            }else if (this.isRunning && flags==1){
                //When music is playing but the user presses on alarm on
                Log.d("There is music running", "But user wants to turn on alarm");
            }else {
                Log.d("Else statement", "Somehow reached which I think would never happen :p");
            }
            //mediaPlayer.reset();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {super.onDestroy();}
}
