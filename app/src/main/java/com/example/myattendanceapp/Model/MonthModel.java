package com.example.myattendanceapp.Model;

import java.util.List;

public class MonthModel {
    boolean success;
    List<Month> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Month> getResult() {
        return result;
    }

    public void setResult(List<Month> result) {
        this.result = result;
    }
}
