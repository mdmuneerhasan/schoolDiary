package com.multi.schooldiary.student_or_guardian;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.multi.schooldiary.R;
import com.multi.schooldiary.utility.Storage;

public class StudentGuardianActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_guardian);

















    }

    public void AccountRequest(View view) {
        startActivity(new Intent(this,RequestActivity.class));
    }
}
