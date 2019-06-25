package com.multi.schooldiary.teacher;

public class Attendance {
    String name,rollNo,uid,attendance;

    public Attendance() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public Attendance(String name, String rollNo, String uid, String attendance) {
        this.name = name;
        this.rollNo = rollNo;
        this.uid = uid;
        this.attendance = attendance;
    }
}
