package com.example.givelify.Models;

import java.util.ArrayList;

public class DonationCentre {
    private String name;
    private String email;
    private String description;
    private String category;
    private String city;
    private String pass;
    private String phone;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    private String createdAt;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    private String website;
    private Integer imageUrl;

    public DonationCentre(String name, String description, ArrayList<String> itemList, String category, String city, Integer imageUrl,String createdAt){
        this.name = name;
        this.description= description;
        this.category=category;
        this.city=city;
        this.imageUrl=imageUrl;
        this.createdAt=createdAt;
    }
    public  DonationCentre(){
        this.name = "";
        this.email="";
        this.pass= "";
        this.phone="";
        this.website="";
        this.description= "";
        this.category="";
        this.city="";
        this.createdAt="";
    }


    public DonationCentre(String name, String email, String pass, String description, String category, String website,String city,String phone, Boolean isVerified,String createdAt){

        this.name = name;
        this.email=email;
        this.pass= pass;
        this.phone=phone;
        this.website=website;
        this.description= description;
        this.category=category;
        this.city=city;
        this.createdAt=createdAt;
    }
    public Integer getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Integer imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
