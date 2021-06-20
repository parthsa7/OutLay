package com.example.practice;

import java.io.Serializable;

public class UserInfo implements Serializable {

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    String username="" , userEmail="" , mobilenumber="";

    public UserInfo(){

    }

    public UserInfo(String name , String email , String mobileno){
        username = name;
        userEmail = email;
        mobilenumber =mobileno;
    }
}
