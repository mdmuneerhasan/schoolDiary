package com.multi.schooldiary.teacher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.multi.schooldiary.R;

public class TeacherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
    }

    public void TeacherRequest(View view) {
        startActivity(new Intent(this,StudentRequestActivity.class));
    }

    public void Attendance(View view) {
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
}
