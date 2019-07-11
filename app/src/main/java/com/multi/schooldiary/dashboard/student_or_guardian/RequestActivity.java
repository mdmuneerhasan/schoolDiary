package com.multi.schooldiary.dashboard.student_or_guardian;

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
import com.multi.schooldiary.utility.SetDefaultClass;
import com.multi.schooldiary.utility.SavedData;

import java.util.ArrayList;


public class RequestActivity extends AppCompatActivity  {
    SetDefaultAdapter setDefaultAdapter;
    ArrayList<SetDefaultClass> arrayList;
    private Click click;
    SavedData savedData;
    Connection connection;
    RecyclerView recyclerView;
    private String name;
    ValueEventListener valueEventListener,valueEventListener2;
    String id ;
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
                holder.btn1.setText("Accept");
                holder.btn2.setVisibility(View.VISIBLE);
                holder.btn2.setText("Reject");
                holder.tvPosition.setText("Guardian account link request");
                holder.tvPosition.setVisibility(View.VISIBLE);
                id=setDefaultClass.getUid();
                connection.getDbUser().child(id)
                        .child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        name=dataSnapshot.getValue(String.class);
                        holder.tvName.setText(name);
                        connection.getDbUser().child(id)
                                .child("name").removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                holder.btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.btn1.setText("accepting..");
                        setDefaultClass.setPermission(true);
                        //sending request to school

                        connection.getDbSchool().child(savedData.getValue("schoolId")).child("guardian")
                                .child(savedData.getValue("stClass")).child(savedData.getValue("uid"))
                                .child("guardianUid").setValue(setDefaultClass.getUid());
                        connection.getDbSchool().child(savedData.getValue("schoolId")).child("guardian")
                                .child(savedData.getValue("stClass")).child(savedData.getValue("uid"))
                                .child("guardianName").setValue(name);
                        connection.getDbSchool().child(savedData.getValue("schoolId")).child("guardian")
                                .child(savedData.getValue("stClass")).child(savedData.getValue("uid"))
                                .child("studentUid").setValue(savedData.getValue("uid"));
                        connection.getDbSchool().child(savedData.getValue("schoolId")).child("guardian")
                                .child(savedData.getValue("stClass")).child(savedData.getValue("uid"))
                                .child("studentName").setValue(savedData.getValue("name"));
                        // updating request
                        connection.getDbUser().child(savedData.getValue("uid"))
                                .child("guardian").child(setDefaultClass.getUid()).setValue(true);
                        connection.getDbUser().child(setDefaultClass.getUid())
                                .child("children").child(savedData.getValue("uid")).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                holder.btn1.setText("accepted");
                                holder.btn2.setText("reject");
                            }
                        });
                    }
                });
                holder.btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.btn2.setText("rejecting..");
                        setDefaultClass.setPermission(false);
                        connection.getDbSchool().child(savedData.getValue("schoolId")).child("guardian")
                                .child(savedData.getValue("stClass")).child(setDefaultClass.getUid())
                                .removeValue();
                        connection.getDbUser().child(savedData.getValue("uid"))
                                .child("guardian").child(setDefaultClass.getUid()).setValue(false);
                        connection.getDbUser().child(setDefaultClass.getUid())
                                .child("children").child(savedData.getValue("uid")).setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                holder.btn1.setText("accept");
                                holder.btn2.setText("rejected");
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
                        setDefaultClass.setUid(dataSnapshot2.getKey());
                        arrayList.add(setDefaultClass);
                    }
                setDefaultAdapter.notifyDataSetChanged();
                savedData.removeAlert();
                connection.getDbUser().child(savedData.getValue("uid"))
                        .child("guardian").removeEventListener(valueEventListener);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        connection.getDbUser().child(savedData.getValue("uid"))
                .child("guardian").addValueEventListener(valueEventListener);

    }
}
