package com.multi.schooldiary.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.multi.schooldiary.R;
import com.multi.schooldiary.utility.SavedData;

public class SetUpFragmentDelete extends Fragment implements View.OnClickListener {

    Button btnTeacher,btnStudent,btnCollege,btnSchool,btnGuardian;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_set_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        btnTeacher=view.findViewById(R.id.btnTeacher);
//        btnStudent=view.findViewById(R.id.btnStudent);
//        btnCollege=view.findViewById(R.id.btnCollege);
//        btnSchool=view.findViewById(R.id.btnSchool);
//        btnGuardian=view.findViewById(R.id.btnGuardian);
        btnTeacher.setOnClickListener(this);
        btnStudent.setOnClickListener(this);
        btnCollege.setOnClickListener(this);
        btnSchool.setOnClickListener(this);
        btnTeacher.setOnClickListener(this);
        new SavedData(getContext()).setValue("progress","2");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            default:
//            case R.id.btnTeacher :
//                startActivity(new Intent(getContext(), TeacherFragmentActivity.class));
//                break;
//            case R.id.btnCollege :
//                startActivity(new Intent(getContext(), CollegeFragmentActivity.class));
//                break;
//            case R.id.btnStudent :
//                startActivity(new Intent(getContext(), StudentJoinFragmentActivity.class));
//                break;
            case R.id.btnSchool :
  //              startActivity(new Intent(getContext(), SchoolFragmentActivity.class));
                break;
        }
    }
}