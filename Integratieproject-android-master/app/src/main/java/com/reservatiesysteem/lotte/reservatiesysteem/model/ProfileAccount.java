package com.reservatiesysteem.lotte.reservatiesysteem.model;

import android.widget.ArrayAdapter;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by lotte on 26/02/2017.
 */

public class ProfileAccount implements Serializable {
    @SerializedName("Id")
    private String id;
    @SerializedName("Email")
    private String email;
    @SerializedName("Name")
    private String firstname;
    @SerializedName("Lastname")
    private String lastname;
    @SerializedName("PhoneNumber")
    private String phoneNumber;
    @SerializedName("Reservations")
    private ArrayList<Reservation> reservations;
    @SerializedName("Favorites")
    private ArrayList<Branch>favorites;

    public void setFavorites(ArrayList<Branch> favorites) {
        this.favorites = favorites;
    }

    public ArrayList<Branch> getFavorites() {

        return favorites;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(ArrayList<Reservation> reservations) {
        this.reservations = reservations;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /*public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }*/
}
