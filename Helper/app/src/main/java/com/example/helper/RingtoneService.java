package com.example.helper;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;

public class RingtoneService extends Service
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    public GoogleApiClient mApiClient;
    MediaPlayer media_song;
    String action;
    PendingIntent pendingIntent;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Getting whether alarm is ON or OFF
        action = intent.getExtras().getString("Action");
        assert action!= null;

        // Set up the notification service
        NotificationManager notify_manager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        // Set up an intent that goes to the Main Activity
        Intent intent_main_activity = new Intent(this.getApplicationContext(), Main2Activity.class);
        // Set up a pending intent
        PendingIntent pending_intent_main_activity = PendingIntent.getActivity(this, 0,
                intent_main_activity, 0);
        // Create the notification parameters
        Notification notification_popup = new Notification.Builder(this)
                .setContentTitle("An alarm is going off!")
                .setContentText("Click me!")
                .setSmallIcon(R.drawable.ic_action_call)
                .setContentIntent(pending_intent_main_activity)
                .setAutoCancel(true)
                .build();

        final int NOTIFICATION_ID = 0;

        if(action.equals("Alarm ON")) {
            // Connect to Google Api
            mApiClient = new GoogleApiClient.Builder(this)
                    .addApi(ActivityRecognition.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            mApiClient.connect();
            // Start the ringtone and set it to loop
            if(media_song == null)
                media_song = MediaPlayer.create(this, R.raw.ringtone1);
            media_song.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            media_song.setLooping(true);
            media_song.start();
            //Log.d("Playing: ", String.valueOf(media_song.isPlaying()));
            // Set up the start command for the notification
            notify_manager.notify(NOTIFICATION_ID, notification_popup);
        } else if(action.equals("Alarm OFF")){
            // Stop the ringtone
            if(media_song != null) {
                media_song.setLooping(false);
                media_song.stop();
                media_song.reset();
                media_song.release();
                //Log.d("Playing: ", String.valueOf(media_song.isPlaying()));
                // Cancel the notification
                notify_manager.cancel(NOTIFICATION_ID);
                if(mApiClient.isConnected()) {
                    // Remove the activity updates and disconnect
                    ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(mApiClient, pendingIntent);
                    mApiClient.disconnect();
                    //Log.d("isConnected", String.valueOf(mApiClient.isConnected()));
                }
            }
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Log.d("RingtonePlayingService", "Onconnected");
        // Create an intent to ActivityRecognize.java which will obtain the recognised activity
        Intent intent = new Intent( this, ActivityRecognizedService.class );
        pendingIntent = PendingIntent.getService( this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates( mApiClient, 300, pendingIntent );
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Log.d("RingtonePlayingService", "OnConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Log.d("RingtonePlayingService", "OnConnectionFailed");
    }

}


