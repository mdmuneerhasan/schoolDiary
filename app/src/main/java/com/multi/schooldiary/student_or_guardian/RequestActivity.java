package com.multi.schooldiary.student_or_guardian;

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


public class RequestActivity extends AppCompatActivity  {
    SetDefaultAdapter setDefaultAdapter;
    ArrayList<SetDefaultClass> arrayList;
    private Click click;
    Storage storage;
    Connection connection;
    RecyclerView recyclerView;
    private String name;
    ValueEventListener valueEventListener;
    String id ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);
        storage=new Storage(this);
        arrayList=new ArrayList<>();
        connection=new Connection();
        recyclerView=findViewById(R.id.recycle);
        storage.showAlert("connecting...");
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

                        connection.getDbSchool().child(storage.getValue("schoolId")).child("guardian")
                                .child(storage.getValue("stClass")).child(setDefaultClass.getUid())
                                .child("guardianUid").setValue(setDefaultClass.getUid());
                        connection.getDbSchool().child(storage.getValue("schoolId")).child("guardian")
                                .child(storage.getValue("stClass")).child(setDefaultClass.getUid())
                                .child("guardianName").setValue(name);
                        connection.getDbSchool().child(storage.getValue("schoolId")).child("guardian")
                                .child(storage.getValue("stClass")).child(setDefaultClass.getUid())
                                .child("studentUid").setValue(storage.getValue("uid"));
                        connection.getDbSchool().child(storage.getValue("schoolId")).child("guardian")
                                .child(storage.getValue("stClass")).child(setDefaultClass.getUid())
                                .child("studentName").setValue(storage.getValue("name"));
                        storage.toast(storage.getValue("uid")+storage.getValue("name"));
                        // updating request
                        connection.getDbUser().child(storage.getValue("uid"))
                                .child("guardian").child(setDefaultClass.getUid()).setValue(true);
                        connection.getDbUser().child(setDefaultClass.getUid())
                                .child("children").child(storage.getValue("uid")).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                        connection.getDbSchool().child(storage.getValue("schoolId")).child("guardian")
                                .child(storage.getValue("stClass")).child(setDefaultClass.getUid())
                                .removeValue();
                        connection.getDbUser().child(storage.getValue("uid"))
                                .child("guardian").child(setDefaultClass.getUid()).setValue(false);
                        connection.getDbUser().child(setDefaultClass.getUid())
                                .child("children").child(storage.getValue("uid")).setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                storage.removeAlert();
                connection.getDbUser().child(storage.getValue("uid"))
                        .child("guardian").removeEventListener(valueEventListener);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        connection.getDbUser().child(storage.getValue("uid"))
                .child("guardian").addValueEventListener(valueEventListener);

    }
}
