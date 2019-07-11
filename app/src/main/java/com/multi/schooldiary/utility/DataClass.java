package com.multi.schooldiary.utility;

import com.google.firebase.database.DataSnapshot;

public class DataClass {
    String id,value;
    DataSnapshot dataSnapshot;

    public DataClass(String id, DataSnapshot dataSnapshot) {
        this.id = id;
        this.dataSnapshot = dataSnapshot;
    }

    public DataClass(String id, String value, DataSnapshot dataSnapshot) {
        this.id = id;
        this.value = value;
        this.dataSnapshot = dataSnapshot;
    }

    public DataClass() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DataSnapshot getDataSnapshot() {
        return dataSnapshot;
    }

    public void setDataSnapshot(DataSnapshot dataSnapshot) {
        this.dataSnapshot = dataSnapshot;
    }
}
