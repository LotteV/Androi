package com.reservatiesysteem.lotte.reservatiesysteem.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lotte on 2/03/2017.
 */

public class Message {
    @SerializedName("Id")
    private int id;
    @SerializedName("ReservationId")
    private int reservationId;
    @SerializedName("BranchId")
    private int branchId;
    @SerializedName("UserId")
    private String userId;
    @SerializedName("User")
    private ProfileAccount user;
    @SerializedName("Text")
    private String text;
    @SerializedName("DateTime")
    private String dateTime;

    public Message(int reservationId, int branchId, String text) {
        this.reservationId = reservationId;
        this.branchId = branchId;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ProfileAccount getUser() {
        return user;
    }

    public void setUser(ProfileAccount user) {
        this.user = user;
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
}
