package com.ownres.model;

public class Visitor {
    String date;
    String firstName;
    String secondName;
    String thirdName;
    int idNumber;
    String gender;
    int phoneNumber;
    int roomVisit;
    String timeIn;
    String timeOut;

    public Visitor(String date, String firstName, String secondName, String thirdName, int idNumber, String gender, int phoneNumber, int roomVisit, String timeIn, String timeOut){
        this.date = date;
        this.firstName = firstName;
        this.secondName = secondName;
        this.thirdName = thirdName;
        this.idNumber = idNumber;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.roomVisit = roomVisit;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }

    public String getDate(){ return  date; }
    public String getFirstName(){ return  firstName; }
    public String getSecondName(){ return secondName; }
    public String getThirdName(){ return thirdName; }
    public int getIdNumber(){ return idNumber; }
    public String getGender(){ return gender; }
    public int getPhoneNumber(){ return phoneNumber; }
    public int getRoomVisit(){ return roomVisit; }
    public String getTimeIn(){ return timeIn; }
    public String getTimeOut(){ return timeOut; }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setRoomVisit(int roomVisit) {
        this.roomVisit = roomVisit;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }
}
