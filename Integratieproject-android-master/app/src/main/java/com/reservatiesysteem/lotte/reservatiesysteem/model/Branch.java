package com.reservatiesysteem.lotte.reservatiesysteem.model;

import android.media.Image;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Jasper on 10/02/2017.
 */
public class Branch {
    @SerializedName("Id")
    private int id;
    @SerializedName("Name")
    private String name;
    @SerializedName("Street")
    private String street;
    @SerializedName("Number")
    private String number;
    @SerializedName("Box")
    private String box;
    @SerializedName("CityId")
    private int cityId;
    @SerializedName("City")
    private City city;
    @SerializedName("Available")
    private boolean available;
    @SerializedName("Description")
    private String description;
    @SerializedName("Picture")
    private String picture;
    @SerializedName("PhoneNumber")
    private String phoneNumber;
    @SerializedName("Email")
    private String email;
    @SerializedName("CompanyId")
    private int companyId;

    @SerializedName("OpeningHours")
    private ArrayList<OpeningHour> openingHours;

    /*@SerializedName("Rooms")
    private String rooms;*/

    @SerializedName("AdditionalInfos")
    private ArrayList<AdditionalInfo> additionalInfo;

    @SerializedName("Reviews")
    private ArrayList<Review> reviews;

    /*@SerializedName("Messages")
    private String messages;*/

    /*@SerializedName("Reservations")
    private String reservations;*/


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBox() {
        return box;
    }

    public void setBox(String box) {
        this.box = box;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public ArrayList<OpeningHour> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(ArrayList<OpeningHour> openingHours) {
        this.openingHours = openingHours;
    }

    public ArrayList<AdditionalInfo> getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(ArrayList<AdditionalInfo> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
