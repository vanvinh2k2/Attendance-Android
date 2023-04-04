package com.example.myattendanceapp.Model;

public class StatusItem {
    String st_id,date_time,status, s_id;

    public StatusItem(String s_id, String date_time, String status, String c_id) {
        this.st_id = s_id;
        this.date_time = date_time;
        this.status = status;
        this.s_id = c_id;
    }

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

    public String getSt_id() {
        return st_id;
    }

    public void setSt_id(String st_id) {
        this.st_id = st_id;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
