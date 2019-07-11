package com.multi.schooldiary.notification;

import com.google.firebase.database.DataSnapshot;

public class Notification {
    // define type 1 for activity 2 for url
    String title,text,type,url,activity,photoUrl,description;
    DataSnapshot dataSnapshot;


    public Notification(String title, String text, DataSnapshot dataSnapshot) {
        this.title = title;
        this.text = text;
        this.dataSnapshot = dataSnapshot;
    }

    public Notification(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public Notification() {
    }

    public Notification(String title, String text, String type, String url, String activity, DataSnapshot dataSnapshot) {
        this.title = title;
        this.text = text;
        this.type = type;
        this.url = url;
        this.activity = activity;
        this.dataSnapshot = dataSnapshot;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public DataSnapshot getDataSnapshot() {
        return dataSnapshot;
    }

    public void setDataSnapshot(DataSnapshot dataSnapshot) {
        this.dataSnapshot = dataSnapshot;
    }
}
