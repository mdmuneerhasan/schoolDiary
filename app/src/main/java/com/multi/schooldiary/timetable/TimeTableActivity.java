package com.multi.schooldiary.timetable;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.multi.schooldiary.R;
import com.multi.schooldiary.activity.MyToolBar;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.SavedData;

import java.util.ArrayList;
import java.util.Calendar;

public class TimeTableActivity extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    SavedData savedData;
    private Connection connection;
    ArrayList<Fragment> fragmentArrayList;
    FragmentManager fragmentManager;
    ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        floatingActionButton=findViewById(R.id.btnFloating);
        savedData =new SavedData(this);
        connection=new Connection();
        MyToolBar myToolBar=new MyToolBar(this, (Toolbar) findViewById(R.id.toolbar)) {
            @Override
            public void onAlertActionPerformed() {
                onStart();
            }
        };







        setSupportActionBar(myToolBar.getToolBar());
        if(Integer.parseInt(savedData.getValue("position"))<4){
            floatingActionButton.setVisibility(View.GONE);
        }
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
              finish();
                    startActivity(new Intent(getBaseContext(),TimeTableActivityConsole.class));
                }
            });


    }

    @Override
    protected void onStart() {
        super.onStart();
        fragmentArrayList=new ArrayList<>();
        fragmentManager=getSupportFragmentManager();
        viewPager=findViewById(R.id.vpPager);
        viewPager.setOffscreenPageLimit(3);
        fragmentArrayList.add(new TSun());
        fragmentArrayList.add(new TMon());
        fragmentArrayList.add(new TTue());
        fragmentArrayList.add(new TWed());
        fragmentArrayList.add(new TThu());
        fragmentArrayList.add(new TFri());
        fragmentArrayList.add(new TSat());
        pagerAdapter=new PagerAdapter(fragmentManager,fragmentArrayList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1);
    }

}
