package com.multi.schooldiary.timetable;

import javax.security.auth.Subject;

public class TimeTable {
    String subject,time,teacher;

    public TimeTable(String subject, String time, String teacher) {
        this.subject = subject;
        this.time = time;
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return subject;
    }

    public TimeTable() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
