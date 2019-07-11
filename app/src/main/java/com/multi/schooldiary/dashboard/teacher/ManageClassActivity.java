package com.multi.schooldiary.dashboard.teacher;

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
import com.multi.schooldiary.activity.MyToolBar;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.RecyclerAdapter;
import com.multi.schooldiary.utility.RecyclerClass;
import com.multi.schooldiary.utility.RecyclerClick;
import com.multi.schooldiary.utility.SavedData;
import com.multi.schooldiary.utility.User;

import java.util.ArrayList;
import java.util.Collections;

public class ManageClassActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    static ArrayList<RecyclerClass> arrayList;
    RecyclerAdapter recyclerAdapter;
    RecyclerClick click;
    SavedData savedData;
    Connection connection;
    RecyclerClass recyclerClass;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);
        recyclerView=findViewById(R.id.recycle);
        arrayList=new ArrayList<>();
        savedData =new SavedData(this);
        connection=new Connection();
        savedData.showAlert("connecting...");
        click=new RecyclerClick() {
            @Override
            public void onBindViewHolder(RecyclerAdapter.Holder holder, final RecyclerClass recyclerClass) {
                holder.tvHeading.setVisibility(View.VISIBLE);
                holder.imageView.setVisibility(View.VISIBLE);
                holder.text1.setVisibility(View.VISIBLE);
                holder.text2.setVisibility(View.VISIBLE);
//                holder.text3.setVisibility(View.VISIBLE);

                holder.btn1.setText("edit");
                holder.btn1.setVisibility(View.VISIBLE);
                holder.btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        startActivity(new Intent(getBaseContext(),EditStudentActivity.class).putExtra("intentId",recyclerClass.getText3())
                                .putExtra("intentName",recyclerClass.getHeading())
                                .putExtra("intentRollNo",recyclerClass.getText1().substring(11)));
                    }
                });
            }
        };
        recyclerAdapter=new RecyclerAdapter(this,click,arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);
        MyToolBar myToolBar= new MyToolBar((Toolbar) findViewById(R.id.toolbar), this) {
            @Override
            public void onAlertActionPerformed() {
            }
        };
        setSupportActionBar(myToolBar.getToolBar());
        findViewById(R.id.btnFloating).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudent(v);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        connection.getDbSchool().child(savedData.getValue("schoolId")).child("student")
                .child(savedData.getValue("stClass")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    id = dataSnapshot1.child("uid").getValue(String.class);
                    User user2 = dataSnapshot1.getValue(User.class);
                    recyclerClass = new RecyclerClass();
                    recyclerClass.setHeading(user2.getName());
                    recyclerClass.setUrl(user2.getPhotoUrl());
                    recyclerClass.setText2("Sid : " + user2.getSid());
                    recyclerClass.setText1("Roll No. : " + user2.getRollNo());
                    recyclerClass.setText3(user2.getUid());
                    arrayList.add(recyclerClass);
                }
                Collections.sort(arrayList,RecyclerClass.text1Comp);
                connection.getDbSchool().child(savedData.getValue("schoolId")).child("student")
                        .child(savedData.getValue("stClass")).removeEventListener(this);
                savedData.removeAlert();
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void addStudent(View view) {
        startActivity(new Intent(this, AddStudentActivity.class));
    }
}
