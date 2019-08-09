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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.multi.schooldiary.R;
import com.multi.schooldiary.utility.Connection;
import com.multi.schooldiary.utility.SavedData;
import com.squareup.picasso.Picasso;

public class MyProfileFragment extends Fragment {
    EditText edtName,edtNumber,edtUrl;
    SavedData savedData;
    String stName,stNumber;
    ImageView imageView;
    Button btnSetUp;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_profile,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSetUp=view.findViewById(R.id.btnSetUpMyProfile);
        edtName=view.findViewById(R.id.edtName);
        imageView=view.findViewById(R.id.imageView);
        edtNumber=view.findViewById(R.id.edtNumber);
        edtUrl=view.findViewById(R.id.edtUrl);
        savedData =new SavedData(getContext());
        edtName.setText(savedData.getValue("name"));
        edtNumber.setText(savedData.getValue("number"));
        edtUrl.setText(savedData.getValue("photoUrl"));
        Picasso.get().load(savedData.getValue("photoUrl")).into(imageView);

        btnSetUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMyProfile(v);
            }
        });
        edtUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Picasso.get().load(edtUrl.getText().toString()).into(imageView);
                }catch (Exception e){

                }
            }
        });
    }
    public void setMyProfile(View view) {
        stName=edtName.getText().toString();
        stNumber=edtNumber.getText().toString();
        if(stName.trim().length()<2){
            edtName.setError("name too short");
            return;
        }if(stNumber.trim().length()<2){
            edtNumber.setError("number too short");
            return;
        }
        savedData.showAlert("connecting...");
        DatabaseReference ref=new Connection().getDbUser();
        ref.child(savedData.getValue("uid")).child("name").setValue(stName);
        ref.child(savedData.getValue("uid")).child("photoUrl").setValue(edtUrl.getText().toString());
        ref.child(savedData.getValue("uid")).child("number").setValue(stNumber).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                savedData.removeAlert();
                savedData.setValue("name",stName);
                savedData.setValue("number",stNumber);
                savedData.setValue("photoUrl",edtUrl.getText().toString());
                getActivity().onBackPressed();
                savedData.toast("profile updated...");
            }
        });
    }
}
