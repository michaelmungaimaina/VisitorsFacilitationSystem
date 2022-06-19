package com.ownres.model;
import com.ownres.controller.LoginController;

public class Login {
    private String userNationalId;
    private String password;
    private String designation;


//LoginController loginController=new LoginController();
    public  Login(String userNationalId, String password, String designation) {
        this.userNationalId =userNationalId ;
        this.password = password;
        this.designation = designation;
    }

public Login(){

}

    public String getUserNationalId() {
        return userNationalId;
    }

    public String getPassword() {
        return password;
    }
    public String getDesignation() {return designation;}

    }
