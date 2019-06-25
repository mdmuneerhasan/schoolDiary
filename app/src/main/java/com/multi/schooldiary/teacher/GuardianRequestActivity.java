package com.multi.schooldiary.teacher;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.multi.schooldiary.R;
import com.multi.schooldiary.activity.Click;
import com.multi.schooldiary.activity.SetDefaultAdapter;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.SetDefaultClass;
import com.multi.schooldiary.utility.Storage;

import java.util.ArrayList;


public class GuardianRequestActivity extends AppCompatActivity  {
    SetDefaultAdapter setDefaultAdapter;
    ArrayList<SetDefaultClass> arrayList;
    private Click click;
    Storage storage;
    private String[] positionList;
    Connection connection;
    RecyclerView recyclerView;
    private String name;
    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);
        storage=new Storage(this);
        arrayList=new ArrayList<>();
        connection=new Connection();
        positionList=new String[]{"none","none","student","parent","monitor","teacher","vice president","principal"};
        recyclerView=findViewById(R.id.recycle);
        storage.showAlert("connecting...");
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
                        connection.getDbSchool().child(storage.getValue("schoolId")).child("guardian")
                                .child(storage.getValue("stClass")).child(setDefaultClass.getSchoolId()).removeValue();
                        connection.getDbSchool().child(storage.getValue("schoolId")).child("student")
                                .child(storage.getValue("stClass")).child(setDefaultClass.getUid()).child("guardian")
                                .child(setDefaultClass.getSchoolId()).setValue(setDefaultClass.getSchoolName())
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
                        connection.getDbSchool().child(storage.getValue("schoolId")).child("guardian")
                                .child(storage.getValue("stClass")).child(setDefaultClass.getSchoolId()).removeValue();
                        connection.getDbSchool().child(storage.getValue("schoolId")).child("student")
                                .child(storage.getValue("stClass")).child(setDefaultClass.getUid()).child("guardian")
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
                storage.removeAlert();
                connection.getDbSchool().child(storage.getValue("schoolId")).child("guardian")
                        .child(storage.getValue("stClass")).removeEventListener(valueEventListener);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        connection.getDbSchool().child(storage.getValue("schoolId")).child("guardian")
                .child(storage.getValue("stClass")).addValueEventListener(valueEventListener);
    }
}
