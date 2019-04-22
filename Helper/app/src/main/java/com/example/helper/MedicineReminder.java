package com.example.helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class MedicineReminder  extends AppCompatActivity implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList al;
    private List list=new ArrayList();
    private ArrayAdapter<String> adapter;
    ListView lv;
    TextView tv;
    FloatingActionButton fab;



    public MedicineReminder() {
        // Required empty public constructor
    }

    public void receiveData(ArrayList al)
    {
        this.al=al;
        list.add(al.get(0));
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_fragment);

        fab= (FloatingActionButton) findViewById(R.id.floatingActionButton);

        lv= (ListView) findViewById(R.id.rem_lv);
        tv= (TextView) findViewById(R.id.reminder_tv);

        fab.setOnClickListener(this);
        DatabaseHandler db=new DatabaseHandler(MedicineReminder.this);
        list=db.getAllReminders();

        if(list.size()==0)
        {
            lv.setVisibility(GONE);
            return ;
        }

        tv.setVisibility(GONE);

        String desc[] = {" add reminders by clicking + button below"};
        adapter = new MedicineList(MedicineReminder.this,list,desc);
        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getActivity(),"You Clicked "+list.get(i), Toast.LENGTH_SHORT).show();
                Intent in=new Intent(MedicineReminder.this,MedicineReminderInformation.class);
                in.putExtra("id",list.get(i).toString());
                startActivity(in);
            }
        });

    }





    @Override
    public void onClick(View v) {
        Intent in=new Intent(MedicineReminder.this,AddReminder.class);
        startActivity(in);
    }



}


