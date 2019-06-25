package com.multi.schooldiary.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.multi.schooldiary.R;
import com.multi.schooldiary.utility.Storage;

public class MainFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content_main,container,false);

    }
    Storage storage;
    StringBuilder loggedIn;
    TextView textView;
    String[] positionList;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        positionList=new String[]{"none","none","student","parent","monitor","teacher","vice president","principal"};
        storage=new Storage(getContext());
        textView=view.findViewById(R.id.loggedIn);
        loggedIn=new StringBuilder();



        if(storage.getValue("schoolName")!=null){
            loggedIn.append("Logged in as "+positionList[Integer.parseInt(storage.getValue("position"))]+" of ");
            if(storage.getValue("stClass")!=null){
                loggedIn.append(storage.getValue("stClass")+" at ");
            }
            loggedIn.append(storage.getValue("schoolName"));
            textView.setText(loggedIn);
        }


    }
}
