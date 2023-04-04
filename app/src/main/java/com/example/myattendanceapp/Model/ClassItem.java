package com.example.myattendanceapp.Model;

import java.io.Serializable;

public class ClassItem implements Serializable {
    private int s_id, t_id;
    private String subject_name, subject_code;

    public ClassItem(String name, String object) {
        this.subject_name = name;
        this.subject_code = object;
    }

    public int getS_id() {
        return s_id;
    }

    public void setS_id(int s_id) {
        this.s_id = s_id;
    }

    public int getT_id() {
        return t_id;
    }

    public void setT_id(int t_id) {
        this.t_id = t_id;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getSubject_code() {
        return subject_code;
    }

    public void setSubject_code(String subject_code) {
        this.subject_code = subject_code;
    }
}
