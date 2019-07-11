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


public class StudentRequestActivity extends AppCompatActivity  {
    SetDefaultAdapter setDefaultAdapter;
    ArrayList<SetDefaultClass> arrayList;
    private Click click;
    SavedData savedData;
    Connection connection;
    RecyclerView recyclerView;
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
                holder.btn1.setText("Accept");
                holder.btn2.setVisibility(View.VISIBLE);
                holder.btn2.setText("Reject");
                holder.tvPosition.setVisibility(View.VISIBLE);
                holder.tvName.setText(setDefaultClass.getName());
                holder.tvPosition.setText("requesting for "+setDefaultClass.getStClass());
                holder.btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.btn1.setText("accepting..");
                        setDefaultClass.setPermission(true);
                        connection.getDbSchool().child(savedData.getValue("schoolId")).child("classRequest")
                                .child(setDefaultClass.getStClass()).child(setDefaultClass.getUid()).removeValue();
                        connection.getDbSchool().child(savedData.getValue("schoolId")).child("student")
                                .child(setDefaultClass.getStClass()).child(setDefaultClass.getUid()).setValue(setDefaultClass);
                        connection.getDbUser().child(setDefaultClass.getUid())
                                .child("permission").child(setDefaultClass.getSchoolId())
                                .child(setDefaultClass.getPosition()).setValue(setDefaultClass)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
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
                        connection.getDbUser().child(setDefaultClass.getUid())
                                .child("permission").child(setDefaultClass.getSchoolId())
                                .child(setDefaultClass.getPosition()).removeValue();
                        connection.getDbSchool().child(savedData.getValue("schoolId")).child("student")
                                .child(setDefaultClass.getStClass()).child(setDefaultClass.getUid()).removeValue();
                        connection.getDbSchool().child(savedData.getValue("schoolId")).child("classRequest")
                                .child(setDefaultClass.getStClass()).child(setDefaultClass.getUid())
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        holder.btn2.setText("rejected");
                                        holder.btn1.setText("accept");
                                    }
                                });

                    }
                });
            }
        };

        setDefaultAdapter=new SetDefaultAdapter(this,click,arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(setDefaultAdapter);
        MyToolBar myToolBar= new MyToolBar((Toolbar) findViewById(R.id.toolbar), this) {
            @Override
            public void onAlertActionPerformed() {
            }
        };
        setSupportActionBar(myToolBar.getToolBar());


    }

    @Override
    protected void onStart() {
        super.onStart();
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                    for(DataSnapshot dataSnapshot2:dataSnapshot.getChildren()){
                        SetDefaultClass setDefaultClass=dataSnapshot2.getValue(SetDefaultClass.class);
                        arrayList.add(setDefaultClass);
                    }
                setDefaultAdapter.notifyDataSetChanged();
                savedData.removeAlert();
                connection.getDbSchool().child(savedData.getValue("schoolId")).child("classRequest")
                        .child(savedData.getValue("stClass")).removeEventListener(valueEventListener);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        connection.getDbSchool().child(savedData.getValue("schoolId")).child("classRequest")
                .child(savedData.getValue("stClass")).addValueEventListener(valueEventListener);
    }
}
