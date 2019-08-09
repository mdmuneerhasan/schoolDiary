package com.multi.schooldiary.notice;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.multi.schooldiary.R;
import com.multi.schooldiary.activity.MyToolBar;
import com.multi.schooldiary.notice.Notice;
import com.multi.schooldiary.notice.NoticeActivity;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.SavedData;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class NoticeConsoleActivity extends AppCompatActivity {
    EditText edtTitle,edtSubject,edtDescription;
    SavedData savedData;
    Button btnDelete, btnBroadcast;
    Connection connection;
    private String key ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_console);
        edtDescription=findViewById(R.id.edtDescription);
        edtTitle=findViewById(R.id.edtTitle);
        edtSubject=findViewById(R.id.edtSubject);
        btnDelete=findViewById(R.id.btnDelete);
        btnBroadcast =findViewById(R.id.btnBroadcast);
        savedData =new SavedData(this);
        connection=new Connection();
        MyToolBar myToolBar= new MyToolBar((Toolbar) findViewById(R.id.toolbar), this) {
            @Override
            public void onAlertActionPerformed() {
            }
        };
        setSupportActionBar(myToolBar.getToolBar());

        try{
             key=getIntent().getStringExtra("key");
             connection.getDbSchool().child(savedData.getValue("schoolId")).child("notice")
                     .child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     Notice notice=dataSnapshot.getValue(Notice.class);
                     edtTitle.setText(notice.getTitle());
                     edtDescription.setText(notice.getDescription());
                     edtSubject.setText(notice.getSubject());
                     btnDelete.setVisibility(View.VISIBLE);
                     btnBroadcast.setText("update notice");
                     connection.getDbSchool().child(savedData.getValue("schoolId")).child("notice")
                             .child(key).removeEventListener(this);
                     }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });
        }catch (Exception e){

        }

    }

    public void broadcast(View view) {
        String stDescription=edtDescription.getText().toString();
        String stTitle=edtTitle.getText().toString();
        String stSubject=edtSubject.getText().toString();
        String broadCaster,uid,date,name;
        uid= savedData.getValue("uid");
        name= savedData.getValue("name");
        date=new SimpleDateFormat("dd-MMM-yy").format(new Date(System.currentTimeMillis()));
        int position=Integer.parseInt(savedData.getValue("position"));
        if(stDescription.trim().length()<1){
            edtDescription.setError("description can't be null");
            return;
        }

        if(savedData.getType().equals("school")){
            if (position==7){
                broadCaster="Principal";
            }else{
                String[] positionList = new String[]{"none", "none", "student", "parent", "monitor", "teacher", "vice president", "principal"};
                broadCaster= savedData.getValue("name")+"\n"+positionList[Integer.parseInt(savedData.getValue("position"))];
            }
        }else {
            if (position==7){
                broadCaster="HOD";
            }else{
                broadCaster= savedData.getValue("name");
            }
        }
        Notice notice=new Notice(stTitle,date,stSubject,stDescription,broadCaster,uid,name);
        if(key==null){
            connection.getDbSchool().child(savedData.getValue("schoolId")).child("notice").push().setValue(notice);
        }else{
            connection.getDbSchool().child(savedData.getValue("schoolId")).child("notice").child(key).setValue(notice);
        }
        finish();
    }

    public void delete(View view) {
        if(key!=null){
            connection.getDbSchool().child(savedData.getValue("schoolId")).child("notice")
                    .child(key).removeValue();
            savedData.toast("deleted");
            finish();

        }
    }
}
