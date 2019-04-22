package com.example.helper;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

public class AccelerometerClass extends Activity implements AccelerometerListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.num_display_activity);

        // Check onResume Method to start accelerometer listener
    }

    public void onAccelerationChanged(float x, float y, float z) {
        // TODO Auto-generated method stub

    }

    public void onShake(float force) {

        // Do your stuff here

        // Called when Motion Detected
        Toast.makeText(getBaseContext(), "Motion detected",
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getBaseContext(), "onResume Accelerometer Started",
                Toast.LENGTH_SHORT).show();

        //Check device supported Accelerometer senssor or not
        if (AccelerometerSensor.isSupported(this)) {

            //Start Accelerometer Listening
            AccelerometerSensor.startListening(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        //Check device supported Accelerometer senssor or not
        if (AccelerometerSensor.isListening()) {

            //Start Accelerometer Listening
            AccelerometerSensor.stopListening();

            Toast.makeText(getBaseContext(), "onStop Accelerometer Stoped",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Sensor", "Service  distroy");

        //Check device supported Accelerometer senssor or not
        if (AccelerometerSensor.isListening()) {

            //Start Accelerometer Listening
            AccelerometerSensor.stopListening();

            Toast.makeText(getBaseContext(), "onDestroy Accelerometer Stoped",
                    Toast.LENGTH_SHORT).show();
        }

    }

}
