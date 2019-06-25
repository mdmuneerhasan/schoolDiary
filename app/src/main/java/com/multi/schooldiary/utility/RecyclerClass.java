package com.multi.schooldiary.utility;

public class RecyclerClass {
    String url,heading,text1,text2,text3,text4,tvNotification;


    public RecyclerClass() {
    }

    public RecyclerClass(String url, String heading, String text1, String text2, String text3, String text4, String tvNotification) {
        this.url = url;
        this.heading = heading;
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.text4 = text4;
        this.tvNotification = tvNotification;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getText3() {
        return text3;
    }

    public void setText3(String text3) {
        this.text3 = text3;
    }

    public String getText4() {
        return text4;
    }

    public void setText4(String text4) {
        this.text4 = text4;
    }

    public String getTvNotification() {
        return tvNotification;
    }

    public void setTvNotification(String tvNotification) {
        this.tvNotification = tvNotification;
    }
}
