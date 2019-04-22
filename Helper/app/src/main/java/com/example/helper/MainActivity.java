package com.example.helper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
    public void remind(View view) {

        Intent intent = new Intent(MainActivity.this, MedicineReminder.class);
        startActivity(intent);

    }

    public void emergency(View view){

        Intent e_intent = new Intent(MainActivity.this, Emergency.class);
        startActivity(e_intent);

    }

}
