package com.multi.schooldiary.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.multi.schooldiary.R;
import com.multi.schooldiary.notice.NoticeActivity;
import com.multi.schooldiary.notification.NotificationControlActivity;
import com.multi.schooldiary.school.MainPageActivity;
import com.multi.schooldiary.services.ServiceItemAdapter;
import com.multi.schooldiary.services.ServiceItemClass;
import com.multi.schooldiary.timetable.TimeTableActivity;
import com.multi.schooldiary.utility.SavedData;

import java.util.ArrayList;

public class MainFragment extends Fragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content_main,container,false);

    }
    SavedData savedData;
    StringBuilder loggedIn;
    String[] positionList;
    RecyclerView recyclerView;
    ArrayList<ServiceItemClass> serviceItemClasses;
    ServiceItemAdapter serviceItemAdapter;
    LinearLayout tvNotification,tvHome,tvTimeTable,tvNotice,llDashBoard;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        positionList=new String[]{"none","none","student","parent","monitor","teacher","vice president","principal"};
        savedData =new SavedData(getContext());
        loggedIn=new StringBuilder();
        tvNotification=view.findViewById(R.id.tvNotification);
        recyclerView=view.findViewById(R.id.recycle);
        tvNotice=view.findViewById(R.id.tvNoticeBoard);
        tvHome=view.findViewById(R.id.tvTimeTable);
        tvTimeTable=view.findViewById(R.id.tvHome);
        llDashBoard=view.findViewById(R.id.dashBoard);
        tvNotification.setOnClickListener(this);
        llDashBoard.setOnClickListener(this);
        tvNotice.setOnClickListener(this);
        tvHome.setOnClickListener(this);
        tvTimeTable.setOnClickListener(this);
        serviceItemClasses =new ArrayList<>();
        serviceItemClasses.add(new ServiceItemClass("http://icons.iconarchive.com/icons/iconsmind/outline/256/Shop-4-icon.png","My Shop","order item to get at school","","",""));
        serviceItemAdapter=new ServiceItemAdapter(serviceItemClasses);


        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),1);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(serviceItemAdapter);

        if(savedData.haveValue("schoolName")){
            loggedIn.append("Logged in as "+positionList[Integer.parseInt(savedData.getValue("position"))]+" of ");
            loggedIn.append(savedData.getValue("schoolName"));
    //        textView.setText(loggedIn);
        }
        if(!savedData.needHelp()){
    //        tvClickable.setVisibility(View.GONE);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.tvNotification ){
            startActivity(new Intent(getContext(), NotificationControlActivity.class));
        }
        else if(v.getId()==R.id.dashBoard ){
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, new DashBoardFragment());
            ft.commit();
        }
        else if(v.getId()==R.id.tvHome ){
            startActivity(new Intent(getContext(), MainPageActivity.class));
        }
        else if(v.getId()==R.id.tvTimeTable ){
            startActivity(new Intent(getContext(), TimeTableActivity.class));
        }
        else if(v.getId()==R.id.tvNoticeBoard ){
            startActivity(new Intent(getContext(), NoticeActivity.class));
        }else {
            savedData.toast("Feature disabled for this version");
        }
    }

}
