package com.example.myapplication.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// CZSLD5PrmqMEn6asWWCM

// https://api.iextrading.com/1.0/stock/market/collection/sector?collectionName=Health%20Care
//https://programming-quotes-api.herokuapp.com/quotes/lang?=en

public class Quotes {

    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("en")
    @Expose
    private String en;
    @SerializedName("author")
    @Expose
    private String author;

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}