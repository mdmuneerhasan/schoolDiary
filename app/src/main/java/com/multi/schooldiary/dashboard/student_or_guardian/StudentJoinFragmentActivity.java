package com.multi.schooldiary.dashboard.student_or_guardian;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.multi.schooldiary.R;
import com.multi.schooldiary.activity.MyToolBar;
import com.multi.schooldiary.activity.SetDefaultActivity;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.SavedData;
import com.multi.schooldiary.utility.SetDefaultClass;
import com.multi.schooldiary.utility.User;

import java.util.HashMap;
import java.util.Map;

public class StudentJoinFragmentActivity extends AppCompatActivity {
    EditText edtSid;
    TextView tvWarning,tvName,tvPosition;
    SavedData savedData;
    Connection connection;
    String sid,schoolId,stClass;
    private String hostId;
    private String hostName;
    Button btnHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_join);
        edtSid=findViewById(R.id.edtSid);
        tvWarning=findViewById(R.id.tvWarning);
        tvName=findViewById(R.id.tvName);
        tvPosition=findViewById(R.id.tvPosition);
        connection=new Connection();
        savedData =new SavedData(this);
        btnHost=findViewById(R.id.btnHost);


        MyToolBar myToolBar= new MyToolBar((Toolbar) findViewById(R.id.toolbar), this) {
            @Override
            public void onAlertActionPerformed() {
            }
        };
        setSupportActionBar(myToolBar.getToolBar());

        edtSid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                tvName.setText("searching...");
                connection.getDbUser().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user=dataSnapshot.child(s.toString()).getValue(User.class);
                        if(user==null){
                            if(s.length()>1){
                                tvName.setText("no result found!!");
                                tvPosition.setText("");
                            }else {
                                tvPosition.setText("");
                                tvName.setText("");
                            }
                        }else{
                            sid=user.getSid();
                            tvName.setText(user.getName());
                            tvPosition.setText("Standard: "+user.getStandard() +"\n School id: " +user.getSchoolId());
                            stClass=user.getStandard();
                            schoolId=user.getSchoolId();
                        }
                        connection.getDbUser().removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void host(View view) {
        btnHost.setText("sending request...");
        if(sid==null){
            edtSid.setError("incorrect sid");
            return;
        }
       connection.getDbSchool().child(schoolId).addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
               String name=dataSnapshot1.child("name").getValue(String.class);
               hostId= dataSnapshot1.child("student").child(stClass).child(sid).child("host").getValue(String.class);
               hostName= dataSnapshot1.child("student").child(stClass).child(sid).child("hostName").getValue(String.class);
               if(hostId==null){
                   connection.getDbSchool().child(schoolId).child("student").child(stClass)
                           .child(sid).child("host").setValue(savedData.getValue("uid"));
                   connection.getDbSchool().child(schoolId).child("student").child(stClass)
                           .child(sid).child("hostName").setValue(savedData.getValue("name"));
                   SetDefaultClass setDefaultClass=new SetDefaultClass(name,"2",stClass,schoolId);
                   setDefaultClass.setPermission(true);
                   setDefaultClass.setSid(sid);
                   connection.getDbUser().child(savedData.getValue("uid")).child("permission").child(schoolId)
                           .child("2").setValue(setDefaultClass);
                   savedData.toast("set your student account as default");
                   startActivity(new Intent(getBaseContext(), SetDefaultActivity.class));
                   finish();
               }else if(hostId.equals(savedData.getValue("uid"))){
                   btnHost.setText("already hosting this account");
               }else {
                   btnHost.setText("something went wrong");
                   tvWarning.setText("this account is already hosted by "+hostName+
                           "\nask your class teacher to remove the host and set your account as new host" +
                           "\nclick on recover button to inform management that your account is hosted by some " +
                           "fake account, we highly recommend not to misuse this feature");
               findViewById(R.id.btnRecover).setVisibility(View.VISIBLE);

               }
               connection.getDbSchool().child(schoolId).child("student").child(stClass).child(sid)
                       .removeEventListener(this);
               }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {
           }
       });

    }

    public void recover(View view) {
        savedData.showAlert("Recovering...");
        Map<String,String> map=new HashMap();
        map.put("host",hostId);
        map.put("hostName",hostName);
        map.put("claimerId", savedData.getValue("uid"));
        map.put("claimerName", savedData.getValue("name"));
        connection.getDbSchool().child(schoolId).child("recover").child(stClass).child(savedData.getValue("uid"))
                .setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                savedData.removeAlert();
                finish();
                savedData.toast("request send!!");
            }
        });
    }
}
