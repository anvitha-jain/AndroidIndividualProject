package com.example.helper;

import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.content.Intent;

public class Instructions extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instruction_activity);
    }

    public void back(View v) {
        Intent i_back = new Intent(Instructions.this, MainActivity.class);
        startActivity(i_back);

    }
}

