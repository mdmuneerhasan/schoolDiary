package com.multi.schooldiary.dashboard.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.multi.schooldiary.R;
import com.multi.schooldiary.activity.MyToolBar;
import com.multi.schooldiary.utility.BiHashMap;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.RecyclerClass;
import com.multi.schooldiary.utility.SetDefaultClass;
import com.multi.schooldiary.utility.SavedData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AttendanceTodayActivity extends AppCompatActivity {
    String  date,dateUnTrimmed;
    ArrayList<String> arrayListDate;
    SavedData savedData;
    Connection connection;
    ArrayList<String> uidList;
    BiHashMap<String, String, String> biHashMap;
    int totalCount = 0, increment = 1;
    Map<String, Integer> mapAttendanceCount;
    BiHashMap<String,String,String> mapGuradian;
    String month;
    ArrayList<RecyclerClass> tableAdapterList,tableAdapterList2;
    RecyclerView recyclerView,recyclerView2;
    TableAdapter tableAdapter,tableAdapter2;
    Date date1;
    TableAdapterClick click;
    private String schoolId;
    private String stClass;
    Boolean clickable=true;
    Button btnRefresh,btnNotify;
    private String type;
    private ArrayList<SetDefaultClass> arrayListSetDefaultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_today);
        MyToolBar myToolBar= new MyToolBar((Toolbar) findViewById(R.id.toolbar), this) {
            @Override
            public void onAlertActionPerformed() {
            }
        };
        setSupportActionBar(myToolBar.getToolBar());
        click=new TableAdapterClick() {
            @Override
            public void switchTo(int which, TableAdapter.Holder holder, RecyclerClass recyclerClass) {
                clickable=false;
                String attendance=recyclerClass.getText2();
                Attendance attendance1=new Attendance(recyclerClass.getHeading(),recyclerClass.getText2());
                switch (which) {
                    case 0:
                        attendance1.setAttendance("p");
                        connection.getDbSchool().child(schoolId).child("attendance").child(stClass)
                                .child(month).child(dateUnTrimmed).child(type).child(recyclerClass.getHeading()).child("1").setValue(attendance1);
                        attendance="p"+attendance.substring(1);
                        break;
                    case 1:
                        attendance1.setAttendance("a");
                        connection.getDbSchool().child(schoolId).child("attendance").child(stClass)
                                .child(month).child(dateUnTrimmed).child(type).child(recyclerClass.getHeading()).child("1").setValue(attendance1);
                        attendance="a"+attendance.substring(1);
                        break;
                    case 2:
                        attendance1.setAttendance("p");
                        connection.getDbSchool().child(schoolId).child("attendance").child(stClass)
                                .child(month).child(dateUnTrimmed).child(type).child(recyclerClass.getHeading()).child("2").setValue(attendance1);
                        attendance=attendance.charAt(0)+"p";
                        break;
                    case 3:
                        attendance1.setAttendance("a");
                        connection.getDbSchool().child(schoolId).child("attendance").child(stClass)
                                .child(month).child(dateUnTrimmed).child(type).child(recyclerClass.getHeading()).child("2").setValue(attendance1);
                        attendance=attendance.charAt(0)+"a";
                        break;
                }
                holder.tvText2.setText(attendance);
            }
        };
        arrayListDate=new ArrayList<>();
        uidList = new ArrayList<>();
        biHashMap = new BiHashMap<>();
        mapGuradian = new BiHashMap<>();
        arrayListDate = new ArrayList<>();
        arrayListSetDefaultList = new ArrayList<>();
        tableAdapterList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycle);
        tableAdapterList2 = new ArrayList<>();
        recyclerView2 = findViewById(R.id.recycle2);
        tableAdapter = new TableAdapter(tableAdapterList,click);
        tableAdapter2 = new TableAdapter(tableAdapterList2,click);
        savedData = new SavedData(this);
        connection = new Connection();
        mapAttendanceCount = new HashMap<>();
        date1 = new Date(System.currentTimeMillis());
        month = new SimpleDateFormat("yyyy-MM").format(date1);
        TextView tvDate = findViewById(R.id.tvDate);
        tvDate.setText("Attendance of " + new SimpleDateFormat("dd-MMM-yyyy").format(date1));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(tableAdapter);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setAdapter(tableAdapter2);
        schoolId= savedData.getValue("schoolId");
        stClass= savedData.getValue("stClass");
        btnNotify= findViewById(R.id.btnNotify);
        btnRefresh= findViewById(R.id.btnRefresh);
        savedData.setValue("get","set");
        type= savedData.getType();
    }

    @Override
    protected void onStart() {
        clickable=true;
        btnRefresh.setText("refresh");
        btnNotify.setError(null);
        savedData.showAlert("Connecting...");
        super.onStart();
        totalCount=0;
        // fetching names
        connection.getDbSchool().child(savedData.getValue("schoolId")).child("student")
                .child(savedData.getValue("stClass")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uidList.clear();
                biHashMap.clear();
                biHashMap.put("uid", "date", "name/date");
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    SetDefaultClass setDefaultClass = dataSnapshot1.getValue(SetDefaultClass.class);
                    for(DataSnapshot v:dataSnapshot1.child("guardian").getChildren()){
                        if(v!=null){
                            if(true){//v.child(v.getKey()).getValue(Boolean.class)
                                mapGuradian.put(setDefaultClass.getUid(),v.getKey(),v.getKey());
                            }
                        }
                    }
                    arrayListSetDefaultList.add(setDefaultClass);
                    mapAttendanceCount.put(setDefaultClass.getUid(), 0);
                    biHashMap.put(setDefaultClass.getUid(), "date", setDefaultClass.getName() + "(" + setDefaultClass.getRollNo() + ")");
                }

                // sorting can be performed here
                Collections.sort(arrayListSetDefaultList,SetDefaultClass.rollNoSort);
                for(SetDefaultClass setDefaultClass:arrayListSetDefaultList){
                    uidList.add(setDefaultClass.getUid());
                }
                connection.getDbSchool().child(savedData.getValue("schoolId")).child("student")
                        .child(savedData.getValue("stClass")).removeEventListener(this);

                // go for daily record
                connection.getDbSchool().child(savedData.getValue("schoolId")).child("attendance")
                        .child(savedData.getValue("stClass")).child(month).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            increment = 1;
                            date = dataSnapshot1.getKey().substring(8, 10);
                            dateUnTrimmed = dataSnapshot1.getKey();
                            biHashMap.put("uid", date, date);
                            arrayListDate.add(date);
                            for (DataSnapshot dataSnapshot2 : dataSnapshot1.child(savedData.getType()).getChildren()) {
                                Attendance attendance = dataSnapshot2.child("1").getValue(Attendance.class);
                                if (attendance != null) {
                                    biHashMap.put(attendance.getUid(), date, attendance.getAttendance());
                                }
                                Attendance attendance2 = dataSnapshot2.child("2").getValue(Attendance.class);
                                if (attendance2 != null) {
                                    if( biHashMap.get(attendance2.getUid(), date)==null){
                                        biHashMap.put(attendance2.getUid(), date, "-" + attendance2.getAttendance());
                                    }else{
                                        biHashMap.put(attendance2.getUid(), date, biHashMap.get(attendance2.getUid(), date) + attendance2.getAttendance());
                                    }
                                }
                                if (dataSnapshot2.child("2").hasChildren()) {
                                    increment = 2;
                                }
                            }
                            totalCount += increment;
                            for (String id : uidList) {
                                if (biHashMap.get(id, date) == null) {
                                } else {
                                    if (biHashMap.get(id, date).equals("p")) {
                                        mapAttendanceCount.put(id, mapAttendanceCount.get(id) + 1);
                                    } else if (biHashMap.get(id, date).equals("pp")) {
                                        mapAttendanceCount.put(id, mapAttendanceCount.get(id) + 2);
                                    } else if (biHashMap.get(id, date).equals("-p")) {
                                        mapAttendanceCount.put(id, mapAttendanceCount.get(id) + 1);
                                    } else if (biHashMap.get(id, date).equals("ap")) {
                                        mapAttendanceCount.put(id, mapAttendanceCount.get(id) + 1);
                                    } else if (biHashMap.get(id, date).equals("pa")) {
                                        mapAttendanceCount.put(id, mapAttendanceCount.get(id) + 1);
                                    }
                                }
                            }
                        }
                        // printing attendance count
                        biHashMap.put("uid", "32", "total");
                        for (String id : uidList) {
                            if (mapAttendanceCount.get(id) == null) {
                            } else {
                                biHashMap.put(id, "32", mapAttendanceCount.get(id) + "/" + String.valueOf(totalCount));
                            }
                        }
                        for (String id : uidList) {
                            if (mapAttendanceCount.get(id) == null) {
                            } else {
                                biHashMap.put(id, "33", String.format("%.2f", (float) mapAttendanceCount.get(id) / (float) (totalCount) * 100) + "%");
                            }
                        }
                        connection.getDbSchool().child(savedData.getValue("schoolId")).child("attendance")
                                .child(savedData.getValue("stClass")).child(month).removeEventListener(this);
                        savedData.removeAlert();
                        createView();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createView() {
        int p=0,a=0;
        String key=new SimpleDateFormat("dd").format(date1);
        tableAdapterList.clear();
        tableAdapterList2.clear();
        for (String id:uidList){
            if(biHashMap.get(id,key)!=null){
                if(biHashMap.get(id,key).startsWith("p")||biHashMap.get(id,key).startsWith("-p")){
                    RecyclerClass recyclerClass=new RecyclerClass(biHashMap.get(id,"date"),biHashMap.get(id,key),biHashMap.get(id,"32"),biHashMap.get(id,"33"));
                    recyclerClass.setHeading(id);
                    tableAdapterList.add(recyclerClass);
                    p++;
                }
            }
        }
        for (String id:uidList){
            if(biHashMap.get(id,key)!=null){
            if(biHashMap.get(id,key).startsWith("a")||biHashMap.get(id,key).startsWith("-a")){
                RecyclerClass recyclerClass=new RecyclerClass(biHashMap.get(id,"date"),biHashMap.get(id,key),biHashMap.get(id,"32"),biHashMap.get(id,"33"));
                recyclerClass.setHeading(id);
                tableAdapterList2.add(recyclerClass);
                for(String gid:mapGuradian.get(id)){
                    connection.getDbUser().child(gid).child("softNotification")
                            .child(id).setValue(biHashMap.get(id,"date")+" is absent");
                }
                a++;
            }
        }
        }
        tableAdapter2.notifyDataSetChanged();
        tableAdapter.notifyDataSetChanged();
        TextView tvTotal=findViewById(R.id.tvTotal);
        TextView tvPresent=findViewById(R.id.tvPresent);
        TextView tvAbsent=findViewById(R.id.tvAbsent);
        tvPresent.setText("Present : "+String.valueOf(p));
        tvAbsent.setText("Absent : "+String.valueOf(a));
        tvTotal.setText("Total : "+String.valueOf(uidList.size()));
    }

    public void Notify(View view) {
        if(clickable==false){
            savedData.toast("please refresh first");
            btnNotify.setError("Please refresh first");
            return;
        }
        String date=new SimpleDateFormat("dd").format(date1);
        for (String id:uidList){
            if(biHashMap.get(id,date)!=null){
                if(biHashMap.get(id,date).startsWith("a")){
                    for(String gid:mapGuradian.get(id)){
                        connection.getDbUser().child(gid).child("hardNotification")
                                .child(biHashMap.get(id,"date")+" is absent").setValue(id);
                    }
                }
            }
        }
        savedData.toast("Guardians are notified");
    }


    public void refresh(View view) {
        btnRefresh.setText("refreshing...");
        onStart();
    }

    public void export(View view) {
        startActivity(new Intent(this, ExportToPdfActivity.class));
    }
}