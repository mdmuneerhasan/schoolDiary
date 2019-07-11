package com.multi.schooldiary.utility;

import java.util.Comparator;

public class SetDefaultClass {
    String schoolName,position,stClass,schoolId;
    Boolean permission;
    String uid,rollNo,name;
    String sid;
    public SetDefaultClass() {
    }

    public SetDefaultClass(String schoolName, String position, String stClass, String schoolId) {
        this.schoolName = schoolName;
        this.position = position;
        this.stClass = stClass;
        this.schoolId = schoolId;
    }
    public static Comparator<SetDefaultClass> rollNoSort = new Comparator<SetDefaultClass>() {

        public int compare(SetDefaultClass s1, SetDefaultClass s2) {
            try{
                return Integer.parseInt(s1.rollNo)-Integer.parseInt(s2.rollNo);
            }catch (Exception e){
                return s1.rollNo.compareTo(s2.rollNo);
            }
        }};


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
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
