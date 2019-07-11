package com.multi.schooldiary.dashboard.teacher;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.multi.schooldiary.R;
import com.multi.schooldiary.activity.Click;
import com.multi.schooldiary.activity.MyToolBar;
import com.multi.schooldiary.activity.SetDefaultAdapter;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.SavedData;
import com.multi.schooldiary.utility.SetDefaultClass;

import java.util.ArrayList;


public class GuardianRequestActivity extends AppCompatActivity  {
    SetDefaultAdapter setDefaultAdapter;
    ArrayList<SetDefaultClass> arrayList;
    private Click click;
    SavedData savedData;
    Connection connection;
    RecyclerView recyclerView;
    private String name;
    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);
        savedData =new SavedData(this);
        arrayList=new ArrayList<>();
        connection=new Connection();
        recyclerView=findViewById(R.id.recycle);
        savedData.showAlert("connecting...");
        click=new Click() {
            @Override
            public void onBindViewHolder(final SetDefaultAdapter.Holder holder, final SetDefaultClass setDefaultClass) {
                holder.btn1.setVisibility(View.VISIBLE);
                holder.btn1.setText("set as guardian");
                holder.btn2.setVisibility(View.VISIBLE);
                holder.btn2.setText("remove request");
                holder.tvName.setText(setDefaultClass.getSchoolName());
                holder.tvPosition.setText("Guardian of "+setDefaultClass.getRollNo());
                holder.tvPosition.setVisibility(View.VISIBLE);
                holder.btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.btn1.setText("updating..");
                        connection.getDbSchool().child(savedData.getValue("schoolId")).child("guardian")
                                .child(savedData.getValue("stClass")).child(setDefaultClass.getUid()).removeValue();
                        connection.getDbSchool().child(savedData.getValue("schoolId")).child("student")
                                .child(savedData.getValue("stClass")).child(setDefaultClass.getUid()).child("guardian")
                                .child(setDefaultClass.getSchoolId()).child(setDefaultClass.getUid()).setValue(setDefaultClass.getSchoolName())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        holder.btn1.setText("profile updated");
                                        holder.btn2.setText("reject request");

                                    }
                                });
                    }
                });
                holder.btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.btn2.setText("rejecting..");
                        connection.getDbSchool().child(savedData.getValue("schoolId")).child("guardian")
                                .child(savedData.getValue("stClass")).child(setDefaultClass.getUid()).removeValue();
                        connection.getDbSchool().child(savedData.getValue("schoolId")).child("student")
                                .child(savedData.getValue("stClass")).child(setDefaultClass.getUid()).child("guardian")
                                .child(setDefaultClass.getSchoolId()).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        holder.btn2.setText("request rejected");
                                        holder.btn1.setText("accept ");
                                    }
                                });

                    }
                });
            }
        };
        MyToolBar myToolBar= new MyToolBar((Toolbar) findViewById(R.id.toolbar), this) {
            @Override
            public void onAlertActionPerformed() {
            }
        };
        setSupportActionBar(myToolBar.getToolBar());

        setDefaultAdapter=new SetDefaultAdapter(this,click,arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(setDefaultAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                    for(DataSnapshot dataSnapshot2:dataSnapshot.getChildren()){
                        SetDefaultClass setDefaultClass=new SetDefaultClass();
                        setDefaultClass.setSchoolId(dataSnapshot2.child("guardianUid").getValue(String.class));
                        setDefaultClass.setSchoolName(dataSnapshot2.child("guardianName").getValue(String.class));
                        setDefaultClass.setUid(dataSnapshot2.child("studentUid").getValue(String.class));
                        setDefaultClass.setRollNo(dataSnapshot2.child("studentName").getValue(String.class));
                        arrayList.add(setDefaultClass);
                    }
                setDefaultAdapter.notifyDataSetChanged();
                savedData.removeAlert();
                connection.getDbSchool().child(savedData.getValue("schoolId")).child("guardian")
                        .child(savedData.getValue("stClass")).removeEventListener(valueEventListener);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        connection.getDbSchool().child(savedData.getValue("schoolId")).child("guardian")
                .child(savedData.getValue("stClass")).addValueEventListener(valueEventListener);
    }
}
