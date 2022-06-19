package com.ownres.model;

public class UserValidity {
    private static String userId;
    String userDesignation;

    public UserValidity(String userId){
       UserValidity.setUserId(userId);
    }

    public static String getUserId(){
        return UserValidity.userId;
    }

    public static void setUserId(String userId){
        UserValidity.userId = userId;
    }


    public String getUserDesignation() {
        return userDesignation;
    }

    public void setUserDesignation(String userDesignation) {
        this.userDesignation = userDesignation;
    }
}
