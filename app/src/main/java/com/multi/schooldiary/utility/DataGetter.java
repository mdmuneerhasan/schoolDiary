package com.multi.schooldiary.utility;

import android.content.Context;
import android.support.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class DataGetter extends SavedData {
    Connection connection;
    public DataGetter(Context context) {
        super(context);
        connection=new Connection();

    }

    public void fetchSchool(String schoolId) {
        connection.getDbSchool().child(schoolId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setValue("schoolName",dataSnapshot.child("name").getValue(String.class));
                setValue("deptType",dataSnapshot.child("deptType").getValue(String.class));
                setValue("schoolPhoto",dataSnapshot.child("name").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
