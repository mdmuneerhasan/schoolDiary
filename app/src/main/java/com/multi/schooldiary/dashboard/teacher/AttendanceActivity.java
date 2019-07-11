package com.multi.schooldiary.dashboard.teacher;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.multi.schooldiary.R;
import com.multi.schooldiary.activity.MyToolBar;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.RecyclerAdapter;
import com.multi.schooldiary.utility.RecyclerClass;
import com.multi.schooldiary.utility.RecyclerClick;
import com.multi.schooldiary.utility.SavedData;
import com.multi.schooldiary.utility.User;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class AttendanceActivity extends AppCompatActivity {
    SavedData savedData;
    Connection connection;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    ArrayList<RecyclerClass> arrayList;
    RecyclerClick recyclerClick;
    String date,month,schoolId,stClass, stText1,stText2,stText3,stText4;
    TextView tvDate,tvText1,tvText2,tvText3;
    RadioGroup rdGroup;
    Boolean firstHalf=false,secondHalf=false;
    String half="1";
    int cl1,cl2,cl3,cl4;
    RadioButton rdButton1,rdButton2;
    int previousPosition=0;
    private Date date1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendece);
        tvDate=findViewById(R.id.tvDate);
        tvText1=findViewById(R.id.text1);
        tvText2=findViewById(R.id.text2);
        tvText3=findViewById(R.id.text3);
        rdGroup=findViewById(R.id.rdGroup);
        rdButton1=findViewById(R.id.firstHalf);
        rdButton2=findViewById(R.id.secondHalf);


        MyToolBar myToolBar= new MyToolBar((Toolbar) findViewById(R.id.toolbar), this) {
            @Override
            public void onAlertActionPerformed() {
            }
        };
        setSupportActionBar(myToolBar.getToolBar());

        recyclerClick=new RecyclerClick() {
            @Override
            public void onBindViewHolder(final RecyclerAdapter.Holder holder, final RecyclerClass recyclerClass) {
                LinearLayout.LayoutParams btnLayout=new LinearLayout.LayoutParams(0,500);
                btnLayout.weight=1;
                btnLayout.setMargins(5,200,5,5);
                holder.text3.setVisibility(View.VISIBLE);
                holder.btn1.setVisibility(View.VISIBLE);
                holder.btn2.setVisibility(View.VISIBLE);
                holder.btn1.setText("present");
                holder.btn2.setText("absent");
                holder.btn1.setLayoutParams(btnLayout);
                holder.btn2.setLayoutParams(btnLayout);
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
                        try {
                            arrayList.get(holder.getAdapterPosition()+1);
                        }catch (Exception odufgoid){
                            startActivity(new Intent(getBaseContext(),AttendanceTodayActivity.class));
                        }

                        stText1 =recyclerClass.getHeading()+"-"+recyclerClass.getText1();
                        holder.btn1.setBackgroundColor(Color.GREEN);
                        Attendance attendance=new Attendance(recyclerClass.getText2(),"p");
                        connection.getDbSchool().child(schoolId).child("attendance").child(stClass)
                                .child(month).child(date).child(savedData.getType()).child(recyclerClass.getText2()).child(half).setValue(attendance).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                    try {
                        arrayList.get(holder.getAdapterPosition()+1);
                    }catch (Exception odufgoid){
                        startActivity(new Intent(getBaseContext(),AttendanceTodayActivity.class));
                    }
                        stText1 =recyclerClass.getHeading();
                        holder.btn2.setBackgroundColor(Color.RED);
                        Attendance attendance=new Attendance(recyclerClass.getText2(),"a");
                        connection.getDbSchool().child(schoolId).child("attendance").child(stClass)
                                .child(month).child(date).child(savedData.getType()).child(recyclerClass.getText2()).child(half).setValue(attendance).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        rdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(group.getCheckedRadioButtonId()==R.id.secondHalf){
                half="2";
                }else{
                    half="1";
                }
            }
        });
        recyclerView=findViewById(R.id.recycle);
        arrayList=new ArrayList<>();
        recyclerAdapter=new RecyclerAdapter(this,recyclerClick,arrayList);
        savedData =new SavedData(this);
        connection=new Connection();
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,1);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);
        savedData.showAlert("connecting...");
        date1=new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        date=simpleDateFormat.format(date1);
        month=new SimpleDateFormat("yyyy-MM").format(date1);
        recyclerAdapter.notifyDataSetChanged();
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp();
            }
        });
        if(savedData.getValue("stClass")==null){
            popUp();
        }
    }


    private void popUp() {
        if (!savedData.getType().equals("school")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setItems(new CharSequence[]
                            {"1st year", "2nd year", "3rd year", "4th year", "5th year", "6th year", "7th year", "other"},
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String[] classList = new String[]{"1st year", "2nd year", "3rd year", "4th year", "5th year", "6th year", "7th year", "other"};
                            savedData.setValue("stClass", classList[which]);
                            onStart();
                        }
                    }).setTitle("Select class");
            builder.create().show();
        } else if (savedData.getValue("position") != null && Integer.parseInt(savedData.getValue("position")) >= 6) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setItems(new CharSequence[]
                            {"play group", "nursery", "KG", "LKG", "UKG", "class 1", "class 2", "class 3", "class 4", "class 5", "class 6", "class 7", "class 8", "class 9", "class 10", "class 11", "class 12"},
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String[] classList = new String[]{"play group", "nursery", "KG", "LKG", "UKG", "class 1", "class 2", "class 3", "class 4", "class 5", "class 6", "class 7", "class 8", "class 9", "class 10", "class 11", "class 12"};
                            savedData.setValue("stClass", classList[which]);
                            onStart();
                        }
                    }).setTitle("Select class");
            builder.create().show();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(stClass==null){
            return;
        }
        stClass= savedData.getValue("stClass");
        schoolId= savedData.getValue("schoolId");
        tvDate.setText(new SimpleDateFormat("dd-MMM-yyyy").format(date1)+"("+ savedData.getValue("stClass")+")");
        connection.getDbSchool().child(schoolId).child("student")
                .child(stClass).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    User user2=dataSnapshot1.getValue(User.class);
                    RecyclerClass recyclerClass=new RecyclerClass();
                    try {
                        recyclerClass.setUrl(user2.getPhotoUrl());
                        recyclerClass.setHeading(user2.getName());
                        recyclerClass.setText1(user2.getRollNo());
                        recyclerClass.setText2(user2.getUid());
                    }catch (Exception e){ }
                    recyclerClass.setText3(removeNull(user2.getName()+"\n"+user2.getNumber()+"\n"+user2.getRollNo()+"\n"+user2.getUid()));
                    arrayList.add(recyclerClass);
                    recyclerAdapter.notifyDataSetChanged();
                    savedData.removeAlert();
                    connection.getDbSchool().child(schoolId).child("student")
                            .child(stClass).removeEventListener(this);
                }
                Collections.sort(arrayList,RecyclerClass.text1Comp);
                savedData.removeAlert();
                recyclerAdapter.notifyDataSetChanged();


                connection.getDbSchool().child(schoolId).child("attendance").child(stClass)
                        .child(month).child(date).child(savedData.getType()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        DataSnapshot dataSnapshot1 = null;
                        for(DataSnapshot dataSnapshot2:dataSnapshot.getChildren()){
                            dataSnapshot1=dataSnapshot2;
                            break;
                        }
                        if(dataSnapshot1!=null){
                            if(dataSnapshot.getChildrenCount()>arrayList.size()/2){
                                firstHalf=true;
                            }
                            if(dataSnapshot1.child("2").getValue()!=null){
                                secondHalf=true;
                            }
                            check();
                        }
                        connection.getDbSchool().child(schoolId).child("attendance").child(stClass)
                                .child(month).child(date).child(savedData.getType()).removeEventListener(this);

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


    private void check() {
        if(savedData.getValue("get").equals("get")){
            if(firstHalf && secondHalf){
                savedData.toast("attendance is rolled for today");
                startActivity(new Intent(this,AttendanceTodayActivity.class));
            }else if(firstHalf){
                savedData.toast("first half attendance is rolled");
                rdButton2.setChecked(true);
                rdButton1.setChecked(false);
            }else if(secondHalf){
                rdButton2.setChecked(false);
                rdButton1.setChecked(true);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private String removeNull(String s) {
        return s.replaceAll("null","");
    }
}
