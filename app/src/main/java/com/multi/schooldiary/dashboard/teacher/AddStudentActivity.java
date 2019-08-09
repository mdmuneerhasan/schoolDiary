package com.multi.schooldiary.dashboard.teacher;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.multi.schooldiary.R;
import com.multi.schooldiary.activity.MyToolBar;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.SavedData;
import com.multi.schooldiary.utility.User;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class AddStudentActivity extends AppCompatActivity {
    EditText edtName,edtRollNo;
    String sid;
    Connection connection;
    SavedData savedData;
    ValueEventListener valueEventListener1;
    User user;
    TextView tvClass;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        edtName =findViewById(R.id.edtName);
        edtRollNo =findViewById(R.id.edtRollNo);
        connection=new Connection();
        savedData =new SavedData(this);
        tvClass=findViewById(R.id.tvClass);
        tvClass.setText("add student to "+ savedData.getValue("stClass"));
        final String time=new SimpleDateFormat("yy").format(new Date(System.currentTimeMillis()));
        MyToolBar myToolBar= new MyToolBar((Toolbar) findViewById(R.id.toolbar), this) {
            @Override
            public void onAlertActionPerformed() {
            }
        };
        setSupportActionBar(myToolBar.getToolBar());

        valueEventListener1=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    sid= "s"+time+String.valueOf(dataSnapshot.getChildrenCount());
                }catch (Exception e){
                    sid= "s"+time+"0";
                }
                user.setSid(sid);
                user.setUid(sid);
                user.setPosition("2");
                user.setSchoolId(savedData.getValue("schoolId"));
                user.setSchoolName(savedData.getValue("schoolName"));
                user.setStClass(savedData.getValue("stClass"));
                connection.getDbUser().child(user.getSid()).setValue(user);
                connection.getDbUser().removeEventListener(valueEventListener1);

                connection.getDbSchool().child(savedData.getValue("schoolId")).child("student")
                        .child(savedData.getValue("stClass")).child(user.getUid()).setValue(user)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                         savedData.removeAlert();
                         savedData.toast("Student added to class");
                         edtName.setText("");
                         edtRollNo.setText("");
                            }
                        });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
    }
    public void add(View view) {
        if(edtName.getText().toString().trim().length()<2){
            edtName.setError("name too short");
        return;
        }
        if(edtRollNo.getText().toString().trim().length()<1){
            edtRollNo.setError("invalid rollo no.");
            return;
        }
        savedData.showAlert("Adding...");
        user=new User();
        user.setName(edtName.getText().toString());
        user.setRollNo(edtRollNo.getText().toString());
        connection.getDbUser().addValueEventListener(valueEventListener1);
    }
}
