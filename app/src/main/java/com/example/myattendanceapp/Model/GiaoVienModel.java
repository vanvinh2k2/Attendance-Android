package com.example.myattendanceapp.Model;

import java.util.List;

public class GiaoVienModel {
    private boolean success;
    private String message;
    private List<GiaoVien> result;

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

    public List<GiaoVien> getResult() {
        return result;
    }

    public void setResult(List<GiaoVien> result) {
        this.result = result;
    }
}
