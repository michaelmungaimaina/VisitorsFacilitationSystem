package com.ownres.model;

public class Session {
    int userId;
    String time;
    String firstName;
    String secondName;
    String userType;

    public Session (String time, int userId, String firstName, String secondName, String userType){
        this.time = time;
        this.userId = userId;
        this.firstName = firstName;
        this.secondName = secondName;
        this.userType = userType;
    }

    public String getTime() {
        return time;
    }

    public int getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getUserType() {
        return userType;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
