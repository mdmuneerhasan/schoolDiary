package com.multi.schooldiary.services;

public class ServiceItemClass {
    String url,title,description,price,detail,size;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public ServiceItemClass() {
    }

    public ServiceItemClass(String url, String title, String description, String price, String detail, String size) {

        this.url = url;
        this.title = title;
        this.description = description;
        this.price = price;
        this.detail = detail;
        this.size = size;
    }
}
