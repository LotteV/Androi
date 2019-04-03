package com.reservatiesysteem.lotte.reservatiesysteem.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lotte on 2/03/2017.
 */

public class Review {
    @SerializedName("Id")
    private int id;
    @SerializedName("Text")
    private String text;
    @SerializedName("DateTime")
    private String dateTime;
    @SerializedName("BranchId")
    private int branchId;
    @SerializedName("Result")
    private boolean result;
    @SerializedName("User")
    private ProfileAccount user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public ProfileAccount getUser() {
        return user;
    }

    public void setUser(ProfileAccount user) {
        this.user = user;
    }
}
