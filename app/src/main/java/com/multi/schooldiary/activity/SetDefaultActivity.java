package com.multi.schooldiary.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.multi.schooldiary.R;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.SetDefaultClass;
import com.multi.schooldiary.utility.Storage;

import java.util.ArrayList;

public class SetDefaultActivity extends AppCompatActivity {
    ArrayList<SetDefaultClass> arrayList;
    Connection connection;
    Storage storage;
    SetDefaultAdapter setDefaultAdapter;
    RecyclerView recyclerView;
    String[] positionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_default);
        arrayList=new ArrayList<>();
        connection=new Connection();
        positionList=new String[]{"none","none","student","parent","monitor","teacher","vice president","principal"};
        storage=new Storage(this);
        recyclerView=findViewById(R.id.recycle);
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
                            storage.setValue("schoolId",setDefaultClass.getSchoolId());
                            storage.setValue("schoolName",setDefaultClass.getSchoolName());
                            storage.setValue("position",setDefaultClass.getPosition());
                            storage.setValue("stClass",setDefaultClass.getStClass());
                            storage.toast(setDefaultClass.getSchoolName()+" is set as default");
                            startActivity(new Intent(getBaseContext(),MainActivity.class));
                        }else {
                            storage.setValue("schoolId","");
                            storage.toast("not allowed");

                        }
                    }
                });
            }

        };
        setDefaultAdapter=new SetDefaultAdapter(this,click,arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(setDefaultAdapter);
        storage.showAlert("connecting...");

    }

    @Override
    protected void onStart() {
        super.onStart();
        arrayList.clear();
        connection.getDbUser().child(storage.getValue("uid")).child("permission")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue()==null){
                            storage.setValue("schoolId","");
                            finish();
                        }else{
                            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                                    SetDefaultClass setDefaultClass=dataSnapshot2.getValue(SetDefaultClass.class);
                                    arrayList.add(setDefaultClass);
                                }
                            }
                            setDefaultAdapter.notifyDataSetChanged();
                        }
                        storage.removeAlert();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
