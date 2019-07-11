package com.multi.schooldiary.dashboard.teacher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.multi.schooldiary.R;
import com.multi.schooldiary.activity.MyToolBar;
import com.multi.schooldiary.utility.SavedData;

public class TeacherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        MyToolBar myToolBar= new MyToolBar((Toolbar) findViewById(R.id.toolbar), this) {
            @Override
            public void onAlertActionPerformed() {
            }
        };
        setSupportActionBar(myToolBar.getToolBar());

    }

    public void TeacherRequest(View view) {
        startActivity(new Intent(this,StudentRequestActivity.class));
    }

    public void Attendance(View view) {
        new SavedData(this).setValue("get","get");
        startActivity(new Intent(this,AttendanceActivity.class));
    }

    public void manageClass(View view) {
        startActivity(new Intent(this,ManageClassActivity.class));
    }

    public void AttendanceRegister(View view) {
        startActivity(new Intent(this, AttendanceRegisterActivity.class));
    }

    public void guardianRequest(View view) {
        startActivity(new Intent(this,GuardianRequestActivity.class));
    }

    public void attendanceToday(View view) {
        startActivity(new Intent(this,AttendanceTodayActivity.class));
    }
}
