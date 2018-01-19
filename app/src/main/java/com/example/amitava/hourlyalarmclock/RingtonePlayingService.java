package com.example.amitava.hourlyalarmclock;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Amitava on 18-Jan-18.
 */

public class RingtonePlayingService extends Service {
    MediaPlayer mediaPlayer;
    //int state;
    boolean isRunning = false;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
