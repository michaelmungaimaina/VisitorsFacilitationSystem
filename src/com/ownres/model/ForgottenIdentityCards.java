package com.ownres.model;

public class ForgottenIdentityCards {
    String firstName;
    String secondName;
    String thirdName;
    int idNumber;

    public ForgottenIdentityCards(String firstName, String secondName, String thirdName, int idNumber){
        this.firstName = firstName;
        this.secondName = secondName;
        this.thirdName = thirdName;
        this.idNumber = idNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getThirdName() {
        return thirdName;
    }

    public int getIdNumber() {
        return idNumber;
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
}
