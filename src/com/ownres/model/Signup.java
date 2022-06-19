package com.ownres.model;


public class Signup {
    private String firstName;
    private String secondName;
    private String lastName;
    private String gender;
    private int nationalId;
    private int phoneNumber;
    private String designation;
    private String password;


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSecondName(String secondName){ this.secondName = secondName; }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNationalId(int nationalId){ this.nationalId = nationalId; }

    public void setPhoneNumber(int phoneNumber){ this.phoneNumber = phoneNumber; }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() { return secondName; }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getDesignation() {
        return designation;
    }

    public String getPassword() {
        return password;
    }

    public int getNationalId() { return  nationalId; }

    public int getPhoneNumber() { return phoneNumber; }

    public Signup(String firstName, String secondName, String lastName, String gender,int phoneNumber, int nationalId, String designation, String password) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.nationalId = nationalId;
        this.designation = designation;
        this.password = password;
    }
}
