package com.multi.schooldiary.timetable;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.multi.schooldiary.R;
import com.multi.schooldiary.activity.MainActivity;
import com.multi.schooldiary.activity.MyToolBar;

import java.util.ArrayList;
import java.util.Calendar;

public class TimeTableActivityConsole extends AppCompatActivity {
    ArrayList<Fragment> fragmentArrayList;
    FragmentManager fragmentManager;
    ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        MyToolBar myToolBar=new MyToolBar(this, (Toolbar) findViewById(R.id.toolbar)) {
            @Override
            public void onAlertActionPerformed() {
                onStart();
            }
        };
        setSupportActionBar(myToolBar.getToolBar());
        FloatingActionButton floatingActionButton=findViewById(R.id.btnFloating);
        floatingActionButton.setImageResource(R.drawable.ic_playlist_add_check_black_24dp);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        fragmentArrayList=new ArrayList<>();
        fragmentManager=getSupportFragmentManager();
        viewPager=findViewById(R.id.vpPager);
        fragmentArrayList.add(new ESun());
        fragmentArrayList.add(new EMon());
        fragmentArrayList.add(new ETue());
        fragmentArrayList.add(new EWed());
        fragmentArrayList.add(new EThu());
        fragmentArrayList.add(new EFri());
        fragmentArrayList.add(new ESat());
        pagerAdapter=new PagerAdapter(fragmentManager,fragmentArrayList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1);

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to save changes?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       save();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        unSave();
                    }
                })
       .setCancelable(true);
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void save() {
        finish();
    }

    private void unSave() {
        System.exit(0);
    }
}
