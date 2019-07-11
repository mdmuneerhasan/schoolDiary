package com.multi.schooldiary.dashboard.teacher;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.multi.schooldiary.R;
import com.multi.schooldiary.activity.MyToolBar;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.SetDefaultClass;
import com.multi.schooldiary.utility.SavedData;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeacherFragmentActivity extends AppCompatActivity {
    SavedData savedData;
    ArrayAdapter<String> adapter;
    Connection connection;
    String id="",position="1",request;
    Spinner spinner;
    Date date;
    Button btnJoin;
    EditText editText;
    ArrayList<String> list;
    Map<String ,String> map;
    AutoCompleteTextView autoCompleteTextView;
    SimpleDateFormat simpleDateFormat;
    String[] classList;
    int index;
    ValueEventListener valueEventListener2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher2);
            savedData =new SavedData(this);
            connection=new Connection();
            spinner=findViewById(R.id.spinnerClass);
            editText = findViewById(R.id.edtName);
            simpleDateFormat = new SimpleDateFormat("yyMM");
            date = new Date(System.currentTimeMillis());
            autoCompleteTextView=findViewById(R.id.tvName);
            list=new ArrayList<>();
            map=new HashMap<>();
            adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,list);
            btnJoin=findViewById(R.id.btnJoin);
            classList=new String[]{"select class or year","play group","nursery","KG","LKG","UKG","class 1","class 2","class 3","class 4","class 5","class 6","class 7","class 8","class 9","class 10","class 11","class 12","1st year","2nd year","3rd year","4th year","5th year","6th year","7th year","other"};
            ArrayAdapter<String> arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line,classList);
            spinner.setAdapter(arrayAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    index=position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            valueEventListener2=new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    list.clear();
                    for (DataSnapshot datasnap:dataSnapshot.getChildren() ) {
                        if(datasnap!=null){
                            list.add(datasnap.child("name").getValue(String.class)+" "+datasnap.child("id").getValue(String.class));
                            map.put(datasnap.child("name").getValue(String.class)+" "+datasnap.child("id").getValue(String.class),datasnap.child("id").getValue(String.class));
                        }
                    }
                    adapter.notifyDataSetChanged();
                    autoCompleteTextView.setAdapter(adapter);
                    connection.getDbSchool().removeEventListener(valueEventListener2);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            findViewById(R.id.btnJoin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    join(v);
                }
            });
        MyToolBar myToolBar= new MyToolBar((Toolbar) findViewById(R.id.toolbar), this) {
            @Override
            public void onAlertActionPerformed() {
            }
        };
        setSupportActionBar(myToolBar.getToolBar());

    }

        @Override
        public void onStart() {
            super.onStart();
            request="schoolRequest";
            position="5";
            connection.getDbSchool().addValueEventListener(valueEventListener2);
        }


        public void join(View view) {
            try {
                // student and teacher join
                id=map.get(autoCompleteTextView.getText().toString());
                savedData.showAlert("connecting...");
                SetDefaultClass setDefaultClass=new SetDefaultClass(autoCompleteTextView.getText().toString().split(id)[0],position,classList[index],id);
                setDefaultClass.setPermission(false);
                setDefaultClass.setUid(savedData.getValue("uid"));
                setDefaultClass.setName(savedData.getValue("name"));
                connection.getDbUser().child(savedData.getValue("uid")).child("permission")
                        .child(id).child(position).setValue(setDefaultClass);
                connection.getDbSchool().child(id).child(request).child(classList[index])
                        .child(savedData.getValue("uid")).setValue(setDefaultClass)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                savedData.removeAlert();
                                savedData.toast("School joining request sent...");
                                finish();
                            }
                        });

            }catch (Exception e){
                savedData.toast("please enter valid school name");
                savedData.removeAlert();
            }
        }
    }
