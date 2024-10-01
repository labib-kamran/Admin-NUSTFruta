package com.labibkamran.adminnustfruta.Models;

public class Allmenu {
    private String foodname;
    private String foodprice;
    private String fooddescription;
    private String foodImage;
    private String key;

    // Constructor
    public Allmenu() {

    }

    public Allmenu(String foodname, String foodprice, String fooddescription, String foodImage,String key) {
        this.foodname = foodname;
        this.foodprice = foodprice;
        this.fooddescription = fooddescription;
        this.foodImage = foodImage;
        this.key = key;
    }

    // Getters
    public String getFoodname() {
        return foodname;
    }

    public String getFoodprice() {
        return foodprice;
    }

    public String getFooddescription() {
        return fooddescription;
    }

    public String getFoodImage() {
        return foodImage;
    }

    // Setters
    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public void setFoodprice(String foodprice) {
        this.foodprice = foodprice;
    }

    public void setFooddescription(String fooddescription) {
        this.fooddescription = fooddescription;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }
}
