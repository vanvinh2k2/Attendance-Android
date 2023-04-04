package com.example.myattendanceapp.Model;

import java.util.List;

public class StudentItemModel {
    boolean success;
    String message;
    List<StudentItem> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<StudentItem> getResult() {
        return result;
    }

    public void setResult(List<StudentItem> result) {
        this.result = result;
    }
}
