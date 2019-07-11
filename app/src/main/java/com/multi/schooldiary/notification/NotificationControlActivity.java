package com.multi.schooldiary.notification;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.multi.schooldiary.R;
import com.multi.schooldiary.activity.MyToolBar;

public class NotificationControlActivity extends AppCompatActivity {

    private static final String TAG = "Notification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notication_control);
        MyToolBar myToolBar= new MyToolBar((Toolbar) findViewById(R.id.toolbar), this) {
            @Override
            public void onAlertActionPerformed() {
            }
        };
        setSupportActionBar(myToolBar.getToolBar());

    }

    public void start(View v) {

    }

    public void stop(View v) {

    }
}