package com.multi.schooldiary.school;

public class Upload {
    String title, description,imageUri,schoolName,uploaderName,time,uid,key,schoolPhoto,likeCount;

    public String getSchoolPhoto() {
        return schoolPhoto;
    }

    public void setSchoolPhoto(String schoolPhoto) {
        this.schoolPhoto = schoolPhoto;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public Upload(String title, String description, String imageUri, String time, String uploaderName, String schoolName, String uid) {
        this.title = title;
        this.description = description;
        this.imageUri = imageUri;
        this.schoolName = schoolName;
        this.uploaderName = uploaderName;
        this.time = time;
        this.uid=uid;
    }


    public Upload() {
    }


    public String getTitle() {
        return title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
