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
import com.multi.schooldiary.utility.Storage;
import com.squareup.picasso.Picasso;

public class SetUpProfileFragment extends Fragment {
    EditText edtName,edtNumber,edtUrl;
    Storage storage;
    String stName,stNumber;
    ImageView imageView;
    Button btnSetUp;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_set_up_profile,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSetUp=view.findViewById(R.id.btnSetUpMyProfile);
        edtName=view.findViewById(R.id.edtName);
        imageView=view.findViewById(R.id.imageView);
        edtNumber=view.findViewById(R.id.edtNumber);
        edtUrl=view.findViewById(R.id.edtUrl);
        storage=new Storage(getContext());
        edtName.setText(storage.getValue("name"));
        edtNumber.setText(storage.getValue("number"));
        edtUrl.setText(storage.getValue("photoUrl"));
        Picasso.get().load(storage.getValue("photoUrl")).into(imageView);

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
        storage.showAlert("connecting...");
        DatabaseReference ref=new Connection().getDbUser();
        ref.child(storage.getValue("sid")).child("name").setValue(stName);
        ref.child(storage.getValue("uid")).child("name").setValue(stName);
        ref.child(storage.getValue("uid")).child("photoUrl").setValue(edtUrl.getText().toString());
        ref.child(storage.getValue("uid")).child("number").setValue(stNumber).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                storage.removeAlert();
                storage.setValue("name",stName);
                storage.setValue("number",stNumber);
                storage.setValue("photoUrl",edtUrl.getText().toString());
                getActivity().onBackPressed();
                storage.toast("profile updated...");
            }
        });
    }
}
