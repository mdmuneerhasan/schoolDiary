package com.multi.schooldiary.teacher;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.multi.schooldiary.R;
import com.multi.schooldiary.activity.SetDefaultActivity;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.RecyclerAdapter;
import com.multi.schooldiary.utility.RecyclerClass;
import com.multi.schooldiary.utility.RecyclerClick;
import com.multi.schooldiary.utility.SetDefaultClass;
import com.multi.schooldiary.utility.Storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AttendanceRegisterActivity extends AppCompatActivity {
    StringBuilder names;
    String name;
    ArrayList<String> arrayList;
    Storage storage;
    Connection connection;
    TextView tvNames;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    ArrayList<String> uidList;
    Map <String,String> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_register);
        names=new StringBuilder("names(roll no.)\n");
        uidList=new ArrayList<>();
        arrayList=new ArrayList<>();
        storage=new Storage(this);
        connection=new Connection();
        map=new HashMap<>();
        tvNames=findViewById(R.id.tvNames);
        recyclerView=findViewById(R.id.recycle);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,1);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        myAdapter=new MyAdapter(arrayList);
        recyclerView.setAdapter(myAdapter);


    }

    @Override
    protected void onStart() {
        storage.showAlert("connecting...");
        super.onStart();
        connection.getDbSchool().child(storage.getValue("schoolId")).child("student")
                .child(storage.getValue("stClass")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uidList.clear();
                names.setLength(0);
                names.append("names\n");
                map.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    SetDefaultClass setDefaultClass=dataSnapshot1.getValue(SetDefaultClass.class);
                    name=dataSnapshot1.child("name").getValue(String.class);
                    uidList.add(setDefaultClass.getUid());
                    String set=name+"("+setDefaultClass.getRollNo()+")"+setDefaultClass.getUid();
                    map.put(setDefaultClass.getUid(),set.substring(0,set.length()>=20?20:set.length())+"\n");

                }
                for(String id:uidList){
                    names.append(map.get(id));
                }


                myAdapter.notifyDataSetChanged();
                tvNames.setText(names);
                storage.removeAlert();
                // go for daily record

                connection.getDbSchool().child(storage.getValue("schoolId")).child("attendance")
                        .child(storage.getValue("stClass")).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        arrayList.clear();
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            names=new StringBuilder();
                            map.clear();
                            names.append(dataSnapshot1.getKey()+"\n");
                            for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                                Attendance attendance=dataSnapshot2.getValue(Attendance.class);
                                map.put(attendance.getUid(),attendance.getAttendance()+"\n");
                            }
                            for(String id:uidList){
                                if(map.get(id)==null){
                                    names.append("-\n");
                                }else {
                                    names.append(map.get(id));
                                }
                            }


                            arrayList.add(names.toString());
                        }
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
}
