package com.multi.schooldiary.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.multi.schooldiary.R;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.SetDefaultClass;
import com.multi.schooldiary.utility.Storage;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JoinFragment extends Fragment {
    RadioGroup rdGroup;
    Storage storage;
    TextView tvId;
    ArrayAdapter<String> adapter;
    Connection connection;
    TextWatcher textWatcher;
    String id="",position="1",request;
    Spinner spinner;
    Date date;
    Button btnCreate,btnJoin;
    EditText editText;
    ArrayList<String> list;
    Map<String ,String> map;
    AutoCompleteTextView autoCompleteTextView;
    SimpleDateFormat simpleDateFormat;
    String[] classList;
    View v;
    int index;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_join,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        v=view;
        rdGroup=view.findViewById(R.id.rdGroup);
        storage=new Storage(getContext());
        connection=new Connection();
        spinner=view.findViewById(R.id.spinnerClass);
        editText = view.findViewById(R.id.edtName);
        simpleDateFormat = new SimpleDateFormat("yyMM");
        date = new Date(System.currentTimeMillis());
        autoCompleteTextView=view.findViewById(R.id.tvName);
        list=new ArrayList<>();
        map=new HashMap<>();
        adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line,list);
        tvId = view.findViewById(R.id.tvId);
        btnCreate=view.findViewById(R.id.btnCreate);
        btnJoin=view.findViewById(R.id.btnJoin);
        classList=new String[]{"select class","class 1","class 2","class 3","class 4","class 5","class 6","class 7","class 8","class 9","class 10"," class 11","class 12","other"};
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line,classList);
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
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                connection.getDbUser().child(editText.getText().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String name=(dataSnapshot.child("name").getValue(String.class));
                        if(name==null){
                            tvId.setText("fetching students name...");
                        }else{
                            tvId.setText(name);
                            id=(dataSnapshot.child("uid").getValue(String.class));

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        rdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                v.findViewById(R.id.linearLayout1).setVisibility(View.GONE);
                v.findViewById(R.id.linearLayout2).setVisibility(View.GONE);
                tvId.setText("");
                editText.setText("");
                switch (group.getCheckedRadioButtonId()){
                    case R.id.rd1:
                        storage.showAlert("Requesting school id.. ");
                        editText.setHint("School name");
                        btnCreate.setText("create school account");
                        editText.removeTextChangedListener(textWatcher);
                        connection.getDbSchool().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot!=null){
                                    id= simpleDateFormat.format(date)+String.valueOf(dataSnapshot.getChildrenCount());
                                }else{
                                    id=simpleDateFormat.format(date)+"0";
                                }
                                storage.removeAlert();
                                tvId.setText("School id : "+id);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                        v.findViewById(R.id.linearLayout1).setVisibility(View.VISIBLE);
                        break;
                    case R.id.rd2:
                        position="2";
                        request="classRequest";
                        v.findViewById(R.id.linearLayout2).setVisibility(View.VISIBLE);
                        break;
                    case R.id.rd3:
                        request="schoolRequest";
                        position="5";
                        v.findViewById(R.id.linearLayout2).setVisibility(View.VISIBLE);
                        break;
                    case R.id.rd4:
                        v.findViewById(R.id.linearLayout1).setVisibility(View.VISIBLE);
                        editText.setHint("Student id");
                        btnCreate.setText("link up account");
                        editText.addTextChangedListener(textWatcher);
                        break;
                }
            }
        });

        view.findViewById(R.id.btnCreate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create(v);
            }
        });
        view.findViewById(R.id.btnJoin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                join(v);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        connection.getDbSchool().addValueEventListener(new ValueEventListener() {
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void join(View view) {
        try {
            // student and teacher join
            id=map.get(autoCompleteTextView.getText().toString());
            storage.showAlert("connecting...");
            SetDefaultClass setDefaultClass=new SetDefaultClass(autoCompleteTextView.getText().toString().split(id)[0],position,classList[index],id);
            setDefaultClass.setPermission(false);
            setDefaultClass.setUid(storage.getValue("uid"));
            connection.getDbUser().child(storage.getValue("uid")).child("permission")
                    .child(id).child(position).setValue(setDefaultClass);
            connection.getDbSchool().child(id).child(request).child(classList[index])
                    .child(storage.getValue("uid")).setValue(setDefaultClass)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            storage.removeAlert();
                            storage.toast("School joining request sent...");
                            getActivity().onBackPressed();
                        }
                    });

        }catch (Exception e){
            storage.toast("please enter valid school name");
            storage.removeAlert();
        }
    }

    public void create(View view) {
        switch (rdGroup.getCheckedRadioButtonId()){
            case R.id.rd1:
                // school account creation
                storage.showAlert("creating school account...");
                SetDefaultClass setDefaultClass=new SetDefaultClass(editText.getText().toString(),"7",null,id);
                setDefaultClass.setPermission(true);
                connection.getDbUser().child(storage.getValue("uid")).child("permission")
                        .child(id).child("7").setValue(setDefaultClass);
                connection.getDbSchool().child(id).child("name").setValue(editText.getText().toString());
                connection.getDbSchool().child(id).child("id").setValue(id);
                connection.getDbSchool().child(id).child("principal").setValue(storage.getValue("uid"))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                storage.removeAlert();
                                storage.toast("Account created successfully");
                                getActivity().onBackPressed();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                break;
            case R.id.rd4:
                // guardian join
                storage.showAlert("connecting...");
                connection.getDbUser().child(storage.getValue("uid")).child("children")
                        .child(id).setValue(false);
                connection.getDbUser().child(id).child("guardian")
                        .child(storage.getValue("uid")).setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        storage.removeAlert();
                        storage.toast("Request sent!! ");
                        getActivity().onBackPressed();
                    }
                });
                break;

        }
    }
}
