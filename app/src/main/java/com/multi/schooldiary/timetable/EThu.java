package com.multi.schooldiary.timetable;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.multi.schooldiary.R;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.SavedData;

import java.util.ArrayList;

public class EThu extends Fragment {
    private EditAdapter editAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycle,container,false);
    }
    RecyclerView recyclerView;
    ArrayList<TimeTable> list;
    Button btnAdd;
    SavedData savedData;
    Connection connection;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.recycle);
        list=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        editAdapter=new EditAdapter(list);
        recyclerView.setAdapter(editAdapter);
        btnAdd=view.findViewById(R.id.btnAdd);
        savedData =new SavedData(getContext());
        connection=new Connection();




        editAdapter.notifyDataSetChanged();


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.add(new TimeTable());
                editAdapter.notifyItemInserted(list.size()-1);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        connection.getDbSchool().child(savedData.getValue("schoolId")).child("timetable")
                .child(savedData.getValue("stClass"))
                .child("thu").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    list.add(dataSnapshot1.getValue(TimeTable.class));
                }
                editAdapter.notifyDataSetChanged();
                connection.getDbSchool().child(savedData.getValue("schoolId")).child("timetable")
                        .child(savedData.getValue("stClass"))
                        .child("thu").removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }@Override
    public void onDetach() {
        super.onDetach();
        connection.getDbSchool().child(savedData.getValue("schoolId")).child("timetable")
                .child(savedData.getValue("stClass"))
                .child("thu").setValue(list);
    }

}
