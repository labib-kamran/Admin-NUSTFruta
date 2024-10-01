package com.labibkamran.adminnustfruta.Models;

public class AdminModel {
    private String key = "";
    private String name ="";
    private String address = "";
    private String email = "";
    private String phoneNumber ="";
    private String designation = "";


    // Default constructor required for calls to DataSnapshot.getValue(AdminModel.class)
    public AdminModel() {
    }

    // Constructor
    public AdminModel(String name, String address, String email, String phoneNumber, String designation) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.designation = designation;
    }

    // Getter and Setter methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

}
