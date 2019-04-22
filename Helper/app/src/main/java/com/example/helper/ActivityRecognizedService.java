package com.example.helper;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;

public class ActivityRecognizedService extends IntentService {
    int count;  // count gives the number of times the activity recognise api recognises an activity and gives a result
    // if count = 0, then it is the first time the api returns a result

    public ActivityRecognizedService() {
        super("ActivityRecognizedService");
    }

    public ActivityRecognizedService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Checking whether activity recognition api returns a result
        // If so, get the result and obtain the recognised activity
        if(ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            int act_num = result.getMostProbableActivity().getType();
            //Log.d("RecognisedActivity", String.valueOf(act_num));
            // Getting the value of count in shared preferences
            SharedPreferences preferences = getSharedPreferences("CountPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor_count = preferences.edit();
            count = preferences.getInt("count", 0);
            // An intent to main activity is created which contains the recognised activity
            // This intent has to be sent during the first time the api returns a result or if the recognised activity is ON_FOOT
            if(act_num == 2 || count == 0) {
                Intent intent_activity = new Intent(this, Main2Activity.class);
                intent_activity.putExtra("RecognisedActivity", act_num);
                intent_activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                count++;
                // Saving the value of count in shared preferences
                editor_count.putInt("count", count);
                editor_count.apply();
                startActivity(intent_activity);
            }
        }
    }
}

