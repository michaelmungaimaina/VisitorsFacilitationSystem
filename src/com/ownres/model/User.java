package com.ownres.model;

import javafx.beans.property.SimpleIntegerProperty;

public class User {
    private int userNationalID;
    private int userPhoneNumber;
    private String userFirstName;
    private String userSecondName;
    private String userLastName;
    private String userGender;
    private String userDesignation;
    private String userPassword;


    public User(int userNationalID, String userFirstName, String userSecondName, String userLastName, String userGender,
                String userDesignation,int userPhoneNumber, String userPassword) {
        this.userNationalID = userNationalID;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userGender = userGender;
        this.userDesignation = userDesignation;
        this.userSecondName = userSecondName;
        this.userPassword = userPassword;
        this.userPhoneNumber = userPhoneNumber;
    }

    public int getUserNationalID() {
        return userNationalID;
    }
    public int getUserPhoneNumber() { return userPhoneNumber; }
    public String getUserFirstName() {
        return userFirstName;
    }
    public String getUserSecondName(){ return  userSecondName; }
    public String getUserLastName() {
        return userLastName;
    }
    public String getUserGender() {
        return userGender;
    }
    public String getUserDesignation() {
        return userDesignation;
    }
    public String getUserPassword() {
        return userPassword;
    }



    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public void setUserDesignation(String userDesignation) {
        this.userDesignation = userDesignation;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public void setUserNationalID(int userID) {
        this.userNationalID = userNationalID;
    }

    public void setUserSecondName(String userSecondName) {
        this.userSecondName = userSecondName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public void setUserPhoneNumber(int userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
