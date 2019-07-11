package com.multi.schooldiary.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.multi.schooldiary.R;
import com.multi.schooldiary.activity.SetDefaultActivity;
import com.multi.schooldiary.dashboard.principal.PrincipalActivity;
import com.multi.schooldiary.dashboard.student_or_guardian.StudentGuardianActivity;
import com.multi.schooldiary.dashboard.teacher.TeacherActivity;
import com.multi.schooldiary.utility.SavedData;

public class DashBoardFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dash_board,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SavedData savedData =new SavedData(getContext());
        savedData.setValue("progress","3");
        if(savedData.getValue("position")==null){
            savedData.toast("please set defaults");
        }else if(Integer.parseInt(savedData.getValue("position"))>=6){
            startActivity(new Intent(getContext(), PrincipalActivity.class));
        }else if(Integer.parseInt(savedData.getValue("position"))>=5){
            startActivity(new Intent(getContext(), TeacherActivity.class));
        }else if(Integer.parseInt(savedData.getValue("position"))>=2){
            startActivity(new Intent(getContext(), StudentGuardianActivity.class));
        }else{
            startActivity(new Intent(getContext(), SetDefaultActivity.class));
            savedData.toast("please set defaults ");
        }
    }
}
