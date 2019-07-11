package com.multi.schooldiary.activity;

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
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.SavedData;
import com.multi.schooldiary.utility.SetDefaultClass;

import java.util.ArrayList;

public class SetDefaultActivity extends AppCompatActivity {
    ArrayList<SetDefaultClass> arrayList;
    Connection connection;
    SavedData savedData;
    SetDefaultAdapter setDefaultAdapter;
    RecyclerView recyclerView;
    String[] positionList;
    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_default);
        arrayList=new ArrayList<>();
        connection=new Connection();
        positionList=new String[]{"none","none","student","parent","monitor","teacher","vice president","principal"};
        savedData =new SavedData(this);
        recyclerView=findViewById(R.id.recycle);
        MyToolBar myToolBar= new MyToolBar((Toolbar) findViewById(R.id.toolbar), this) {
            @Override
            public void onAlertActionPerformed() {
            }
        };
        setSupportActionBar(myToolBar.getToolBar());

        Click click=new Click() {
            @Override
            public void onBindViewHolder(SetDefaultAdapter.Holder holder, final SetDefaultClass setDefaultClass) {
                if(setDefaultClass.getPermission()){
                    holder.tvPosition.setText(positionList[Integer.parseInt(setDefaultClass.getPosition())]+"(verified)");
                }else{
                    holder.tvPosition.setText(positionList[Integer.parseInt(setDefaultClass.getPosition())]+"(not verified)");
                }
                holder.tvPosition.setVisibility(View.VISIBLE);
                holder.tvName.setText(setDefaultClass.getSchoolName()+" ("+setDefaultClass.getSchoolId()+")");
                holder.btn1.setVisibility(View.VISIBLE);
                holder.btn1.setText("Set as default");
                holder.btn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                    public void onClick(View v) {
                        if(setDefaultClass.getPermission()){
                            savedData.showAlert("setting profile...");
                            savedData.setValue("schoolId",setDefaultClass.getSchoolId());
                            savedData.setValue("position",setDefaultClass.getPosition());
                            savedData.setValue("stClass",setDefaultClass.getStClass());
                            savedData.setValue("sid",setDefaultClass.getSid());
                            connection.getDbSchool().child(setDefaultClass.getSchoolId())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot!=null){
                                        savedData.setValue("deptType",dataSnapshot.child("deptType").getValue(String.class));
                                        savedData.setValue("schoolPhoto",dataSnapshot.child("schoolPhoto").getValue(String.class));
                                        String name=dataSnapshot.child("name").getValue(String.class);
                                        if(name!=null)
                                        if(name.length()>0){
                                            savedData.setValue("schoolName",name);
                                        }
                                    }
                                    savedData.removeAlert();
                                    savedData.toast(setDefaultClass.getSchoolName()+" is set as default");
                                    connection.getDbSchool().child(setDefaultClass.getSchoolId()).child("type")
                                            .removeEventListener(this);
                                    startActivity(new Intent(getBaseContext(),MainActivity.class));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }else {
                            savedData.setValue("schoolId","");
                            savedData.toast("not allowed");
                        }
                    }
                });
            }

        };
        setDefaultAdapter=new SetDefaultAdapter(this,click,arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(setDefaultAdapter);
        savedData.showAlert("connecting...");


        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null){
                    savedData.setValue("progress","1");
                    savedData.toast("please add a account first");
                    finish();
                    }else{
                    savedData.setValue("progress","2");
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                            SetDefaultClass setDefaultClass=dataSnapshot2.getValue(SetDefaultClass.class);
                            arrayList.add(setDefaultClass);
                        }
                    }
                    setDefaultAdapter.notifyDataSetChanged();
                }
                connection.getDbUser().child(savedData.getValue("uid")).child("permission")
                        .removeEventListener(valueEventListener);
                savedData.removeAlert();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        arrayList.clear();
        connection.getDbUser().child(savedData.getValue("uid")).child("permission")
                .addValueEventListener(valueEventListener);
    }

}
