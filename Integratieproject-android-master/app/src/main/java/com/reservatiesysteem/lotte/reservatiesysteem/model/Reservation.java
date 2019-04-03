package com.reservatiesysteem.lotte.reservatiesysteem.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lotte on 26/02/2017.
 */

public class Reservation implements Serializable {
    @SerializedName("Id")
    private int id;
    @SerializedName("AmountOfPersons")
    private int amount;
    @SerializedName("DateTime")
    private String dateTime;
    @SerializedName("EndDateTime")
    private String endDateTime;
    @SerializedName("Cancelled")
    private boolean cancelled;
    @SerializedName("BranchId")
    private int branchId;
    @SerializedName("AccountId")
    private String accountId;

    /*@SerializedName("Messages")
    private ArrayList<Message> messages;
    @SerializedName("Review")
    private ArrayList<Review> reviews;*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

}
