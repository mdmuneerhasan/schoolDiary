package com.multi.schooldiary.utility;

public class SetDefaultClass {
    String schoolName,position,stClass,schoolId;
    Boolean permission;
    String uid,rollNo;

    public SetDefaultClass() {
    }

    public SetDefaultClass(String schoolName, String position, String stClass, String schoolId) {
        this.schoolName = schoolName;
        this.position = position;
        this.stClass = stClass;
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getPosition() {
        return position;
    }

    public String getUid() {
        return uid;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Boolean getPermission() {
        return permission;
    }

    public void setPermission(Boolean permission) {
        this.permission = permission;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStClass() {
        return stClass;
    }

    public void setStClass(String stClass) {
        this.stClass = stClass;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }
}
