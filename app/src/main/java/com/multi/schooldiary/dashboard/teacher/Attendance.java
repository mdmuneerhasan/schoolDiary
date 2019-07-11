package com.multi.schooldiary.dashboard.teacher;

public class Attendance {
    String uid,attendance;

    public Attendance() {
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

    public Attendance(String uid, String attendance) {
        this.uid = uid;
        this.attendance = attendance;
    }
}
