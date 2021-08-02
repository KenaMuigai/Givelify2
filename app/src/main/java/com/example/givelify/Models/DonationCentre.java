package com.example.givelify.Models;

import java.util.ArrayList;

public class DonationCentre {
    private String name;
    private String description;
    private ArrayList<DonationItem> itemList;
    private String category;
    private String city;
    private Integer imageUrl;

    public DonationCentre(){

        this.name = "";
        this.description="";
        this.itemList= new ArrayList<>();
        this.category="";
        this.city="";
        this.imageUrl=0;
    }
    public DonationCentre(String name, String description, ArrayList<DonationItem> itemList, String category, String city, Integer imageUrl){

        this.name = name;
        this.description= description;
        this.itemList= itemList;
        this.category=category;
        this.city=city;
        this.imageUrl=imageUrl;
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

    public ArrayList<DonationItem> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<DonationItem> itemList) {
        this.itemList = itemList;
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
}
