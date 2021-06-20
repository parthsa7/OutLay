    package com.example.practice;

import java.io.Serializable;

public class SpendDatabase implements Serializable {

    public String getSpendAmount() {
        return SpendAmount;
    }

    public void setSpendAmount(String spendAmount) {
        SpendAmount = spendAmount;
    }

    public String getSpendDate() {
        return SpendDate;
    }

    public void setSpendDate(String spendDate) {
        SpendDate = spendDate;
    }

    public String getSpendId() {
        return SpendId;
    }

    public void setSpendId(String spendId) {
        SpendId = spendId;
    }

    public String getSpendActivity() {
        return SpendActivity;
    }

    public void setSpendActivity(String spendActivity) {
        SpendActivity = spendActivity;
    }

    String SpendAmount="", SpendDate="",SpendId="" , SpendActivity="";

    public SpendDatabase(){

    }

    public SpendDatabase(String spendAmount, String spendDate, String spendId , String spendActivity) {
        SpendAmount = spendAmount;
        SpendDate = spendDate;
        SpendId = spendId;
        SpendActivity = spendActivity;
    }
}
