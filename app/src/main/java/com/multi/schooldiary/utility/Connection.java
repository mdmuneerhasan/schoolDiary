package com.multi.schooldiary.utility;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.multi.schooldiary.BuildConfig;

public class Connection {
    DatabaseReference reference;
    String path="";

    public Connection() {
        if(BuildConfig.DEBUG){
            path="debug";
        }else {
            path="release";
        }
        reference = FirebaseDatabase.getInstance().getReference(path);
    }

    public DatabaseReference getDbUser() {
        return reference.child("user");
    }

    public DatabaseReference getDbSchool() {
        return reference.child("school");
    }
}
