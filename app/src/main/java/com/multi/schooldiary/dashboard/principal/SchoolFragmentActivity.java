package com.multi.schooldiary.dashboard.principal;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.multi.schooldiary.R;
import com.multi.schooldiary.activity.MyToolBar;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.SavedData;
import com.multi.schooldiary.utility.SetDefaultClass;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class SchoolFragmentActivity extends AppCompatActivity {

    SavedData savedData;
    Connection connection;
    String id="";
    Date date;
    EditText editText;
    SimpleDateFormat simpleDateFormat;
    ValueEventListener valueEventListener1;
    private String name;
    private EditText edtphotoUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_fragment);
        edtphotoUrl=findViewById(R.id.edtPhotoUrl);
            savedData =new SavedData(this);
            connection=new Connection();
            editText =findViewById(R.id.edtName);
            simpleDateFormat = new SimpleDateFormat("yy");
            date = new Date(System.currentTimeMillis());


        MyToolBar myToolBar= new MyToolBar((Toolbar) findViewById(R.id.toolbar), this) {
            @Override
            public void onAlertActionPerformed() {
            }
        };
        setSupportActionBar(myToolBar.getToolBar());

        edtphotoUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>2){
                    Picasso.get().load(s.toString()).into((ImageView) findViewById(R.id.imageView));
                    findViewById(R.id.imageView).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        valueEventListener1=new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot!=null){
                        id= "c"+simpleDateFormat.format(date)+String.valueOf(dataSnapshot.getChildrenCount());
                    }else{
                        id="c"+simpleDateFormat.format(date)+"0";
                    }
                    SetDefaultClass setDefaultClass=new SetDefaultClass(name,"7",null,id);
                    setDefaultClass.setPermission(true);
                    connection.getDbUser().child(savedData.getValue("uid")).child("permission")
                            .child(id).child("7").setValue(setDefaultClass);
                    connection.getDbSchool().child(id).child("name").setValue(name);
                    connection.getDbSchool().child(id).child("schoolPhoto").setValue(edtphotoUrl.getText().toString());                    connection.getDbSchool().child(id).child("id").setValue(id);
                    connection.getDbSchool().child(id).child("deptType").setValue("school");
                    connection.getDbSchool().child(id).child("principal").setValue(savedData.getValue("uid"))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    savedData.removeAlert();
                                    savedData.toast("Account created successfully");
                                    connection.getDbSchool().removeEventListener(valueEventListener1);
                                    savedData.setValue("schoolId",id);
                                    savedData.setValue("schoolName",name);
                                    savedData.setValue("deptType","school");
                                    savedData.setValue("position","7");
                                    savedData.setValue("stClass","class 11");
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };

 }

        @Override
        public void onStart() {
            super.onStart();
        }


        public void create(View view) {
        name=editText.getText().toString();
        if(name.trim().length()<2){
            savedData.toast("School name too short");
            editText.setError("name too short");
            return;
        }
                    savedData.showAlert("creating school account...");
            connection.getDbSchool().addListenerForSingleValueEvent(valueEventListener1);
        }

        @Override
        public void onStop() {
            super.onStop();
            connection.getDbUser().child(editText.getText().toString()).removeEventListener(valueEventListener1);
        }
    }
