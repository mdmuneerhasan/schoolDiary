package com.multi.schooldiary.principal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.multi.schooldiary.R;

public class PrincipalActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);



    }

    public void TeacherRequest(View view) {
        startActivity(new Intent(this, TeacherRequestActivity.class));
    }
}
