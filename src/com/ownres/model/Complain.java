package com.ownres.model;

public class Complain {
    String date;
    String firstName;
    String secondName;
    String thirdName;
    int idNumber;
    int phoneNumber;
    String kraPin;
    String department;
    String complain;
    String action;
    String status;

    public Complain (String date, String firstName, String secondName, String thirdName, int idNumber, int phoneNumber, String kraPin, String department, String complain, String action, String status){
        this.date = date;
        this.firstName = firstName;
        this.secondName = secondName;
        this.thirdName = thirdName;
        this.idNumber = idNumber;
        this.phoneNumber = phoneNumber;
        this.kraPin = kraPin;
        this.department = department;
        this.complain = complain;
        this.action = action;
        this.status = status;
    }

    public String getDate(){
        return date;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getSecondName(){
        return secondName;
    }
    public String getThirdName() {
        return thirdName;
    }
    public int getIdNumber() {
        return idNumber;
    }
    public int getPhoneNumber() {
        return phoneNumber;
    }
    public String getKraPin() {
        return kraPin;
    }
    public String getDepartment() {
        return department;
    }
    public String getComplain() {
        return complain;
    }
    public String getAction() {
        return action;
    }
    public String getStatus() {
        return status;
    }


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
    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setKraPin(String kraPin) {
        this.kraPin = kraPin;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public void setComplain(String complain) {
        this.complain = complain;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
