package com.example.practice;

import java.io.Serializable;

public class Earndatabase implements Serializable {


    public String getEarnAmount() {
        return EarnAmount;
    }

    public void setEarnAmount(String earnAmount) {
        EarnAmount = earnAmount;
    }

    public String getEarnDate() {
        return EarnDate;
    }

    public void setEarnDate(String earnDate) {
        EarnDate = earnDate;
    }

    public String getEarnId() {
        return EarnId;
    }

    public void setEarnId(String earnId) {
        EarnId = earnId;
    }

    public String getEarnActivity() {
        return EarnActivity;
    }

    public void setEarnActivity(String earnActivity) {
        EarnActivity = earnActivity;
    }

    String EarnAmount="",EarnDate="",EarnId="",EarnActivity="";

    public Earndatabase(){

    }


    public Earndatabase(String earnAmount, String earnDate, String earnId , String earnActivity) {
        EarnAmount = earnAmount;
        EarnDate = earnDate;
        EarnId = earnId;
        EarnActivity = earnActivity;
    }
}
