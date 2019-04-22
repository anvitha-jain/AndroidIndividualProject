package com.example.helper;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;


class Emergency extends MainActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_activity);


    }

    public void register(View v) {
        Intent i_register=new Intent(Emergency.this,Registration.class);
        startActivity(i_register);

    }

    public void display_no(View v) {
        Intent i_view=new Intent(Emergency.this,Display.class);
        startActivity(i_view);

    }

    public void instruct(View v) {
        Intent i_help=new Intent(Emergency.this,Instructions.class);
        startActivity(i_help);
    }

    public void verify(View v) {
        Intent i_verify=new Intent(Emergency.this,Verification.class);
        startActivity(i_verify);
    }
}

