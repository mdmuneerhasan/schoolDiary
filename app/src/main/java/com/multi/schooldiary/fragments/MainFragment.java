package com.multi.schooldiary.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.multi.schooldiary.R;
import com.multi.schooldiary.notice.NoticeActivity;
import com.multi.schooldiary.notification.NotificationControlActivity;
import com.multi.schooldiary.school.MainPageActivity;
import com.multi.schooldiary.timetable.TimeTableActivity;
import com.multi.schooldiary.utility.SavedData;

public class MainFragment extends Fragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content_main,container,false);

    }
    SavedData savedData;
    StringBuilder loggedIn;
//    TextView textView;
    String[] positionList;
    LinearLayout tvNotification,tvHome,tvTimeTable,tvNotice,llDashBoard;
//    TextView tvClickable;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        positionList=new String[]{"none","none","student","parent","monitor","teacher","vice president","principal"};
        savedData =new SavedData(getContext());
  //      textView=view.findViewById(R.id.loggedIn);
        loggedIn=new StringBuilder();
        tvNotification=view.findViewById(R.id.tvNotification);
  //      tvClickable=view.findViewById(R.id.tvClickableText);
        tvNotice=view.findViewById(R.id.tvNoticeBoard);
        tvHome=view.findViewById(R.id.tvTimeTable);
        tvTimeTable=view.findViewById(R.id.tvHome);
        llDashBoard=view.findViewById(R.id.dashBoard);
        tvNotification.setOnClickListener(this);
        llDashBoard.setOnClickListener(this);
        tvNotice.setOnClickListener(this);
        tvHome.setOnClickListener(this);
        tvTimeTable.setOnClickListener(this);
//        tvClickable.setOnClickListener(this);

        if(!savedData.getValue("schoolName").equals(savedData.getDefault())){
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
