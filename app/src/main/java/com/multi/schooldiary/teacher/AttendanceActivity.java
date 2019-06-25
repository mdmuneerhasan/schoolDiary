package com.multi.schooldiary.teacher;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
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

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AttendanceActivity extends AppCompatActivity {
    Storage storage;
    Connection connection;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    ArrayList<RecyclerClass> arrayList;
    RecyclerClick recyclerClick;
    String datedate,schoolId,stClass, stText1,stText2,stText3,stText4;
    TextView tvDate,tvText1,tvText2,tvText3;
    int cl1,cl2,cl3,cl4;
 //   String stText1,stText2,stText3;
//    int clText1,clText2,clText3;
    int previousPosition=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendece);
        tvDate=findViewById(R.id.tvDate);
        tvText1=findViewById(R.id.text1);
        tvText2=findViewById(R.id.text2);
        tvText3=findViewById(R.id.text3);
        recyclerClick=new RecyclerClick() {
            @Override
            public void onBindViewHolder(final RecyclerAdapter.Holder holder, final RecyclerClass recyclerClass) {
                LinearLayout.LayoutParams btnLayout=new LinearLayout.LayoutParams(0,500);
                btnLayout.weight=1;
                btnLayout.setMargins(5,200,5,5);
                holder.text3.setVisibility(View.VISIBLE);
                holder.text1.setVisibility(View.VISIBLE);
                holder.btn1.setVisibility(View.VISIBLE);
                holder.btn2.setVisibility(View.VISIBLE);
                holder.btn1.setText("present");
                holder.btn2.setText("absent");
                holder.btn1.setLayoutParams(btnLayout);
                holder.btn2.setLayoutParams(btnLayout);
                if(holder.getAdapterPosition()<previousPosition){
                    //remove value
                }
                try{
                    if(Integer.parseInt(recyclerClass.getText4())==1){
                        holder.btn1.setBackgroundColor(Color.GREEN);
                        holder.btn2.setBackgroundColor(Color.GRAY);
                    }else{
                        holder.btn1.setBackgroundColor(Color.GRAY);
                        holder.btn2.setBackgroundColor(Color.RED);
                    }
                }catch (Exception e){
                    holder.btn1.setBackgroundColor(Color.GRAY);
                    holder.btn2.setBackgroundColor(Color.GRAY);
                }
                holder.btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*
                        * heading=name;
                        * text1=roll no;
                        * text2=uid;
                        * text3=detail;
                        * text4=attendance;
                        * */

                        stText1 =recyclerClass.getHeading();
                        holder.btn1.setBackgroundColor(Color.GREEN);
                        Attendance attendance=new Attendance(recyclerClass.getHeading(),recyclerClass.getText1(),recyclerClass.getText2(),"p");
                        connection.getDbSchool().child(schoolId).child("attendance").child(stClass)
                                .child(datedate).child(recyclerClass.getText2()).setValue(attendance).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                stText4=stText3;
                                stText3=stText2;
                                stText2=stText1+"(p)";

                                tvText3.setText(stText4);
                                tvText2.setText(stText3);
                                tvText1.setText(stText2);

                                cl4=cl3;
                                cl3=cl2;
                                cl2=cl1;
                                cl1=Color.GREEN;
                                tvText3.setBackgroundColor(cl3);
                                tvText2.setBackgroundColor(cl2);
                                tvText1.setBackgroundColor(cl1);
                            }
                        });
                        // for button color
                        recyclerClass.setText4("1");
                        recyclerView.scrollToPosition(holder.getAdapterPosition()+1);
                        recyclerAdapter.notifyDataSetChanged();
                    }
                });
                holder.btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stText1 =recyclerClass.getHeading();
                        holder.btn2.setBackgroundColor(Color.RED);
                        Attendance attendance=new Attendance(recyclerClass.getHeading(),recyclerClass.getText1(),recyclerClass.getText2(),"a");
                        connection.getDbSchool().child(schoolId).child("attendance").child(stClass)
                                .child(datedate).child(recyclerClass.getText2()).setValue(attendance).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                stText4=stText3;
                                stText3=stText2;
                                stText2=stText1+"(a)";
                                tvText3.setText(stText4);
                                tvText2.setText(stText3);
                                tvText1.setText(stText2);
                                cl4=cl3;
                                cl3=cl2;
                                cl2=cl1;
                                cl1=Color.RED;
                                tvText3.setBackgroundColor(cl3);
                                tvText2.setBackgroundColor(cl2);
                                tvText1.setBackgroundColor(cl1);
                            }
                        });
                        // for button color
                        recyclerClass.setText4("0");
                        recyclerView.scrollToPosition(holder.getAdapterPosition()+1);
                        recyclerAdapter.notifyDataSetChanged();
                    }
                });
                previousPosition=holder.getAdapterPosition();
            }
        };
        recyclerView=findViewById(R.id.recycle);
        arrayList=new ArrayList<>();
        recyclerAdapter=new RecyclerAdapter(this,recyclerClick,arrayList);
        storage=new Storage(this);
        connection=new Connection();
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,1);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);
        storage.showAlert("connecting...");
        Date date1=new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-dd-mm");
        datedate=simpleDateFormat.format(date1);
        tvDate.setText(datedate);
        recyclerAdapter.notifyDataSetChanged();
        stClass=storage.getValue("stClass");
        schoolId=storage.getValue("schoolId");

    }

    @Override
    protected void onStart() {
        super.onStart();
        connection.getDbSchool().child(storage.getValue("schoolId")).child("student")
                .child(storage.getValue("stClass")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                arrayList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    String id=dataSnapshot1.child("uid").getValue(String.class);
                    connection.getDbUser().child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
 //                           Attendance attendance=new Attendance("","","","");
                            User user2=dataSnapshot3.getValue(User.class);
                            RecyclerClass recyclerClass=new RecyclerClass();
                            try {
                                recyclerClass.setUrl(user2.getPhotoUrl());
                                recyclerClass.setHeading(user2.getName());
                                recyclerClass.setText1(user2.getRollNo());
                                recyclerClass.setText2(user2.getUid());
                            }catch (Exception e){}
                            recyclerClass.setText3(removeNull(user2.getName()+"\n"+user2.getNumber()+"\n"+user2.getRollNo()+"\n"+user2.getUid()));
                            arrayList.add(recyclerClass);
                            recyclerAdapter.notifyDataSetChanged();
                            storage.removeAlert();
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

    private String removeNull(String s) {
        return s.replaceAll("null","");
    }
}
