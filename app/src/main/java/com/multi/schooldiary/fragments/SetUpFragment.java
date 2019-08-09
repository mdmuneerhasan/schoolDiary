package com.multi.schooldiary.fragments;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.multi.schooldiary.R;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.DataGetter;
import com.multi.schooldiary.utility.SavedData;
import com.multi.schooldiary.utility.Storage;
import com.multi.schooldiary.utility.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SetUpFragment extends Fragment implements View.OnClickListener {
    LinearLayout btnStudent, btnTeacher, btnGuardian, btnFollow , linearLayout1,linearLayout2;
    CardView cv1, cv2;
    ScrollView scrollView;
    TextView tvHint,tvName,tvExtra;
    AutoCompleteTextView edtValue;
    Button btnConnect;
    ImageView avatar;


    Storage storage;
    SavedData savedData;
    Connection connection;
    int choice;
    ArrayList<String> list;
    Map<String,SetUpFragmentHelperClass> map;
    ArrayAdapter<String> adapter;
    int lastPopped=10;
    private SetUpFragmentHelperClass helperClass3;
    private TextWatcher textWatcher;
    User user,user1, userHost;
    private boolean block =false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_set_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        connection=new Connection();
        savedData=new SavedData(getContext());
        storage=new Storage();
        list=new ArrayList<>();
        map=new HashMap<>();
        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_dropdown_item_1line,list);
        tvName=view.findViewById(R.id.tvName);
        tvExtra=view.findViewById(R.id.tvExtra);
        avatar=view.findViewById(R.id.imgAvatar);
        btnConnect=view.findViewById(R.id.btnConnect);
        scrollView=view.findViewById(R.id.scrollView);
        btnStudent=view.findViewById(R.id.tvStudent);
        edtValue=view.findViewById(R.id.edtValue);
        tvHint=view.findViewById(R.id.tvHint);
        btnTeacher=view.findViewById(R.id.tvTeacher);
        btnGuardian=view.findViewById(R.id.tvGuardian);
        btnFollow=view.findViewById(R.id.tvFollow);
        linearLayout1=view.findViewById(R.id.lowerLayout1);
        linearLayout2=view.findViewById(R.id.lowerLayout2);
        cv1=view.findViewById(R.id.cv1);
        cv2=view.findViewById(R.id.cv2);
        btnStudent.setOnClickListener(this);
        btnTeacher.setOnClickListener(this);
        btnGuardian.setOnClickListener(this);
        btnFollow.setOnClickListener(this);
        btnConnect.setOnClickListener(this);
        edtValue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                helperClass3=map.get(edtValue.getText().toString());
                avatar.setPadding(0,0,0,0);
                Picasso.get().load(helperClass3.getUrl()).into(avatar);
                tvName.setText(helperClass3.getName());
                btnConnect.setText(helperClass3.getButton());
                popUp(3);
            }
        });
    }

    private void set(int i) {
        choice=i;
        btnStudent.setBackgroundColor(Color.parseColor("#ffffff"));
        btnTeacher.setBackgroundColor(Color.parseColor("#ffffff"));
        btnGuardian.setBackgroundColor(Color.parseColor("#ffffff"));
        btnFollow.setBackgroundColor(Color.parseColor("#ffffff"));
        scrollView.animate().translationY(0).setDuration(500);
        switch (i%4){
            case 0:
                tvHint.setText("Enter Student id(Sid)");
                btnStudent.setBackgroundColor(Color.parseColor("#cccccc"));
                break;
            case 1:
                tvHint.setText("Enter Teacher id(Sid)");
                btnTeacher.setBackgroundColor(Color.parseColor("#cccccc"));
                break;
            case 2:
                tvHint.setText("Enter Student id(Sid)");
                btnGuardian.setBackgroundColor(Color.parseColor("#cccccc"));
                break;
            case 3:
                tvHint.setText("Enter school / college name or id");
                btnFollow.setBackgroundColor(Color.parseColor("#cccccc"));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        edtValue.removeTextChangedListener(textWatcher);
        avatar.setPadding(10,10,10,10);
        popDown();
        switch (v.getId()){
            case R.id.tvStudent:
                set(0);
                act0(0);
                if(lastPopped==0){
                    popUp(0);
                }
                break;
            case R.id.tvTeacher:
                set(1);
                act0(1);
                if(lastPopped==1){
                    popUp(1);
                }
                break;
            case R.id.tvGuardian:
                act0(2);
                set(2);
                if(lastPopped==2){
                    popUp(2);
                }
                break;
            case R.id.tvFollow:
                set(3);
                act3();
                if(lastPopped==3){
                    avatar.setPadding(0,0,0,0);
                    popUp(3);
                }
                break;
            case R.id.btnConnect:
                connect(lastPopped);
                break;
        }
    }

    private void act0(final int choice) {
        textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if(s.length()>2){
                    popUp(choice);
                    tvName.setText("loading...");
                    connection.getDbUser().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            user=dataSnapshot.child(s.toString()).getValue(User.class);
                            if(user==null){
                                tvName.setText("no result found!!");
                                tvExtra.setText("");
                            }else{
                                tvName.setText(user.getName());
                                tvExtra.setText("Standard: "+user.getStClass()+ getPosition(user.getPosition()) +"\nSchool: " +user.getSchoolName());
                                Picasso.get().load("https://cdn3.iconfinder.com/data/icons/vector-icons-6/96/256-512.png").into(avatar);
                                userHost =dataSnapshot.child(s.toString()).child("host").getValue(User.class);
                            }
                            connection.getDbUser().removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{
                    avatar.setImageDrawable(Drawable.createFromPath("@drawable/ic_add_a_photo_black_24dp"));
                    tvName.setText("");
                    tvExtra.setText("");
                    popDown();
                }
                tvName.setText("searching...");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        edtValue.addTextChangedListener(textWatcher) ;
    }

    private String getPosition(String position) {
        try{
            switch (Integer.parseInt(position)){
                case 2:
                    return " (student)";
                case 3:
                    return " (student)";
                case 5:
                    return " (teacher)";
            }
        }catch (Exception e){

        }
        return null;
    }

    private void connect(int lastPopped) {
        popUp(lastPopped);
        switch (lastPopped){
            case 0  :
                if(Integer.parseInt(user.getPosition())!=2 && Integer.parseInt(user.getPosition())!=3){
                    tvExtra.setText("not a student account");
                    return;
                }
                if(userHost!=null){
                    if(userHost.getUid().equals(savedData.getValue("uid"))){
                        connection.getDbUser().child(savedData.getValue("uid")).child("host")
                                .setValue(user);
                        savedData.toast("Connected");
                        savedData.setValue("schoolId",user.getSchoolId());
                        savedData.setValue("position","2");
                        savedData.setValue("stClass",user.getSchoolId());
                        savedData.setValue("sid",user.getUid());
                        new DataGetter(getContext()).fetchSchool(user.getSchoolId());
                        setMainFragment();
                    }else{
                     tvExtra.setText("this account is already hosted by "+userHost.getName()+"\n" +
                             "ask management to set your account as default");
                    }
                    return;
                }
                user1=new User(savedData.getValue("name"),savedData.getValue("uid"),savedData.getValue("photoUrl"));
                connection.getDbSchool().child(user.getSchoolId()).child("student").child(user.getStClass())
                        .child(user.getUid()).child("host").setValue(user1);
                savedData.test(user.getSchoolId()+user.getStClass()+user.getUid());

                connection.getDbUser().child(user.getUid()).child("host")
                        .setValue(user1);
                connection.getDbUser().child(savedData.getValue("uid")).child("host")
                        .setValue(user);
                // follow
                connection.getDbSchool().child(user.getSchoolId()).child("followers")
                        .child(savedData.getValue("uid")).child("name").setValue(savedData.getValue("name"));
                connection.getDbUser().child(savedData.getValue("uid")).child("following")
                        .child(user.getSchoolId()).setValue(new SetUpFragmentHelperClass(user.getPhotoUrl(),user.getName(),null,null));
                break;
            case 1  :
                if(Integer.parseInt(user.getPosition())!=5){
                    tvExtra.setText("not a teacher account");
                    return;
                }
                if(userHost!=null){
                    if(userHost.getUid().equals(savedData.getValue("uid"))){
                        connection.getDbUser().child(savedData.getValue("uid")).child("host")
                                .setValue(user);
                        btnConnect.setText("Connected");
                    }else{
                        tvExtra.setText("this account is already hosted by "+userHost.getName()+"\n" +
                                "ask management to replace to host");
                    }
                    return;
                }
                user1=new User(savedData.getValue("name"),savedData.getValue("uid"),savedData.getValue("photoUrl"));
                connection.getDbSchool().child(user.getSchoolId()).child("staff").child(user.getStClass())
                        .child(user.getUid()).child("host").setValue(user1);
                connection.getDbUser().child(user.getUid()).child("host")
                        .setValue(user1);
                connection.getDbUser().child(savedData.getValue("uid")).child("host")
                        .setValue(user);
                // follow
                connection.getDbSchool().child(user.getSchoolId()).child("followers")
                        .child(savedData.getValue("uid")).child("name").setValue(savedData.getValue("name"));
                connection.getDbUser().child(savedData.getValue("uid")).child("following")
                        .child(user.getSchoolId()).setValue(new SetUpFragmentHelperClass(user.getPhotoUrl(),user.getName(),null,null));
                break;
            case 2  :
                btnConnect.setText("connected");
                user1=new User(savedData.getValue("name"),savedData.getValue("uid"),savedData.getValue("photoUrl"));
                connection.getDbSchool().child(user.getSchoolId()).child("student").child(user.getStClass())
                        .child(user.getUid()).child("guardian").child(savedData.getValue("uid")).setValue(user1);
                connection.getDbUser().child(savedData.getValue("uid")).child("children")
                        .child(user.getSid()).setValue(user);
                // follow
                connection.getDbSchool().child(user.getSchoolId()).child("followers")
                        .child(savedData.getValue("uid")).child("name").setValue(savedData.getValue("name"));
                connection.getDbUser().child(savedData.getValue("uid")).child("following")
                        .child(user.getSchoolId()).setValue(new SetUpFragmentHelperClass(user.getPhotoUrl(),user.getName(),null,null));
                break;
            case 3 :
                btnConnect.setText("following");
                savedData.toast("now you are following "+helperClass3.getName());
                connection.getDbSchool().child(helperClass3.getId()).child("followers")
                        .child(savedData.getValue("uid")).child("name").setValue(savedData.getValue("name"));
                connection.getDbUser().child(savedData.getValue("uid")).child("following")
                        .child(helperClass3.getId()).setValue(helperClass3);
                helperClass3.setButton("following");
                break;
        }
    }

    private void setMainFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new MainFragment());
        ft.commit();
    }

    private void act3() {
        connection.getDbSchool().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot datasnap:dataSnapshot.getChildren() ) {
                    if(datasnap!=null){
                        list.add(datasnap.child("name").getValue(String.class)+" "+datasnap.child("id").getValue(String.class));
                        SetUpFragmentHelperClass setUpFragmentHelperClass=datasnap.getValue(SetUpFragmentHelperClass.class);
                        setUpFragmentHelperClass.setUrl(datasnap.child("schoolPhoto").getValue(String.class));
                        if(datasnap.child("followers").child(savedData.getValue("uid")).child("name").getValue(String.class)!=null){
                            setUpFragmentHelperClass.setButton("following");
                        }else{
                            setUpFragmentHelperClass.setButton("follow");
                        }
                        map.put(datasnap.child("name").getValue(String.class)+" "+datasnap.child("id").getValue(String.class),setUpFragmentHelperClass);
                    }
                }
                adapter.notifyDataSetChanged();
                edtValue.setAdapter(adapter);
                connection.getDbSchool().removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void popDown() {
        if(block){
            block=false;
            return;
        }
        cv2.animate().translationX(1024).setDuration(500);
        linearLayout1.animate().translationY(-300).setDuration(500);
        linearLayout2.animate().translationY(-300).setDuration(500);
    }
    private void popUp(int i) {
        lastPopped=i;
        cv2.animate().translationY(0).translationX(0).setDuration(500);
        linearLayout1.animate().translationY(0).setDuration(500);
        linearLayout2.animate().translationY(0).setDuration(500);
    }
}
