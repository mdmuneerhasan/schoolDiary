package com.multi.schooldiary.dashboard.teacher;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.multi.schooldiary.R;
import com.multi.schooldiary.activity.MyToolBar;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.SavedData;
import com.multi.schooldiary.utility.SetDefaultClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AttendanceRegisterActivity extends AppCompatActivity {
    StringBuilder names;
    String name,date,month;
    ArrayList<String> arrayList,arrayListDate;
    SavedData savedData;
    Connection connection;
    TextView tvNames;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    ArrayList<String> uidList;
    Map <String,String> map,map2;
    int totalCount=0,increment=1;
    Map <String,Integer> mapAttendanceCount;
    MyAdapterClick myAdapterClick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_register);


        MyToolBar myToolBar= new MyToolBar((Toolbar) findViewById(R.id.toolbar), this) {
            @Override
            public void onAlertActionPerformed() {
            }
        };
        setSupportActionBar(myToolBar.getToolBar());
        myAdapterClick=new MyAdapterClick() {
            @Override
            public void onBindViewHolder(final MyAdapter.Holder holder, String s) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{
                            startActivity(new Intent(getBaseContext(),AttendanceTodayActivity.class)
                                    .putExtra("date",arrayListDate.get(holder.getAdapterPosition())));
                        }catch (Exception e){

                        }
                    }
                });

            }
        };
        names=new StringBuilder("names(roll no.)\n");
        uidList=new ArrayList<>();
        arrayList=new ArrayList<>();
        arrayListDate=new ArrayList<>();
        savedData =new SavedData(this);
        connection=new Connection();
        map=new HashMap<>();
        map2=new HashMap<>();
        mapAttendanceCount=new HashMap<>();
        tvNames=findViewById(R.id.tvNames);
        recyclerView=findViewById(R.id.recycle);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,1);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        myAdapter=new MyAdapter(arrayList,myAdapterClick);
        recyclerView.setAdapter(myAdapter);
        Date date1=new Date(System.currentTimeMillis());
        month=new SimpleDateFormat("yyyy-MM").format(date1);
        TextView tvDate=findViewById(R.id.tvDate);
        tvDate.setText(new SimpleDateFormat("MMM-yyyy").format(date1));

    }

    @Override
    protected void onStart() {
        savedData.showAlert("connecting...");
        super.onStart();
        totalCount=0;
        // fetching names
        connection.getDbSchool().child(savedData.getValue("schoolId")).child("student")
                .child(savedData.getValue("stClass")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uidList.clear();
                names.setLength(0);
                names.append("names(roll no.)\n");
                map.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    SetDefaultClass setDefaultClass=dataSnapshot1.getValue(SetDefaultClass.class);
                    name=dataSnapshot1.child("name").getValue(String.class);
                    uidList.add(setDefaultClass.getUid());
                    String set=name+"("+setDefaultClass.getRollNo()+")"+setDefaultClass.getUid();
                    map.put(setDefaultClass.getUid(),set.substring(0,set.length()>=20?20:set.length())+"\n");
                    mapAttendanceCount.put(setDefaultClass.getUid(),0);
                }
                connection.getDbSchool().child(savedData.getValue("schoolId")).child("student")
                        .child(savedData.getValue("stClass")).removeEventListener(this);
                // sorting can be performed here


                for(String id:uidList){
                    names.append(map.get(id));
                }
                myAdapter.notifyDataSetChanged();
                tvNames.setText(names);
                savedData.removeAlert();
                // go for daily record
                connection.getDbSchool().child(savedData.getValue("schoolId")).child("attendance")
                        .child(savedData.getValue("stClass")).child(month).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        arrayList.clear();
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            names=new StringBuilder();
                            map.clear();
                            map2.clear();
                            increment=1;
                            date=dataSnapshot1.getKey().substring(8,10);
                            arrayListDate.add(dataSnapshot1.getKey());
                            names.append(date+"\n");
                            for(DataSnapshot dataSnapshot2:dataSnapshot1.child(savedData.getType()).getChildren()){
                                Attendance attendance=dataSnapshot2.child("1").getValue(Attendance.class);
                                if(attendance!=null){
                                    map.put(attendance.getUid(),attendance.getAttendance());
                                }
                                Attendance attendance2=dataSnapshot2.child("2").getValue(Attendance.class);
                                if(attendance2!=null){
                                    map2.put(attendance2.getUid(),attendance2.getAttendance());
                                }
                                if(dataSnapshot2.child("2").hasChildren()){
                                    increment=2;
                                }
                            }
                            totalCount+=increment;
                            for(String id:uidList){
                                if(map.get(id)==null){
                                    names.append("-");
                                }else {
                                    if(map.get(id).equals("p")){
                                        mapAttendanceCount.put(id,mapAttendanceCount.get(id)+1);
                                    }
                                    names.append(map.get(id));
                                }

                                if(map2.get(id)==null){
                                    names.append("");
                                }else {
                                    if(map2.get(id).equals("p")){
                                        mapAttendanceCount.put(id,mapAttendanceCount.get(id)+1);
                                    }
                                    names.append(map2.get(id));
                                }
                                names.append("\n");
                            }


                            arrayList.add(names.toString());
                        }
                        // printing attendance count
                        names.setLength(0);
                        names.append("total\n");
                        for(String id:uidList){
                            if(mapAttendanceCount.get(id)==null){
                                names.append("-\n");
                            }else {
                                names.append(mapAttendanceCount.get(id)+"/"+String.valueOf(totalCount)+"\n");
                            }
                        }
                        arrayList.add(names.toString());
                        // printing percentage count
                        names.setLength(0);
                        names.append("percentage\n");
                        for(String id:uidList){
                            if(mapAttendanceCount.get(id)==null){
                                names.append("-\n");
                            }else {
                                names.append(String.format("%.2f",(float)mapAttendanceCount.get(id)/(float)(totalCount)*100)+"%\n");
                            }
                        }
                        arrayList.add(names.toString());
                        connection.getDbSchool().child(savedData.getValue("schoolId")).child("attendance")
                                .child(savedData.getValue("stClass")).removeEventListener(this);
                        myAdapter.notifyDataSetChanged();
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

    public void exportActivity(View view) {
        startActivity(new Intent(this,ExportToPdfActivity.class));
    }
}
