package com.ownres.model;

public class VisitorInstance {
    String visitorId;
    String visitorFirstName;
    String visitorSecondName;
    String visitorThirdName;
    String visitorGender;
    String visitorPhoneNumber;

    public  VisitorInstance(String visitorId, String visitorFirstName, String visitorSecondName, String visitorThirdName,String visitorGender, String visitorPhoneNumber){
        this.visitorId = visitorId;
        this.visitorFirstName = visitorFirstName;
        this.visitorSecondName = visitorSecondName;
        this.visitorThirdName = visitorThirdName;
        this.visitorGender = visitorGender;
        this.visitorPhoneNumber = visitorPhoneNumber;
    }

    public String getVisitorId(){ return visitorId; }
    public String getVisitorFirstName(){ return  visitorFirstName; }
    public String getVisitorSecondName(){ return visitorSecondName; }
    public String getVisitorThirdName(){ return visitorThirdName; }
    public String getVisitorGender(){ return visitorGender; }
    public String getVisitorPhoneNumber(){ return visitorPhoneNumber; }

    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }

    public void setVisitorFirstName(String visitorFirstName) {
        this.visitorFirstName = visitorFirstName;
    }

    public void setVisitorSecondName(String visitorSecondName) {
        this.visitorSecondName = visitorSecondName;
    }

    public void setVisitorThirdName(String visitorThirdName) {
        this.visitorThirdName = visitorThirdName;
    }

    public void setVisitorGender(String visitorGender) {
        this.visitorGender = visitorGender;
    }

    public void setVisitorPhoneNumber(String visitorPhoneNumber) {
        this.visitorPhoneNumber = visitorPhoneNumber;
    }
}
