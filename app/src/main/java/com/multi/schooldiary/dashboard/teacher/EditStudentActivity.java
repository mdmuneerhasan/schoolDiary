package com.multi.schooldiary.dashboard.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.multi.schooldiary.R;
import com.multi.schooldiary.activity.MyToolBar;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.SavedData;

public class EditStudentActivity extends AppCompatActivity {
    EditText edtName, edtRollNo;
    Connection connection;
    SavedData savedData;
    String name, uid, rollNo;
    TextView tvClass;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);


        //      startActivity(new Intent(getBaseContext(),EditStudentActivity.class).putExtra("intentId",recyclerClass.getText3())
        //                                .putExtra("intentName",recyclerClass.getHeading())
        //                                .putExtra("intentRollNo",recyclerClass.getText1()));
        //
        edtName = findViewById(R.id.edtName);
        edtRollNo = findViewById(R.id.edtRollNo);
        connection = new Connection();
        savedData = new SavedData(this);
        tvClass = findViewById(R.id.tvClass);
        try {
            uid = getIntent().getStringExtra("intentId");
            name = getIntent().getStringExtra("intentName");
            rollNo = getIntent().getStringExtra("intentRollNo");
        }catch (Exception e) {
        }
        edtName.setText(name);
        tvClass.setText("edit " + name + " of " + savedData.getValue("stClass"));
        edtRollNo.setText(rollNo);
        MyToolBar myToolBar= new MyToolBar((Toolbar) findViewById(R.id.toolbar), this) {
            @Override
            public void onAlertActionPerformed() {
            }
        };
        setSupportActionBar(myToolBar.getToolBar());

    }

    public void add(View view) {
        name = edtName.getText().toString();
        rollNo = edtRollNo.getText().toString();
        if (name.trim().length() < 2) {
            edtName.setError("name too short");
            return;
        }
        if (rollNo.trim().length() < 1) {
            edtRollNo.setError("invalid rollo no.");
            return;
        }
        savedData.showAlert("updating...");
        connection.getDbUser().child(uid).child("name").setValue(name);
        connection.getDbUser().child(uid).child("rollNo").setValue(rollNo);
        connection.getDbSchool().child(savedData.getValue("schoolId")).child("student")
                .child(savedData.getValue("stClass")).child(uid).child("name").setValue(name);
        connection.getDbSchool().child(savedData.getValue("schoolId")).child("student")
                .child(savedData.getValue("stClass")).child(uid).child("rollNo").setValue(rollNo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(getBaseContext(),ManageClassActivity.class));
                        finish();
                        savedData.removeAlert();
                    }
                });
    }

    public void delete(View view) {
        savedData.showAlert("Removing Student");
        connection.getDbSchool().child(savedData.getValue("schoolId")).child("student")
                .child(savedData.getValue("stClass")).child(uid)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                        savedData.removeAlert();
                        startActivity(new Intent(getBaseContext(),ManageClassActivity.class));
                    }
                });
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getBaseContext(),ManageClassActivity.class));
    }
}
