package com.multi.schooldiary.teacher;

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
import com.multi.schooldiary.utility.RecyclerAdapter;
import com.multi.schooldiary.utility.RecyclerClass;
import com.multi.schooldiary.utility.RecyclerClick;
import com.multi.schooldiary.utility.Storage;
import com.multi.schooldiary.utility.User;

import java.util.ArrayList;

public class ManageClassActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    static ArrayList<RecyclerClass> arrayList;
    RecyclerAdapter recyclerAdapter;
    RecyclerClick click;
    Storage storage;
    Connection connection;
    RecyclerClass recyclerClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);
        recyclerView=findViewById(R.id.recycle);
        arrayList=new ArrayList<>();
        storage=new Storage(this);
        connection=new Connection();
        storage.showAlert("connecting...");
        click=new RecyclerClick() {
            @Override
            public void onBindViewHolder(RecyclerAdapter.Holder holder, RecyclerClass recyclerClass) {
                holder.tvHeading.setVisibility(View.VISIBLE);
                holder.imageView.setVisibility(View.VISIBLE);

                holder.text1.setVisibility(View.VISIBLE);
                holder.text2.setVisibility(View.VISIBLE);
//                holder.text3.setVisibility(View.VISIBLE);

                holder.btn1.setText("ok");
                holder.btn1.setVisibility(View.VISIBLE);

            }
        };
        recyclerAdapter=new RecyclerAdapter(this,click,arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();

        connection.getDbSchool().child(storage.getValue("schoolId")).child("student")
                .child(storage.getValue("stClass")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    String id=dataSnapshot1.child("uid").getValue(String.class);
                    connection.getDbUser().child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                            User user2=dataSnapshot3.getValue(User.class);
                            recyclerClass=new RecyclerClass();
                            recyclerClass.setHeading(user2.getName());
                            recyclerClass.setUrl(user2.getPhotoUrl());
                            recyclerClass.setText1(user2.getNumber());
                            arrayList.add(recyclerClass);
                            recyclerAdapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });

                }
                storage.removeAlert();
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
