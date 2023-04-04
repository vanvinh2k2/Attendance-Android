package com.example.myattendanceapp.Model;

import java.util.List;

public class ClassItemModel {
    boolean success;
    String message;
    List<ClassItem> result;

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

    public List<ClassItem> getResult() {
        return result;
    }

    public void setResult(List<ClassItem> result) {
        this.result = result;
    }
}
