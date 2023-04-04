package com.example.myattendanceapp.Model;

public class Student {
    private String roll;
    private String name;
    private String status;

    public Student(String roll, String name) {
        this.roll = roll;
        this.name = name;
        status = "A";
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
