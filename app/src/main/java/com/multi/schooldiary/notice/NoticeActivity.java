package com.multi.schooldiary.notice;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.multi.schooldiary.R;
import com.multi.schooldiary.activity.MainActivity;
import com.multi.schooldiary.activity.MyToolBar;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.SavedData;

import java.util.ArrayList;

public class NoticeActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Connection connection;
    SavedData savedData;
    NoticeAdapter noticeAdapter;
    ArrayList<Notice> noticeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        recyclerView=findViewById(R.id.recycle);
        connection=new Connection();
        savedData =new SavedData(this);
        setSupportActionBar(new MyToolBar((Toolbar) findViewById(R.id.toolbar),this) {
            @Override
            public void onAlertActionPerformed() {
            }
        }.getToolBar());
        noticeList=new ArrayList<>();
        noticeAdapter= new NoticeAdapter(noticeList, savedData.getValue("uid")) {
            @Override
            public void onClickListener() {
                finish();
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(noticeAdapter);
        try{if(Integer.parseInt(savedData.getValue("position"))<5){
            findViewById(R.id.btnAdd).setVisibility(View.GONE);
        }
        }catch (Exception e){
            savedData.test(savedData.getValue("uid"));
            findViewById(R.id.btnAdd).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        savedData.showAlert("connecting...");
        connection.getDbSchool()
                .child(savedData.getValue("schoolId"))
                .child("notice")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                noticeList.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                       Notice notice=dataSnapshot1.getValue(Notice.class);
                       notice.setKey(dataSnapshot1.getKey());
                       noticeList.add(0,notice);
                }
                noticeAdapter.notifyDataSetChanged();
                connection.getDbSchool().child(savedData.getValue("uid"))
                        .child("notice").removeEventListener(this);
                savedData.removeAlert();
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void add(View view) {
        finish();
        startActivity(new Intent(this, NoticeConsoleActivity.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
