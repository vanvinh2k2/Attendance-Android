package com.example.myattendanceapp.Model;

import java.util.List;

public class StatusGroupModel {
    private boolean success;
    private List<Status> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Status> getResult() {
        return result;
    }

    public void setResult(List<Status> result) {
        this.result = result;
    }
}
